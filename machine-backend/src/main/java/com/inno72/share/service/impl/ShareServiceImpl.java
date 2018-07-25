package com.inno72.share.service.impl;


import com.inno72.common.AbstractService;
import com.inno72.common.Result;
import com.inno72.common.Results;
import com.inno72.common.UploadUtil;
import com.inno72.share.model.Inno72AdminArea;
import com.inno72.share.service.ShareService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


/**
 * Created by CodeGenerator on 2018/06/29.
 */
@Service
@Transactional
public class ShareServiceImpl extends AbstractService<Inno72AdminArea> implements ShareService {
   
	private static Logger logger = LoggerFactory.getLogger(ShareServiceImpl.class);
	@Override
	public Result<String> uploadImage(MultipartFile file,String type) {
		if ( file.getSize() > 0) {
			//调用上传图片
			return UploadUtil.uploadImage(file,type);
		}else{
			logger.info("[out-uploadImg]-空");
			return Results.failure("图片为空！");
		}

	}

}
