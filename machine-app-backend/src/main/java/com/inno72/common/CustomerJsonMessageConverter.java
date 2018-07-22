package com.inno72.common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

public class CustomerJsonMessageConverter extends MappingJackson2HttpMessageConverter {

	// 数据读前
	@Override
	protected Object readInternal(Class<?> clazz, HttpInputMessage inputMessage)
			throws IOException, HttpMessageNotReadableException {
		super.readInternal(clazz, inputMessage);
		InputStream is = inputMessage.getBody();

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] b = new byte[4096];
		int n = 0;
		while ((n = is.read(b)) > 0) {
			out.write(b, 0, n);
		}
		// 解密 解密
		String plainBody = "123";
		try {
			return this.objectMapper.readValue(new ByteArrayInputStream(plainBody.getBytes()), String.class);
			// return this.objectMapper.readValue(new
			// ByteArrayInputStream(plainBody.getBytes()), javaType);
		} catch (IOException ex) {
			throw new HttpMessageNotReadableException("Could not read document: " + ex.getMessage(), ex);
		}
	}

}