package com.inno72.Interact.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inno72.Interact.mapper.Inno72InteractMerchantMapper;
import com.inno72.Interact.model.Inno72InteractMerchant;
import com.inno72.Interact.service.InteractMerchantService;
import com.inno72.Interact.vo.InteractMerchantVo;
import com.inno72.common.AbstractService;
import com.inno72.common.Result;
import com.inno72.common.Results;
import com.inno72.common.SessionData;
import com.inno72.common.SessionUtil;
import com.inno72.common.StringUtil;
import com.inno72.project.mapper.Inno72MerchantMapper;
import com.inno72.project.model.Inno72Merchant;
import com.inno72.system.model.Inno72User;

/**
 * Created by CodeGenerator on 2018/09/19.
 */
@Service
@Transactional
public class InteractMerchantServiceImpl extends AbstractService<Inno72InteractMerchant>
		implements InteractMerchantService {

	private static Logger logger = LoggerFactory.getLogger(InteractMerchantServiceImpl.class);
	@Resource
	private Inno72InteractMerchantMapper inno72InteractMerchantMapper;
	@Resource
	private Inno72MerchantMapper inno72MerchantMapper;

	@Override
	public Result<String> save(InteractMerchantVo model) {

		try {
			SessionData session = SessionUtil.sessionData.get();
			Inno72User mUser = Optional.ofNullable(session).map(SessionData::getUser).orElse(null);
			if (mUser == null) {
				logger.info("登陆用户为空");
				return Results.failure("未找到用户登录信息");
			}
			String mUserId = Optional.ofNullable(mUser).map(Inno72User::getId).orElse(null);
			model.setId(StringUtil.getUUID());
			model.setIsDelete(0);
			model.setCreateId(mUserId);
			model.setUpdateId(mUserId);
			model.setCreateTime(LocalDateTime.now());
			model.setUpdateTime(LocalDateTime.now());
			// 数据校验
			if (StringUtil.isBlank(model.getChannelId())) {
				logger.info("请选择渠道");
				return Results.failure("请选择渠道");
			}
			if (StringUtil.isBlank(model.getMerchantCode())) {
				logger.info("请填写商家编号");
				return Results.failure("请填写商家编号");
			}
			if (StringUtil.isBlank(model.getMerchantName())) {
				logger.info("请填写商家名称");
				return Results.failure("请填写商家名称");
			}

			Inno72InteractMerchant interactMerchant = new Inno72InteractMerchant();
			interactMerchant.setId(StringUtil.getUUID());
			interactMerchant.setInteractId(model.getInteractId());
			interactMerchant.setMerchantId(model.getId());

			inno72InteractMerchantMapper.insert(interactMerchant);
			inno72MerchantMapper.insert(model);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(e.getMessage());
			return Results.failure("操作失败");
		}
		return Results.success("操作成功");
	}

	@Override
	public Result<String> update(InteractMerchantVo model) {

		try {
			SessionData session = SessionUtil.sessionData.get();
			Inno72User mUser = Optional.ofNullable(session).map(SessionData::getUser).orElse(null);
			if (mUser == null) {
				logger.info("登陆用户为空");
				return Results.failure("未找到用户登录信息");
			}
			String mUserId = Optional.ofNullable(mUser).map(Inno72User::getId).orElse(null);
			model.setUpdateId(mUserId);
			model.setUpdateTime(LocalDateTime.now());
			// 数据校验
			if (StringUtil.isBlank(model.getChannelId())) {
				logger.info("请选择渠道");
				return Results.failure("请选择渠道");
			}
			if (StringUtil.isBlank(model.getMerchantCode())) {
				logger.info("请填写商家编号");
				return Results.failure("请填写商家编号");
			}
			if (StringUtil.isBlank(model.getMerchantName())) {
				logger.info("请填写商家名称");
				return Results.failure("请填写商家名称");
			}

			inno72MerchantMapper.updateByPrimaryKeySelective(model);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(e.getMessage());
			return Results.failure("操作失败");
		}
		return Results.success("操作成功");
	}

	@Override
	public List<InteractMerchantVo> getList(String interactId) {
		logger.info("---------------------获取活动下商户列表-------------------");
		return inno72InteractMerchantMapper.selectMerchantByInteractId(interactId);

	}

	@Override
	public Inno72Merchant findMerchantsById(String id) {
		return inno72MerchantMapper.selectByPrimaryKey(id);
	}

}
