package com.inno72.project.service;

import com.inno72.common.Result;
import com.inno72.common.Service;
import com.inno72.project.model.Inno72ActivityIndex;
import com.inno72.project.vo.Inno72ActivityIndexVo;


/**
 * Created by CodeGenerator on 2019/01/11.
 */
public interface Inno72ActivityIndexService extends Service<Inno72ActivityIndex> {

	Result<Inno72ActivityIndexVo> activityInfo(String merchantId, String activityId);

	Result<String> saveIndex(String index);
}
