package com.inno72.project.service.impl;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inno72.common.AbstractService;
import com.inno72.common.Result;
import com.inno72.common.Results;
import com.inno72.common.SessionData;
import com.inno72.common.SessionUtil;
import com.inno72.common.StringUtil;
import com.inno72.project.mapper.Inno72GoodsTypeMapper;
import com.inno72.project.model.Inno72GoodsType;
import com.inno72.project.service.GoodsTypeService;
import com.inno72.system.model.Inno72User;

import tk.mybatis.mapper.entity.Condition;

/**
 * Created by CodeGenerator on 2018/12/29.
 */
@Service
@Transactional
public class GoodsTypeServiceImpl extends AbstractService<Inno72GoodsType> implements GoodsTypeService {
	private static Logger logger = LoggerFactory.getLogger(GoodsTypeServiceImpl.class);
	@Resource
	private Inno72GoodsTypeMapper inno72GoodsTypeMapper;

	@Override
	public List<Inno72GoodsType> findByPage(String code, String keyword) {
		Map<String, Object> params = new HashMap<String, Object>();
		keyword = Optional.ofNullable(keyword).map(a -> a.replace("'", "")).orElse(keyword);
		params.put("keyword", keyword);
		if (StringUtil.isEmpty(code)) {
			params.put("level", 1);
		} else {
			params.put("parentCode", code);
		}

		return inno72GoodsTypeMapper.selectByPage(params);

	}

	@Override
	public List<Inno72GoodsType> getList(String code, String keyword) {
		Condition condition = new Condition(Inno72GoodsType.class);
		if (StringUtil.isEmpty(code)) {
			condition.createCriteria().andCondition("level = 1").andLike("name", keyword);
		} else {
			condition.createCriteria().andEqualTo("parentCode", code).andLike("name", keyword);
		}
		return inno72GoodsTypeMapper.selectByConditionByPage(condition);

	}

	@Override
	public Result<String> saveModel(Inno72GoodsType model) {
		logger.info("----------------商品类目添加--------------");
		try {
			SessionData session = SessionUtil.sessionData.get();
			Inno72User mUser = Optional.ofNullable(session).map(SessionData::getUser).orElse(null);
			if (mUser == null) {
				logger.info("登陆用户为空");
				return Results.failure("未找到用户登录信息");
			}

			String userId = Optional.ofNullable(mUser).map(Inno72User::getId).orElse(null);
			String code = "";
			if (StringUtil.isBlank(model.getParentCode())) {
				code = StringUtil.createRandomCode(4);
				model.setLevel(1);
			} else {
				code = model.getParentCode() + "00" + StringUtil.createRandomCode(4);
				Inno72GoodsType parent = inno72GoodsTypeMapper.selectByPrimaryKey(model.getParentCode());
				model.setLevel(2);
				model.setParentName(parent.getName());
			}

			model.setCode(code);
			model.setCreateId(userId);
			model.setUpdateId(userId);
			model.setCreateTime(LocalDateTime.now());
			model.setUpdateTime(LocalDateTime.now());

			super.save(model);
		} catch (Exception e) {
			logger.info(e.getMessage());
			return Results.failure("操作失败");
		}

		return Results.success("操作成功");
	}

	@Override
	public Result<String> updateModel(Inno72GoodsType model) {
		logger.info("----------------商品类目编辑--------------");
		try {
			SessionData session = SessionUtil.sessionData.get();
			Inno72User mUser = Optional.ofNullable(session).map(SessionData::getUser).orElse(null);
			if (mUser == null) {
				logger.info("登陆用户为空");
				return Results.failure("未找到用户登录信息");
			}
			String userId = Optional.ofNullable(mUser).map(Inno72User::getId).orElse(null);
			Inno72GoodsType base = inno72GoodsTypeMapper.selectByPrimaryKey(model.getCode());
			if (base.getLevel() == 2) {
				Inno72GoodsType parent = inno72GoodsTypeMapper.selectByPrimaryKey(base.getParentCode());
				base.setParentName(parent.getName());
			} else {
				Inno72GoodsType parent = new Inno72GoodsType();
				parent.setParentCode(model.getCode());
				List<Inno72GoodsType> childList = inno72GoodsTypeMapper.selectByCondition(parent);
				for (Inno72GoodsType inno72GoodsType : childList) {
					inno72GoodsType.setParentName(model.getName());
				}
			}
			base.setName(model.getName());
			base.setUpdateId(userId);
			base.setUpdateTime(LocalDateTime.now());

			super.update(model);
		} catch (Exception e) {
			logger.info(e.getMessage());
			return Results.failure("操作失败");
		}

		return Results.success("操作成功");
	}

}
