package com.inno72.Interact.service.impl;

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

import com.inno72.Interact.mapper.Inno72InteractMapper;
import com.inno72.Interact.model.Inno72Interact;
import com.inno72.Interact.service.InteractService;
import com.inno72.Interact.vo.InteractListVo;
import com.inno72.common.AbstractService;
import com.inno72.common.Result;
import com.inno72.common.Results;
import com.inno72.common.SessionData;
import com.inno72.common.SessionUtil;
import com.inno72.common.StringUtil;
import com.inno72.system.model.Inno72User;

/**
 * Created by CodeGenerator on 2018/09/19.
 */
@Service
@Transactional
public class InteractServiceImpl extends AbstractService<Inno72Interact> implements InteractService {
	private static Logger logger = LoggerFactory.getLogger(InteractServiceImpl.class);
	@Resource
	private Inno72InteractMapper inno72InteractMapper;

	@Override
	public List<InteractListVo> findByPage(String keyword, Integer status) {
		logger.info("---------------------活动分页列表查询-------------------");
		Map<String, Object> params = new HashMap<String, Object>();
		keyword = Optional.ofNullable(keyword).map(a -> a.replace("'", "")).orElse(keyword);
		params.put("keyword", keyword);
		params.put("status", status);

		return inno72InteractMapper.selectByPage(params);
	}

	@Override
	public Result<Object> save(Inno72Interact model, Integer type) {

		try {
			SessionData session = SessionUtil.sessionData.get();
			Inno72User mUser = Optional.ofNullable(session).map(SessionData::getUser).orElse(null);
			if (mUser == null) {
				logger.info("登陆用户为空");
				return Results.failure("未找到用户登录信息");
			}
			String mUserId = Optional.ofNullable(mUser).map(Inno72User::getId).orElse(null);
			model.setId(StringUtil.getUUID());
			model.setCreateId(mUserId);
			model.setUpdateId(mUserId);
			model.setCreateTime(LocalDateTime.now());
			model.setUpdateTime(LocalDateTime.now());

			if (type == 1) {
				// 下一步
				if (StringUtil.isBlank(model.getName())) {
					logger.info("请填写互派名称");
					return Results.failure("请填写互派名称");
				}

				if (StringUtil.isBlank(model.getGameId())) {
					logger.info("请选择游戏");
					return Results.failure("请选择游戏");
				}
				if (StringUtil.isBlank(model.getManager())) {
					logger.info("请填写负责人");
					return Results.failure("请填写负责人");
				}

			} else if (null == type) {
				logger.info("参数错误");
				return Results.failure("参数错误");
			}

			inno72InteractMapper.insert(model);

			return Results.warn("操作成功", 0, model.getId());

		} catch (Exception e) {
			e.printStackTrace();
			logger.info(e.getMessage());
			return Results.failure("操作失败");
		}
	}

	@Override
	public Result<Object> update(Inno72Interact model, Integer type) {

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

			if (type == 1) {
				// 下一步
				if (StringUtil.isBlank(model.getName())) {
					logger.info("请填写互派名称");
					return Results.failure("请填写互派名称");
				}

				if (StringUtil.isBlank(model.getGameId())) {
					logger.info("请选择游戏");
					return Results.failure("请选择游戏");
				}
				if (StringUtil.isBlank(model.getManager())) {
					logger.info("请填写负责人");
					return Results.failure("请填写负责人");
				}

			} else if (null == type) {
				logger.info("参数错误");
				return Results.failure("参数错误");
			}

			inno72InteractMapper.updateByPrimaryKey(model);

			return Results.warn("操作成功", 0, model.getId());

		} catch (Exception e) {
			e.printStackTrace();
			logger.info(e.getMessage());
			return Results.failure("操作失败");
		}
	}

}
