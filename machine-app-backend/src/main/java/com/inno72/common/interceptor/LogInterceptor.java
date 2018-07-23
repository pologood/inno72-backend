package com.inno72.common.interceptor;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.alibaba.fastjson.JSON;

/**
 * 项目拦截器
 * 
 * @author lzh
 *
 */
public class LogInterceptor extends HandlerInterceptorAdapter {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		// StringBuilder stringBuilder = new StringBuilder();
		// BufferedReader bufferedReader = null;
		// try {
		// InputStream inputStream = request.getInputStream();
		// if (inputStream != null) {
		// bufferedReader = new BufferedReader(new
		// InputStreamReader(inputStream));
		// char[] charBuffer = new char[128];
		// int bytesRead = -1;
		// while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
		// stringBuilder.append(charBuffer, 0, bytesRead);
		// }
		// } else {
		// stringBuilder.append("");
		// }
		// } catch (IOException ex) {
		// throw ex;
		// } finally {
		// if (bufferedReader != null) {
		// try {
		// bufferedReader.close();
		// } catch (IOException ex) {
		// throw ex;
		// }
		// }
		// }
		// String body = stringBuilder.toString();
		// System.out.println(body);
		// response.getOutputStream().write(body.getBytes());
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

}
