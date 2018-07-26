package com.inno72.common;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;

@Aspect
@Component
public class LogCut {
	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Pointcut("execution(* com.inno72.*.controller.*.*(..))")
	public void pointcut() {
	}

	@Around("pointcut()")
	public Object handle(ProceedingJoinPoint joinPoint) throws Throwable {
		preHandle();
		Object retVal = joinPoint.proceed();

		postHandle(retVal);
		return retVal;
	}

	private void preHandle() throws IOException {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		logger.info("使用{}方式请求地址{}", request.getMethod().toUpperCase(), request.getServletPath());
		String token = request.getHeader("lf-None-Matoh") == null ? "无" : request.getHeader("lf-None-Matoh");

		// String jsonStr = new
		// String(StreamUtils.copyToByteArray(request.getInputStream()));
		String jsonStr = "";
		if (!StringUtil.isEmpty(jsonStr)) {
			logger.info("请求token：{},json参数：{}", token, jsonStr);
		} else {
			@SuppressWarnings("rawtypes")
			Enumeration enumeration = request.getParameterNames();
			StringBuffer parm = new StringBuffer();

			while (enumeration.hasMoreElements()) {
				Object element = enumeration.nextElement();
				if (element instanceof String) {
					String name = (String) element;
					Object attr = request.getParameter(name);
					parm.append(name).append("=").append(attr).append("&");
				}
			}
			logger.info("请求token：{},param参数：{}", token, jsonStr);
		}
	}

	private void postHandle(Object retVal) {
		if (retVal instanceof ModelAndView) {
			ModelAndView modelAndView = (ModelAndView) retVal;
			Map<String, Object> model = modelAndView.getModel();
			Map<String, Object> newModel = new HashMap<String, Object>();
			for (Map.Entry<String, Object> item : model.entrySet()) {
				Object attr = item.getValue();
				// 把所有值为空的key变为""
				if (attr == null) {
					newModel.put(item.getKey(), "");
				}
			}
			return;
		}
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		logger.info("请求地址{}返回值为：{}", request.getServletPath(), JSON.toJSONString(retVal));
	}

}
