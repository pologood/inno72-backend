package com.inno72.system.service;

import java.util.List;

import com.inno72.common.Result;
import com.inno72.common.Service;
import com.inno72.share.model.Inno72AdminArea;
import com.inno72.system.model.Inno72UserFunctionArea;
import com.inno72.system.vo.AreaTreeResultVo;

/**
 * Created by CodeGenerator on 2018/09/03.
 */
public interface UserFunctionAreaService extends Service<Inno72UserFunctionArea> {

	Result<AreaTreeResultVo> findAllAreaTree(String userId);

	Result<String> updateFunctionArea(String userId, List<Inno72AdminArea> areas);

}
