package com.inno72.common.log;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import com.inno72.common.StringUtil;

public class ProcessDataFilter extends OncePerRequestFilter {
	Logger logger = LoggerFactory.getLogger(ProcessDataFilter.class);

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		CustomerHttpServletRequestWrapper req = new CustomerHttpServletRequestWrapper(request);
		CustomerServletResponseWrapper resp = new CustomerServletResponseWrapper(response);
		logger.info("使用{}方式请求地址{}", request.getMethod().toUpperCase(), request.getServletPath());
		String token = request.getHeader("lf-None-Matoh") == null ? "无" : request.getHeader("lf-None-Matoh");
		String jsonStr = req.getParamString();
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
			logger.info("请求token：{},param参数：{}", token, parm.toString());
		}
		super.doFilter(req, resp, filterChain);
	}

}
