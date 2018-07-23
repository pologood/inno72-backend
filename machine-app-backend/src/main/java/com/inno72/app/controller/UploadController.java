package com.inno72.app.controller;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.inno72.app.service.AppLogService;
import com.inno72.app.service.UploadService;
import com.inno72.common.CommonConstants;
import com.inno72.common.Result;
import com.inno72.common.UploadUtil;

@RestController
@RequestMapping("/upload")
@CrossOrigin
public class UploadController {
	@Resource
	private UploadService uploadService;

	@Resource
	private AppLogService appLogService;

	/**
	 * 上传文件
	 * 
	 * @param file
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "/upload", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> uploadImage(@RequestParam(value = "upfile", required = false) MultipartFile file,
			HttpServletRequest req) {
		String fileName = file.getOriginalFilename();
		String prefix = fileName.substring(fileName.lastIndexOf(".") + 1);
		String bashPath = "log".equals(prefix) ? CommonConstants.OSS_LOG_PATH : CommonConstants.OSS_IMG_PATH;
		return UploadUtil.uploadImage(file, prefix, bashPath);
	}

	/**
	 * 发送日志信息
	 * 
	 * @param msg
	 * @return
	 */
	@RequestMapping(value = "/sendLogInfo", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> sendLogInfo(@RequestBody Map<String, Object> msg) {
		return appLogService.sendLogInfo(msg);
	}

	public static void main(String[] args) {
		testUploadImage();
	}

	public static void testUploadImage() {
		String url = "http://localhost:8884/upload/uploadImage";
		String fileName = "/Users/lzh/Logs/machine-backend/info.log";
		String fileName1 = "/Users/lzh/abc.jpeg";

		Map<String, String> fileMap = new HashMap<String, String>();
		fileMap.put("upfile", fileName);
		String a = formUpload(url, fileMap);
		System.out.println(a);
		fileMap = new HashMap<String, String>();
		fileMap.put("upfile", fileName1);
		String a1 = formUpload(url, fileMap);
		System.out.println(a1);
	}

	@SuppressWarnings("rawtypes")
	public static String formUpload(String urlStr, Map<String, String> fileMap) {
		String res = "";
		HttpURLConnection conn = null;
		// boundary就是request头和上传文件内容的分隔符
		String BOUNDARY = "---------------------------123821742118716";
		try {
			URL url = new URL(urlStr);
			conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5000);
			conn.setReadTimeout(30000);
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)");
			conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
			OutputStream out = new DataOutputStream(conn.getOutputStream());
			// file
			if (fileMap != null) {
				Iterator iter = fileMap.entrySet().iterator();
				while (iter.hasNext()) {
					Map.Entry entry = (Map.Entry) iter.next();
					String inputName = (String) entry.getKey();
					String inputValue = (String) entry.getValue();
					if (inputValue == null) {
						continue;
					}
					File file = new File(inputValue);
					String filename = file.getName();

					String contentType = "application/octet-stream";
					StringBuffer strBuf = new StringBuffer();
					strBuf.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
					strBuf.append("Content-Disposition: form-data; name=\"" + inputName + "\"; filename=\"" + filename
							+ "\"\r\n");
					System.out.println(inputName + "," + filename);

					strBuf.append("Content-Type:" + contentType + "\r\n\r\n");
					out.write(strBuf.toString().getBytes());
					DataInputStream in = new DataInputStream(new FileInputStream(file));
					int bytes = 0;
					byte[] bufferOut = new byte[1024];
					while ((bytes = in.read(bufferOut)) != -1) {
						out.write(bufferOut, 0, bytes);
					}
					in.close();
				}
			}
			byte[] endData = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();
			out.write(endData);
			out.flush();
			out.close();
			// 读取返回数据
			StringBuffer strBuf = new StringBuffer();
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line = null;
			while ((line = reader.readLine()) != null) {
				strBuf.append(line).append("\n");
			}
			res = strBuf.toString();
			reader.close();
			reader = null;
		} catch (Exception e) {
			System.out.println("发送POST请求出错。" + urlStr);
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.disconnect();
				conn = null;
			}
		}
		return res;
	}

}