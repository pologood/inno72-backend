package com.inno72.Interact.service;

import java.util.List;

import com.inno72.Interact.model.Inno72Interact;
import com.inno72.Interact.vo.InteractListVo;
import com.inno72.Interact.vo.InteractRuleVo;
import com.inno72.common.Result;
import com.inno72.common.Service;

/**
 * Created by CodeGenerator on 2018/09/19.
 */
public interface InteractService extends Service<Inno72Interact> {

	List<InteractListVo> findByPage(String keyword, Integer status);

	Result<Object> save(Inno72Interact model, Integer type);

	Result<Object> update(Inno72Interact model, Integer type);

	Result<String> next(String interactId, String type);

	Result<Object> updateRule(InteractRuleVo interactRule);

}
