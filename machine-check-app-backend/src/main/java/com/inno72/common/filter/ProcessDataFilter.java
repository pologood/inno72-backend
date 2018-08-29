package com.inno72.common.filter;

import com.inno72.common.AesUtils;
import com.inno72.common.StringUtil;
import com.inno72.utils.page.Pagination;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class ProcessDataFilter extends OncePerRequestFilter {
	private Logger logger = LoggerFactory.getLogger(ProcessDataFilter.class);
	private static List<String> doNotCheckUs = Arrays
			.asList("/check/user/upload","/check/user/encrypt","/check/user/decrypt",
					"/check/fault/upload","/machine/channel/merge","/machine/channel/split",
                    "/machine/channel/findAndPushByTaskParam","/machine/channel/findLockGoodsPush"
					);

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String url = request.getServletPath();
		boolean match = doNotCheckUs.parallelStream().anyMatch(url::contains);
		Pagination.threadLocal.remove();
		if (!match && !url.equals("/") && !url.contains("/h5")) {
			CustomerHttpServletRequestWrapper req = new CustomerHttpServletRequestWrapper(request);
			CustomerServletResponseWrapper resp = new CustomerServletResponseWrapper(response);
			super.doFilter(req, resp, filterChain);
			byte[] bytes = resp.getBytes();
			String returnData = new String(bytes, "utf-8");
			logger.info("请求{}接口返回值为{}", request.getRequestURI(), returnData);
			String msg = AesUtils.encrypt(returnData);
			response.setContentType("text/html; charset=utf-8");
			response.getWriter().print(msg);
		} else {
			super.doFilter(request, response, filterChain);
		}
	}

}
