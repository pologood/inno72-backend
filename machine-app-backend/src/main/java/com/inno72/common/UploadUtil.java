package com.inno72.common;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.inno72.oss.OSSUtil;

public class UploadUtil {

	/**
	 * 上传图片
	 * 
	 * @param file
	 * @param type
	 * @return
	 */
	public static Result<String> uploadImage(MultipartFile file, String type, String basePath) {
		String name = StringUtil.getUUID() + "." + type;
		String path = basePath + "/" + name;
		try {
			OSSUtil.uploadByStream(file.getInputStream(), path);
			return Results.success(path);
		} catch (IOException e) {
			e.printStackTrace();
			return Results.failure("图片处理失败！");
		} catch (Exception e) {
			e.printStackTrace();
			return Results.failure("图片处理失败！");
		}
	}

}
