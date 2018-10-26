package com.inno72.app.contoller;

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
import com.inno72.app.model.Inno72AppLog;
import com.inno72.app.service.AppLogService;
import com.inno72.common.CommonConstants;
import com.inno72.common.Result;
import com.inno72.common.UploadUtil;

@RestController
@RequestMapping("/upload")
@CrossOrigin
public class UploadController {
	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource
	private AppLogService appLogService;

	/**
	 * 上传文件
	 * 
	 * @param file
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "/add", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> add(@RequestParam(value = "file", required = false) MultipartFile file,
			HttpServletRequest req) {
		String fileName = file.getOriginalFilename();
		logger.info("上传文件，文件名：{}", fileName);
		String bashPath = "app/log";
		Result<String> result = UploadUtil.uploadImage(file,bashPath);
		logger.info("上传结果:{}", JSON.toJSONString(result));
		return result;
	}

	/**
	 * 发送日志信息
	 * 
	 * @param log
	 * @return
	 */
	@RequestMapping(value = "/sendLogInfo", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> sendLogInfo(@RequestBody Inno72AppLog log) {
		return appLogService.sendLogInfo(log);
	}

}