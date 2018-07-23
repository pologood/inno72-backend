package com.inno72.app.controller;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.inno72.app.service.AppLogService;
import com.inno72.app.service.UploadService;
import com.inno72.common.CommonConstants;
import com.inno72.common.Result;
import com.inno72.common.UploadUtil;

@RestController
@RequestMapping("/upload")
@CrossOrigin
public class UploadController {
	Logger logger = LoggerFactory.getLogger(this.getClass());

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
	public Result<String> upload(@RequestParam(value = "file", required = false) MultipartFile file,
			HttpServletRequest req) {

		String fileName = file.getOriginalFilename();
		logger.info("上传文件，文件名：{}", fileName);
		String prefix = fileName.substring(fileName.lastIndexOf(".") + 1);
		String bashPath = "zip".equals(prefix) ? CommonConstants.OSS_LOG_PATH : CommonConstants.OSS_IMG_PATH;
		Result<String> result = UploadUtil.uploadImage(file, prefix, bashPath);
		logger.info("上传结果:{}", JSON.toJSONString(result));
		return result;
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

}