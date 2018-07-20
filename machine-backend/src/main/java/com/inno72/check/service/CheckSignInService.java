package com.inno72.check.service;
import java.util.List;

import com.inno72.check.model.Inno72CheckSignIn;
import com.inno72.check.vo.Inno72CheckUserVo;
import com.inno72.common.Service;


/**
 * Created by CodeGenerator on 2018/07/20.
 */
public interface CheckSignInService extends Service<Inno72CheckSignIn> {

	List<Inno72CheckUserVo> findByPage(String code, String keyword);

}
