package com.inno72.system.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inno72.common.AbstractService;
import com.inno72.common.Result;
import com.inno72.common.Results;
import com.inno72.common.utils.StringUtil;
import com.inno72.share.mapper.Inno72AdminAreaMapper;
import com.inno72.share.model.Inno72AdminArea;
import com.inno72.system.mapper.Inno72UserFunctionAreaMapper;
import com.inno72.system.model.Inno72UserFunctionArea;
import com.inno72.system.service.UserFunctionAreaService;
import com.inno72.system.vo.AreaTreeResultVo;
import com.inno72.system.vo.AreaTreeVo;

import tk.mybatis.mapper.entity.Condition;

/**
 * Created by CodeGenerator on 2018/09/03.
 */
@Service
@Transactional
public class UserFunctionAreaServiceImpl extends AbstractService<Inno72UserFunctionArea>
		implements UserFunctionAreaService {
	@Resource
	private Inno72UserFunctionAreaMapper inno72UserFunctionAreaMapper;
	@Resource
	private Inno72AdminAreaMapper inno72AdminAreaMapper;

	@Override
	public Result<AreaTreeResultVo> findAllAreaTree(String userId) {
		AreaTreeResultVo areaResult = new AreaTreeResultVo();
		if (!StringUtil.isEmpty(userId)) {
			Condition condition1 = new Condition(Inno72UserFunctionArea.class);
			condition1.createCriteria().andEqualTo("userId", userId);
			List<Inno72UserFunctionArea> userFunctionArea = inno72UserFunctionAreaMapper.selectByCondition(condition1);
			List<String> areaCodes = new ArrayList<>();
			for (Inno72UserFunctionArea area : userFunctionArea) {
				areaCodes.add(area.getAreaCode());
			}
			areaResult.setAreaCodes(areaCodes);
		}
		AreaTreeVo areaTree = new AreaTreeVo();
		areaTree.setName("");
		areaTree.setCode("");
		Condition condition = new Condition(Inno72AdminArea.class);
		condition.createCriteria().andEqualTo("level", 1);
		List<Inno72AdminArea> first = inno72AdminAreaMapper.selectByCondition(condition);
		List<AreaTreeVo> firstVo = new ArrayList<>();
		for (Inno72AdminArea areaFirst : first) {
			AreaTreeVo areaFirstVo = new AreaTreeVo();
			areaFirstVo.setCode(areaFirst.getCode());
			areaFirstVo.setName(areaFirst.getName());
			firstVo.add(areaFirstVo);
			condition = new Condition(Inno72AdminArea.class);
			condition.createCriteria().andEqualTo("level", 2).andEqualTo("parentCode", areaFirst.getCode());
			List<Inno72AdminArea> second = inno72AdminAreaMapper.selectByCondition(condition);
			List<AreaTreeVo> secondVo = new ArrayList<>();
			for (Inno72AdminArea areaSecond : second) {
				AreaTreeVo areaSecondVo = new AreaTreeVo();
				areaSecondVo.setCode(areaSecond.getCode());
				areaSecondVo.setName(areaSecond.getName());
				secondVo.add(areaSecondVo);

				condition = new Condition(Inno72AdminArea.class);
				condition.createCriteria().andEqualTo("level", 3).andEqualTo("parentCode", areaSecond.getCode());
				List<Inno72AdminArea> third = inno72AdminAreaMapper.selectByCondition(condition);
				List<AreaTreeVo> thirdVo = new ArrayList<>();
				for (Inno72AdminArea areaThird : third) {
					AreaTreeVo areaThirdVo = new AreaTreeVo();
					areaThirdVo.setCode(areaThird.getCode());
					areaThirdVo.setName(areaThird.getName());
					thirdVo.add(areaThirdVo);
				}
				areaSecondVo.setChildren(thirdVo);
			}
			areaFirstVo.setChildren(secondVo);
		}
		areaTree.setChildren(firstVo);
		areaResult.setAreaTree(areaTree);
		return Results.success(areaResult);
	}

}
