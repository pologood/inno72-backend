package com.inno72.Interact.service;

import java.util.List;

import com.inno72.Interact.model.Inno72InteractGameRule;
import com.inno72.common.Service;

/**
 * Created by CodeGenerator on 2018/11/27.
 */
public interface InteractGameRuleService extends Service<Inno72InteractGameRule> {

	List<Inno72InteractGameRule> getGameRuleList(String interactId);

}
