package com.inno72.common.filter;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StreamUtils;

import com.inno72.common.StringUtil;
import com.inno72.util.AesUtils;

public class CustomerHttpServletRequestWrapper extends HttpServletRequestWrapper {

	private byte[] requestBody = null;
	Logger logger = LoggerFactory.getLogger(CustomerHttpServletRequestWrapper.class);

	public CustomerHttpServletRequestWrapper(HttpServletRequest request) {
		super(request);
		try {
			byte[] encryptRequestBodyBytes = StreamUtils.copyToByteArray(request.getInputStream());
			String encryptRequestBody = new String(encryptRequestBodyBytes);
			logger.info("请求{}接口加密参数为{}", request.getRequestURI(), encryptRequestBody);
			if (StringUtil.isEmpty(encryptRequestBody)) {
				requestBody = encryptRequestBodyBytes;
			} else {
				String message = AesUtils.decrypt(encryptRequestBody);
				logger.info("请求{}接口参数为{}", request.getRequestURI(), new String(message));
				requestBody = message.getBytes();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public ServletInputStream getInputStream() throws IOException {
		if (requestBody == null) {
			requestBody = new byte[0];
		}
		final ByteArrayInputStream bais = new ByteArrayInputStream(requestBody);
		return new ServletInputStream() {
			@Override
			public int read() throws IOException {
				return bais.read();
			}

			@Override
			public boolean isFinished() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean isReady() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public void setReadListener(ReadListener readListener) {
				// TODO Auto-generated method stub

			}

		};
	}

	@Override
	public BufferedReader getReader() throws IOException {
		return new BufferedReader(new InputStreamReader(getInputStream()));
	}

}
