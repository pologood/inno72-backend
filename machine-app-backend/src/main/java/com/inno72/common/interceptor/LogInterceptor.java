package com.inno72.common.interceptor;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.alibaba.fastjson.JSON;
import com.inno72.redis.IRedisUtil;
import com.inno72.utils.page.Pagination;

/**
 * 项目拦截器
 * 
 * @author lzh
 *
 */
public class LogInterceptor extends HandlerInterceptorAdapter {

	@Resource
	private IRedisUtil redisUtil; // memcachedClient

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		// 检查POST方法，token，url权限, 启用后删除检查参数中的token
		checkAuthority(request, response);
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

	private boolean checkAuthority(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return true;
	}

	public IRedisUtil getRedisUtil() {
		return redisUtil;
	}

	public void setRedisUtil(IRedisUtil redisUtil) {
		this.redisUtil = redisUtil;
	}

}
