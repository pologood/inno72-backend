package com.inno72.share.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inno72.common.AbstractService;
import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;
import com.inno72.common.Results;
import com.inno72.common.StringUtil;
import com.inno72.share.mapper.Inno72AdminAreaMapper;
import com.inno72.share.model.Inno72AdminArea;
import com.inno72.share.service.AdminAreaService;

import tk.mybatis.mapper.entity.Condition;

/**
 * Created by CodeGenerator on 2018/06/29.
 */
@Service
@Transactional
public class AdminAreaServiceImpl extends AbstractService<Inno72AdminArea> implements AdminAreaService {
	@Resource
	private Inno72AdminAreaMapper inno72AdminAreaMapper;

	@Override
	public List<Inno72AdminArea> getLiset(String code) {
		Condition condition = new Condition(Inno72AdminArea.class);
		if (StringUtil.isEmpty(code)) {
			condition.createCriteria().andCondition("level = 1");
		} else {
			condition.createCriteria().andEqualTo("parentCode", code).andNotEqualTo("level", 4);
		}
		return inno72AdminAreaMapper.selectByCondition(condition);
	}

	@Override
	public Result<String> saveArea(Inno72AdminArea adminArea) {
		String parentCode = adminArea.getParentCode();
		String name = adminArea.getName();
		if (StringUtil.isEmpty(parentCode) || StringUtil.isEmpty(name)) {
			return Results.failure("参数有误");
		}
		List<Inno72AdminArea> areaList = inno72AdminAreaMapper.select(adminArea);
		if (areaList != null && areaList.size() > 0) {
			return Results.failure("请勿重复添加");
		}
		Inno72AdminArea area = inno72AdminAreaMapper.selectMaxByParentCode(parentCode);
		String before = parentCode.substring(0, 4);
		Integer maxMiddle = Integer.parseInt(area.getCode().substring(4, 6));
		String last = area.getCode().substring(6, 9);
		Integer middle = maxMiddle + 1;
		String code = null;
		String supply = "0";
		if (middle < 10) {
			code = before + supply + middle + last;
		} else {
			code = before + middle + last;
		}
		adminArea.setLevel(3);
		adminArea.setCode(code);
		adminArea.setProvince(area.getProvince());
		adminArea.setCity(area.getCity());
		adminArea.setDistrict(name);
		inno72AdminAreaMapper.insertSelective(adminArea);
		return ResultGenerator.genSuccessResult();
	}

	@Override
	public List<Inno72AdminArea> findByPage(String code) {
		int num = StringUtil.getAreaCodeNum(code);
		Map<String, Object> map = new HashMap<>();
		map.put("code", code);
		map.put("num", num);
		List<Inno72AdminArea> list = inno72AdminAreaMapper.findByPage(map);
		return list;
	}

	@Override
	public Result<String> updateArea(Inno72AdminArea adminArea) {
		String code = adminArea.getCode();
		String name = adminArea.getName();
		if (StringUtil.isEmpty(code) || StringUtil.isEmpty(name)) {
			return Results.failure("参数有误");
		}
		Map<String, Object> map = new HashMap<>();
		map.put("code", code);
		map.put("name", name);
		List<Inno72AdminArea> adminAreaList = inno72AdminAreaMapper.findByParam(map);
		if (adminAreaList != null && adminAreaList.size() > 0) {
			return Results.failure("已存在此区域");
		} else {
			Inno72AdminArea area = new Inno72AdminArea();
			area.setCode(code);
			area.setName(name);
			area.setDistrict(name);
			inno72AdminAreaMapper.updateByPrimaryKeySelective(area);
			return ResultGenerator.genSuccessResult();
		}

	}

}
