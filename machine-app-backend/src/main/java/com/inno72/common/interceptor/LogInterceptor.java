package com.inno72.common.interceptor;

import com.alibaba.fastjson.JSON;
import com.inno72.common.CommonConstants;
import com.inno72.common.Result;
import com.inno72.common.SessionData;
import com.inno72.redis.IRedisUtil;
import com.inno72.utils.page.Pagination;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 * 项目拦截器
 * 
 * @author lzh
 *
 */
public class LogInterceptor extends HandlerInterceptorAdapter {

	@Resource
	private IRedisUtil redisUtil; // memcachedClient

	private static List<String> doNotCheckUs = Arrays.asList(new String[] { "/check/user/smsCode","/check/user/login" });

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		// 检查POST方法，token，url权限, 启用后删除检查参数中的token
		 checkAuthority(request,response);
		@SuppressWarnings("rawtypes")
		Enumeration enumeration = request.getParameterNames();
		StringBuffer parm = new StringBuffer();

		// 移除分页对象
		Pagination.threadLocal.remove();
		while (enumeration.hasMoreElements()) {
			Object element = enumeration.nextElement();
			if (element instanceof String) {
				String name = (String) element;
				Object attr = request.getParameter(name);
				boolean isv = false;
				if (name.equals("v") || name.equals("V")) {
					isv = true;
				}
				if (!isv) {
					parm.append(name).append("=").append(attr).append("&");

					String attrStr = (String) attr;

					if (name.equals("pageNo")) {
						Pagination pagination = new Pagination();
						int pageNo = 1;
						try {
							if (attrStr.indexOf("_") != -1) {
								pageNo = Integer.parseInt(attrStr.split("_")[0]);
								pagination.setPageSize(Integer.parseInt(attrStr.split("_")[1]));
							} else {
								pageNo = Integer.parseInt(attrStr);
							}
							pagination.setPageNo(pageNo);
						} catch (Exception e) {
						}
						pagination.setPageNo(pageNo);
						Pagination.threadLocal.set(pagination);
					}
				}

			}
		}

		return true;

	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {

		// 获取controller返回值

		if (modelAndView != null) {
			Map<String, Object> model = modelAndView.getModel();
			Map<String, Object> newModel = new HashMap<String, Object>();
			for (Map.Entry<String, Object> item : model.entrySet()) {
				Object attr = item.getValue();
				// 把所有值为空的key变为""
				if (attr == null) {
					newModel.put(item.getKey(), "");
				}

			}
			modelAndView.addAllObjects(newModel);

		}

		log(request, response, modelAndView);

	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
	}

	private String log(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) {

		Map<String, Object> map = new HashMap<>();

		// 请求参数
		map.put("request_data", request.getParameterMap());

		// 响应结果
		Map<String, Object> response_data = new HashMap<String, Object>();
		if (modelAndView != null) {
			Map<String, Object> model = modelAndView.getModel();

			model.entrySet().forEach(entry -> {
				Object attr = entry.getValue();
				String key = entry.getKey();

				if ("result".equals(key)) {
					response_data.put(key, JSON.toJSONString(attr));
				}
				if ("data".equals(key)) {
					response_data.put(key, JSON.toJSONString(attr));
				}
				if ("code".equals(key)) {
					response_data.put(key, JSON.toJSONString(attr));
				}
			});
		}
		map.put("response_data", response_data);

		Map<String, Object> _log = new HashMap<>();
		_log.put(request.getRequestURI(), map);

		return null;
	}

	@SuppressWarnings("unused")
	private boolean checkAuthority(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String url = request.getServletPath();
		boolean match = doNotCheckUs.parallelStream().anyMatch(_url -> url.indexOf(_url) != -1);
		if (match) {
			return true;
		}
		// 获取请求方法
		String requestMethod = request.getMethod().toUpperCase();
		if (requestMethod.equals("GET") || requestMethod.equals("POST") || requestMethod.equals("DELETE")
				|| requestMethod.equals("PUT")) {
			if (!match) {
				// lf-None-Matoh 传入token
				String token = request.getHeader("lf-None-Matoh");
				if (token == null) {
					Result<String> result = new Result<>();
					result.setCode(999);
					result.setMsg("你未登录，请登录");
					String origin = request.getHeader("Origin");
					response(response, origin);
					PrintWriter out = null;
					try {
						out = response.getWriter();
						out.append(JSON.toJSONString(result));
					} catch (IOException e) {
						e.printStackTrace();
					} finally {
						if (out != null) {
							out.close();
						}
					}
					return false;
				}
				String info = redisUtil.get(CommonConstants.USER_LOGIN_CACHE_KEY_PREF + token);
				if (info == null) {
					// 判断用户是否被踢出
					boolean checkout = redisUtil.sismember(CommonConstants.CHECK_OUT_USER_TOKEN_SET_KEY, token);
					redisUtil.srem(CommonConstants.CHECK_OUT_USER_TOKEN_SET_KEY, token);
					Result<String> result = new Result<>();
					if (checkout) {
						result.setCode(888);
						result.setMsg("你的账号在另一处登录，你已被踢出");
					} else {
						result.setCode(999);
						result.setMsg("你登录超时，请重新登录");
					}
					String origin = request.getHeader("Origin");
					response(response, origin);
					PrintWriter out = null;
					try {
						out = response.getWriter();
						out.append(JSON.toJSONString(result));
					} catch (IOException e) {
						e.printStackTrace();
					} finally {
						if (out != null) {
							out.close();
						}
					}
					return false;
				} else {
					String _info = info.toString();
					CommonConstants.SESSION_DATA = JSON.parseObject(_info, SessionData.class);
				}
			}
		}
		return true;
	}

	private void response(HttpServletResponse response, String origin) {
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json; charset=utf-8");
		response.setHeader("Access-Control-Allow-Origin", origin);
		response.setHeader("Vary", "Origin");
		// Access-Control-Max-Age
		response.setHeader("Access-Control-Max-Age", "3600");
		// Access-Control-Allow-Credentials
		response.setHeader("Access-Control-Allow-Credentials", "true");
		// Access-Control-Allow-Methods
		response.setHeader("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, HEAD, OPTIONS");
		// Access-Control-Allow-Headers
		response.setHeader("Access-Control-Allow-Headers",
				"Origin, X-Requested-With, Content-Type, Accept, lf-None-Matoh");
	}

	public IRedisUtil getRedisUtil() {
		return redisUtil;
	}

	public void setRedisUtil(IRedisUtil redisUtil) {
		this.redisUtil = redisUtil;
	}

}
