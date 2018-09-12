package com.inno72.project.service.impl;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inno72.common.AbstractService;
import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;
import com.inno72.common.Results;
import com.inno72.common.SessionData;
import com.inno72.common.SessionUtil;
import com.inno72.common.StringUtil;
import com.inno72.project.mapper.Inno72GameMapper;
import com.inno72.project.model.Inno72Game;
import com.inno72.project.model.Inno72Goods;
import com.inno72.project.service.GameService;
import com.inno72.project.vo.Inno72GameVo;
import com.inno72.system.model.Inno72User;

import tk.mybatis.mapper.entity.Condition;

/**
 * Created by CodeGenerator on 2018/06/29.
 */
@Service
@Transactional
public class GameServiceImpl extends AbstractService<Inno72Game> implements GameService {

	private static Logger logger = LoggerFactory.getLogger(GameServiceImpl.class);
	@Resource
	private Inno72GameMapper inno72GameMapper;

	Pattern patternNumbe = Pattern.compile("^[0-9]{1,8}$");

	@Override
	public Result<String> saveModel(Inno72Game model) {
		logger.info("----------------游戏添加--------------");
		try {
			SessionData session = SessionUtil.sessionData.get();
			Inno72User mUser = Optional.ofNullable(session).map(SessionData::getUser).orElse(null);
			if (mUser == null) {
				logger.info("登陆用户为空");
				return Results.failure("未找到用户登录信息");
			}
			String userId = Optional.ofNullable(mUser).map(Inno72User::getId).orElse(null);
			if (null != model.getMinGoodsNum() || null != model.getMaxGoodsNum()) {
				Matcher match = patternNumbe.matcher(model.getMinGoodsNum().toString());
				if (!match.matches()) {
					return Results.failure("数量最大8位整数");
				}
				Matcher match1 = patternNumbe.matcher(model.getMaxGoodsNum().toString());
				if (!match1.matches()) {
					return Results.failure("数量最大8位整数");
				}
			} else {

			}

			if (model.getMinGoodsNum() < 0) {
				return Results.failure("最小数量应大于0");
			}
			if (model.getMaxGoodsNum() <= model.getMinGoodsNum()) {
				return Results.failure("最大数量应大于最小数量");
			}

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
		logger.info("----------------游戏删除--------------");
		try {
			SessionData session = SessionUtil.sessionData.get();
			Inno72User mUser = Optional.ofNullable(session).map(SessionData::getUser).orElse(null);
			if (mUser == null) {
				logger.info("登陆用户为空");
				return Results.failure("未找到用户登录信息");
			}

			int nmu = inno72GameMapper.selectIsUseing(id);
			if (nmu > 0) {
				return ResultGenerator.genFailResult("游戏使用中，不能删除！");
			}
			String userId = Optional.ofNullable(mUser).map(Inno72User::getId).orElse(null);
			Inno72Game model = inno72GameMapper.selectByPrimaryKey(id);
			model.setIsDelete(1);
			model.setUpdateId(userId);
			model.setUpdateTime(LocalDateTime.now());
			super.update(model);
		} catch (Exception e) {
			logger.info(e.getMessage());
			return Results.failure("操作失败");
		}
		return ResultGenerator.genSuccessResult();
	}

	@Override
	public Result<String> updateModel(Inno72Game model) {
		logger.info("----------------游戏更新--------------");
		try {
			SessionData session = SessionUtil.sessionData.get();
			Inno72User mUser = Optional.ofNullable(session).map(SessionData::getUser).orElse(null);
			if (mUser == null) {
				logger.info("登陆用户为空");
				return Results.failure("未找到用户登录信息");
			}
			String userId = Optional.ofNullable(mUser).map(Inno72User::getId).orElse(null);
			if (null != model.getMinGoodsNum() && null != model.getMaxGoodsNum()) {
				if (model.getMinGoodsNum() < 1) {
					return Results.failure("最小数量应大于0");
				}
				if (model.getMaxGoodsNum() <= model.getMinGoodsNum()) {
					return Results.failure("最大数量应大于最小数量");
				}
			}

			model.setUpdateId(userId);
			model.setUpdateTime(LocalDateTime.now());
			super.update(model);
		} catch (Exception e) {
			logger.info(e.getMessage());
			return Results.failure("操作失败");
		}

		return ResultGenerator.genSuccessResult();
	}

	@Override
	public List<Inno72GameVo> findByPage(String code, String keyword) {
		logger.info("----------------游戏分页列表--------------");
		keyword = Optional.ofNullable(keyword).map(a -> a.replace("'", "")).orElse(keyword);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("keyword", keyword);
		params.put("code", code);

		return inno72GameMapper.selectByPage(params);
	}

	@Override
	public List<Inno72Game> getList(Inno72Game model) {
		logger.info("---------------------获取游戏列表-------------------");
		model.setIsDelete(0);
		Condition condition = new Condition(Inno72Goods.class);
		condition.createCriteria().andEqualTo(model);
		return super.findByCondition(condition);

	}

}
