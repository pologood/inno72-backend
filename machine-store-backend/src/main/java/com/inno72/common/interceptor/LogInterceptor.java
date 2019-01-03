package com.inno72.common.interceptor;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.alibaba.fastjson.JSON;
import com.inno72.common.CommonConstants;
import com.inno72.common.DataAutherInterceptor;
import com.inno72.common.Result;
import com.inno72.common.SessionData;
import com.inno72.common.SessionUtil;
import com.inno72.redis.IRedisUtil;
import com.inno72.utils.page.Pagination;

/**
 * 项目拦截器
 * 
 * @author lzh
 *
 */
public class LogInterceptor extends HandlerInterceptorAdapter {
	Logger log = LoggerFactory.getLogger(this.getClass());
	@Resource
	private IRedisUtil redisUtil; // memcachedClient

	private static List<String> doNotCheckUs = Arrays.asList(new String[] { "/admin/area/list" });

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		boolean result = checkAuthority(request, response);
		if (result) {
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
		}
		return result;

	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {

		// 获取controller返回值

		if (modelAndView != null) {
			Map<String, Object> model = modelAndView.getModel();
			model.put("unColumn", DataAutherInterceptor.forbiddenColumn());
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

	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
	}

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
				String info = redisUtil.get(CommonConstants.STORE_KEEPER_TOKEN_KEY_PREF + token);
				if (info == null) {
					Result<String> result = new Result<>();
					result.setCode(999);
					result.setMsg("你登录超时，请重新登录");
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
					SessionData session = JSON.parseObject(_info, SessionData.class);
					SessionUtil.sessionData.set(session);
					DataAutherInterceptor.setUserId(session.getStorekeeper().getId());
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
