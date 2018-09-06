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
import com.inno72.project.mapper.Inno72MerchantMapper;
import com.inno72.project.model.Inno72Merchant;
import com.inno72.project.service.MerchantService;
import com.inno72.project.vo.Inno72MerchantVo;
import com.inno72.system.model.Inno72User;

import tk.mybatis.mapper.entity.Condition;

/**
 * Created by CodeGenerator on 2018/06/29.
 */
@Service
@Transactional
public class MerchantServiceImpl extends AbstractService<Inno72Merchant> implements MerchantService {
	private static Logger logger = LoggerFactory.getLogger(MerchantServiceImpl.class);

	@Resource
	private Inno72MerchantMapper inno72MerchantMapper;

	@Override
	public Result<String> saveModel(Inno72Merchant model) {
		logger.info("---------------------商户新增-------------------");
		try {
			SessionData session = SessionUtil.sessionData.get();
			Inno72User mUser = Optional.ofNullable(session).map(SessionData::getUser).orElse(null);
			if (mUser == null) {
				logger.info("登陆用户为空");
				return Results.failure("未找到用户登录信息");
			}

			int n = inno72MerchantMapper.getCount(model.getMerchantCode());
			if (n > 0) {
				return Results.failure("商户编码已存在，请确认！");
			}
			String userId = Optional.ofNullable(mUser).map(Inno72User::getId).orElse(null);

			model.setId(StringUtil.getUUID());
			model.setCreateId(userId);
			model.setUpdateId(userId);

			super.save(model);
		} catch (Exception e) {
			logger.info(e.getMessage());
			return Results.failure("操作失败");
		}
		return Results.success("操作成功");
	}

	@Override
	public Result<String> delById(String id) {
		logger.info("--------------------商户删除-------------------");
		SessionData session = SessionUtil.sessionData.get();
		Inno72User mUser = Optional.ofNullable(session).map(SessionData::getUser).orElse(null);
		if (mUser == null) {
			logger.info("登陆用户为空");
			return Results.failure("未找到用户登录信息");
		}

		int n = inno72MerchantMapper.selectIsUseing(id);
		if (n > 0) {
			return Results.failure("店铺使用中，不能删除！");
		}
		String userId = Optional.ofNullable(mUser).map(Inno72User::getId).orElse(null);
		Inno72Merchant model = inno72MerchantMapper.selectByPrimaryKey(id);
		model.setIsDelete(1);// 0正常,1结束
		model.setUpdateId(userId);
		model.setUpdateTime(LocalDateTime.now());

		super.update(model);
		return Results.success("操作成功");
	}

	@Override
	public Result<String> updateModel(Inno72Merchant model) {
		logger.info("---------------------商户更新-------------------");
		try {
			SessionData session = SessionUtil.sessionData.get();
			Inno72User mUser = Optional.ofNullable(session).map(SessionData::getUser).orElse(null);
			if (mUser == null) {
				logger.info("登陆用户为空");
				return Results.failure("未找到用户登录信息");
			}
			if (StringUtil.isEmpty(model.getId())) {
				return Results.failure("ID不能为空！");
			}
			if (StringUtil.isEmpty(model.getMerchantCode())) {
				return Results.failure("商户编码不能为空！");
			}
			if (StringUtil.isNotBlank(model.getMerchantCode())) {
				Inno72Merchant m = inno72MerchantMapper.selectByPrimaryKey(model.getId());
				int n = inno72MerchantMapper.getCount(model.getMerchantCode());
				if (n > 0 && !m.getMerchantCode().equals(model.getMerchantCode())) {
					return Results.failure("商户编码已存在，请确认！");
				}
			}

			String userId = Optional.ofNullable(mUser).map(Inno72User::getId).orElse(null);

			model.setUpdateId(userId);
			model.setUpdateTime(LocalDateTime.now());

			super.update(model);
		} catch (Exception e) {
			logger.info(e.getMessage());
			return Results.failure("操作失败");
		}

		return Results.success("操作成功");
	}

	@Override
	public List<Inno72MerchantVo> findByPage(String code, String keyword) {
		logger.info("---------------------商户分页列表查询-------------------");
		Map<String, Object> params = new HashMap<String, Object>();
		keyword = Optional.ofNullable(keyword).map(a -> a.replace("'", "")).orElse(keyword);
		params.put("keyword", keyword);
		params.put("code", code);

		return inno72MerchantMapper.selectByPage(params);
	}

	@Override
	public List<Inno72Merchant> getList(Inno72Merchant model) {
		logger.info("---------------------获取商户列表-------------------");
		model.setIsDelete(0);
		Condition condition = new Condition(Inno72Merchant.class);
		condition.createCriteria().andEqualTo(model);
		return super.findByCondition(condition);
	}

}
