package com.inno72.common.filter;

import com.inno72.common.AesUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StreamUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class CustomerHttpServletRequestWrapper extends HttpServletRequestWrapper {

	private byte[] requestBody = null;
	Logger logger = LoggerFactory.getLogger(CustomerHttpServletRequestWrapper.class);

	public CustomerHttpServletRequestWrapper(HttpServletRequest request) {
		super(request);
		try {
			byte[] encryptRequestBodyBytes = StreamUtils.copyToByteArray(request.getInputStream());
			String encryptRequestBody = new String(encryptRequestBodyBytes);
			String message = AesUtils.decrypt(encryptRequestBody);
			logger.info("请求{}接口参数为{}", request.getRequestURI(), new String(message));
			requestBody = message.getBytes();
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
				return false;
			}

			@Override
			public boolean isReady() {
				return false;
			}

			@Override
			public void setReadListener(ReadListener readListener) {
			}
		};
	}

	@Override
	public BufferedReader getReader() throws IOException {
		return new BufferedReader(new InputStreamReader(getInputStream()));
	}

}
