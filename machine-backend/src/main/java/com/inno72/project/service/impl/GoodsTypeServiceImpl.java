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
import com.inno72.redis.IRedisUtil;
import com.inno72.system.model.Inno72User;

import tk.mybatis.mapper.entity.Condition;

/**
 * Created by CodeGenerator on 2018/12/29.
 */
@Service
@Transactional
public class GoodsTypeServiceImpl extends AbstractService<Inno72GoodsType> implements GoodsTypeService {
	private static Logger logger = LoggerFactory.getLogger(GoodsTypeServiceImpl.class);

	/** 计划ID key **/
	public static final String LEVEL1_KEY = "goodsype_leve1_num";
	public static final String LEVEL2_KEY = "goodsype_leve2_";
	@Resource
	private IRedisUtil redisUtil;
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
		return inno72GoodsTypeMapper.selectList(null);

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

			if (StringUtil.isNotBlank(model.getName())) {
				Inno72GoodsType gt = new Inno72GoodsType();
				gt.setName(model.getName());
				List<Inno72GoodsType> gnList = inno72GoodsTypeMapper.select(gt);
				if (null != gnList && gnList.size() > 0) {
					logger.info("类目名称已存在");
					return Results.failure("类目名称已存在");
				}
			}
			StringBuffer code = new StringBuffer();
			if (StringUtil.isBlank(model.getParentCode())) {
				code.append("10");
				code.append(redisUtil.incr(LEVEL1_KEY).toString());
				model.setLevel(1);
			} else {
				code.append(model.getParentCode());
				code.append("00");
				code.append(redisUtil.incr(LEVEL2_KEY + model.getParentCode()).toString());
				Inno72GoodsType parent = inno72GoodsTypeMapper.selectByPrimaryKey(model.getParentCode());
				model.setLevel(2);
				model.setParentName(parent.getName());
			}

			model.setCode(code.toString());
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
			if (StringUtil.isNotBlank(model.getName())) {
				Inno72GoodsType gt = new Inno72GoodsType();
				gt.setName(model.getName());
				List<Inno72GoodsType> gnList = inno72GoodsTypeMapper.select(gt);
				gnList.remove(base);
				if (null != gnList && gnList.size() > 0) {
					logger.info("类目名称已存在");
					return Results.failure("类目名称已存在");
				}

			}
			if (base.getLevel() == 2) {
				Inno72GoodsType parent = inno72GoodsTypeMapper.selectByPrimaryKey(base.getParentCode());
				base.setParentName(parent.getName());
			} else {
				Inno72GoodsType parent = new Inno72GoodsType();
				parent.setParentCode(base.getCode());
				List<Inno72GoodsType> childList = inno72GoodsTypeMapper.select(parent);
				for (Inno72GoodsType inno72GoodsType : childList) {
					inno72GoodsType.setParentName(model.getName());
					super.update(inno72GoodsType);
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
