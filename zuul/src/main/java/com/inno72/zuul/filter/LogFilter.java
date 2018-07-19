package com.inno72.zuul.filter;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

public class LogFilter extends ZuulFilter {

	Logger logger = LoggerFactory.getLogger(LogFilter.class);

	@Override
	public Object run() {
		RequestContext ctx = RequestContext.getCurrentContext();
		ctx.setDebugRequest(true);
		HttpServletRequest request = ctx.getRequest();
		String uri = request.getRequestURI();
		logger.info("*************************************** 请求接口: {} ***************************************", uri);
		request.getParameterMap().forEach((k, v) -> {
			logger.info("Parameter参数 : {}, 值 : {}", k, v);
		});
		if (logger.isDebugEnabled()) {
			String name = null;
			Enumeration<String> headerNames = request.getHeaderNames();
			if (headerNames.hasMoreElements()) {
				name = headerNames.nextElement();
				logger.debug("Header参数 : {}, 值 : {}", name, request.getHeader(name));
			}
		}
		logger.info("*************************************** 响应接口: {} ***************************************", uri);

		HttpServletResponse response = ctx.getResponse();
		String contentType = response.getContentType();
		logger.info("响应类型: {}", contentType);
		if (contentType.indexOf("application/json") != -1) {
			String responseBody = ctx.getResponseBody();
			logger.info("响应结果: {}", responseBody);
		}
		return null;
	}

	@Override
	public boolean shouldFilter() {
		return true;
	}

	@Override
	public int filterOrder() {
		return 999999;
	}

	@Override
	public String filterType() {
		return "post";
	}

}
