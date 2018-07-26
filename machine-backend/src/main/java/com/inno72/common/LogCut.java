package com.inno72.common;

import java.util.Enumeration;

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

	private void preHandle() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		logger.info("使用{}方式请求地址{}", request.getMethod().toUpperCase(), request.getServletPath());
		String token = request.getHeader("lf-None-Matoh") == null ? "无" : request.getHeader("lf-None-Matoh");
		CustomerHttpServletRequestWrapper req = new CustomerHttpServletRequestWrapper(request);
		String jsonStr = new String(req.getRequestBody());
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
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		logger.info("请求地址{}返回值为：{}", request.getServletPath(), JSON.toJSONString(retVal));
	}

}
