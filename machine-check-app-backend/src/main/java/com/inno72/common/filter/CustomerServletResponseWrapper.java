package com.inno72.common.filter;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.*;

public class CustomerServletResponseWrapper extends HttpServletResponseWrapper {

	private ByteArrayOutputStream bytes = new ByteArrayOutputStream();
	private PrintWriter pwrite;

	CustomerServletResponseWrapper(HttpServletResponse response) {
		super(response);
	}

	@Override
	public ServletOutputStream getOutputStream() {
		return new MyServletOutputStream(bytes);
	}

	@Override
	public PrintWriter getWriter() {
		try {
			pwrite = new PrintWriter(new OutputStreamWriter(bytes, "utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return pwrite;
	}

	/**
	 * 获取缓存在 PrintWriter 中的响应数据
	 * 
	 */
	byte[] getBytes() {
		if (null != pwrite) {
			pwrite.close();
			return bytes.toByteArray();
		}

		if (null != bytes) {
			try {
				bytes.flush();
				return bytes.toByteArray();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	class MyServletOutputStream extends ServletOutputStream {
		private ByteArrayOutputStream ostream;

		MyServletOutputStream(ByteArrayOutputStream ostream) {
			this.ostream = ostream;
		}

		@Override
		public void write(int b) {
			ostream.write(b);
		}

		@Override
		public boolean isReady() {
			return false;
		}

		@Override
		public void setWriteListener(WriteListener writeListener) {

		}

	}

}
