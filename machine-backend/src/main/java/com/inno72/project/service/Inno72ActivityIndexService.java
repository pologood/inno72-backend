package com.inno72.project.service;

import com.inno72.common.Result;
import com.inno72.common.Service;
import com.inno72.project.model.Inno72ActivityIndex;
import com.inno72.project.vo.Inno72ActivityIndexVo;


/**
 * Created by CodeGenerator on 2019/01/11.
 */
public interface Inno72ActivityIndexService extends Service<Inno72ActivityIndex> {

	/**
	 * @param merchantId 商户编码
	 * @param activityId 活动ID
	 * @return 商户活动配置的核心指标和已添加的操作日志
	 */
	Result<Inno72ActivityIndexVo> activityInfo(String merchantId, String activityId);

	/**
	 *
	 * @param index 核心指标json
	 * @return 操作结果
	 */
	Result<String> saveIndex(String index);
}
