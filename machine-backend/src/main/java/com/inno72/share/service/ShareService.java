package com.inno72.share.service;

import org.springframework.web.multipart.MultipartFile;

import com.inno72.common.Result;
import com.inno72.common.Service;
import com.inno72.share.model.Inno72AdminArea;


/**
 * Created by CodeGenerator on 2018/06/29.
 */
public interface ShareService extends Service<Inno72AdminArea> {

	Result<String> uploadImage(MultipartFile file, String type);
	

}
