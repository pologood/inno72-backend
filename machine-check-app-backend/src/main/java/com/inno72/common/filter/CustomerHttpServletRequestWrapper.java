package com.inno72.common.filter;

import com.inno72.common.AesUtils;
import com.inno72.common.StringUtil;
import com.inno72.utils.page.Pagination;
import net.sf.json.JSONObject;
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
			if(StringUtil.isNotEmpty(encryptRequestBody)){
				String message = AesUtils.decrypt(encryptRequestBody);
				logger.info("请求{}接口参数为{}", request.getRequestURI(), new String(message));
				requestBody = message.getBytes();
				String data = new String(requestBody);
				if(StringUtil.isNotEmpty(data)){
					JSONObject t=JSONObject.fromObject(data);//转化成json对象
					Object pageNoObj = t.get("pageNo");

					if(pageNoObj != null){
						Integer pageNo = Integer.parseInt(pageNoObj.toString());
						if(pageNo != null){
							if(pageNo<1){
								pageNo = 1;
							}
							Pagination pagination = new Pagination();
							pagination.setPageNo(pageNo);
							Pagination.threadLocal.set(pagination);
						}
					}
				}
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
