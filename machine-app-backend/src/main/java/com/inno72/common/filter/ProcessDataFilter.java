package com.inno72.common.filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import com.inno72.util.AesUtils;

public class ProcessDataFilter extends OncePerRequestFilter {
	Logger logger = LoggerFactory.getLogger(ProcessDataFilter.class);
	private static List<String> doNotCheckUs = Arrays
			.asList(new String[] { "/machine/encrypt", "/machine/decrypt", "/upload/upload", "/push/pushMsg" });

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String url = request.getServletPath();
		boolean match = doNotCheckUs.parallelStream().anyMatch(_url -> url.indexOf(_url) != -1);
		if (!match) {
			CustomerHttpServletRequestWrapper req = new CustomerHttpServletRequestWrapper(request);
			CustomerServletResponseWrapper resp = new CustomerServletResponseWrapper(response);
			super.doFilter(req, resp, filterChain);
			byte[] bytes = resp.getBytes();
			String returnData = new String(bytes, "utf-8");
			logger.info("请求{}接口返回值为{}", request.getRequestURI(), returnData);
			String msg = AesUtils.encrypt(returnData);
			response.setContentType("text/html; charset=utf-8");
			response.getOutputStream().write(msg.getBytes());
		} else {
			super.doFilter(request, response, filterChain);
		}
	}

}
