package com.inno72.Interact.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inno72.Interact.mapper.Inno72InteractGoodsMapper;
import com.inno72.Interact.mapper.Inno72InteractMachineGoodsMapper;
import com.inno72.Interact.mapper.Inno72InteractMachineMapper;
import com.inno72.Interact.mapper.Inno72InteractMapper;
import com.inno72.Interact.mapper.Inno72InteractMerchantMapper;
import com.inno72.Interact.mapper.Inno72InteractShopsMapper;
import com.inno72.Interact.model.Inno72Interact;
import com.inno72.Interact.model.Inno72InteractGoods;
import com.inno72.Interact.model.Inno72InteractMachine;
import com.inno72.Interact.service.InteractService;
import com.inno72.Interact.vo.Inno72InteractMachineGoodsVo;
import com.inno72.Interact.vo.InteractListVo;
import com.inno72.Interact.vo.InteractMerchantVo;
import com.inno72.Interact.vo.InteractRuleVo;
import com.inno72.Interact.vo.MachineVo;
import com.inno72.Interact.vo.TreeVo;
import com.inno72.common.AbstractService;
import com.inno72.common.Result;
import com.inno72.common.Results;
import com.inno72.common.SessionData;
import com.inno72.common.SessionUtil;
import com.inno72.common.StringUtil;
import com.inno72.system.model.Inno72User;

import tk.mybatis.mapper.entity.Condition;

/**
 * Created by CodeGenerator on 2018/09/19.
 */
@Service
@Transactional
public class InteractServiceImpl extends AbstractService<Inno72Interact> implements InteractService {
	private static Logger logger = LoggerFactory.getLogger(InteractServiceImpl.class);
	@Resource
	private Inno72InteractMapper inno72InteractMapper;

	@Resource
	private Inno72InteractMachineMapper inno72InteractMachineMapper;

	@Resource
	private Inno72InteractMerchantMapper inno72InteractMerchantMapper;

	@Resource
	private Inno72InteractShopsMapper inno72InteractShopsMapper;

	@Resource
	private Inno72InteractGoodsMapper inno72InteractGoodsMapper;

	@Resource
	private Inno72InteractMachineGoodsMapper inno72InteractMachineGoodsMapper;

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
			model.setStatus(0);
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

			if (type == 0) {
				inno72InteractMapper.updateByPrimaryKeySelective(model);
			} else if (type == 1) {
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
				inno72InteractMapper.updateByPrimaryKeySelective(model);
			} else if (type == 2) {
				Inno72Interact old = new Inno72Interact();
				old.setId(model.getId());
				old = inno72InteractMapper.selectOne(old);
				old.setStatus(2);
				inno72InteractMapper.updateByPrimaryKeySelective(old);
			} else if (null == type) {
				logger.info("参数错误");
				return Results.failure("参数错误");
			}

			return Results.warn("操作成功", 0, model.getId());

		} catch (Exception e) {
			e.printStackTrace();
			logger.info(e.getMessage());
			return Results.failure("操作失败");
		}
	}

	@Override
	public Result<Object> updateRule(InteractRuleVo interactRule) {

		try {
			SessionData session = SessionUtil.sessionData.get();
			Inno72User mUser = Optional.ofNullable(session).map(SessionData::getUser).orElse(null);
			if (mUser == null) {
				logger.info("登陆用户为空");
				return Results.failure("未找到用户登录信息");
			}
			String mUserId = Optional.ofNullable(mUser).map(Inno72User::getId).orElse(null);

			Inno72Interact interact = inno72InteractMapper.selectByPrimaryKey(interactRule.getId());

			interact.setUpdateId(mUserId);
			interact.setUpdateTime(LocalDateTime.now());

			if (interactRule.getType() == 1) {
				// 下一步
				if (null == interactRule.getTimes()) {
					logger.info("填写同一用户参与活动次数");
					return Results.failure("填写同一用户参与活动次数");
				}
				if (null == interactRule.getDayTimes()) {
					logger.info("填写同一用户每天参与活动次数");
					return Results.failure("填写同一用户每天参与活动次数");
				}
				if (null == interactRule.getNumber()) {
					logger.info("填写同一用户获得商品次数");
					return Results.failure("填写同一用户获得商品次数");
				}
				if (null == interactRule.getDayNumber()) {
					logger.info("填写同一用户每天得商品次数");
					return Results.failure("填写同一用户每天得商品次数");
				}
				interact.setTimes(interactRule.getTimes());
				interact.setDayTimes(interactRule.getDayTimes());
				interact.setNumber(interactRule.getNumber());
				interact.setDayNumber(interactRule.getDayNumber());

				interact.setStatus(1);

			} else if (interactRule.getType() == 0) {
				interact.setTimes(interactRule.getTimes());
				interact.setDayTimes(interactRule.getDayTimes());
				interact.setNumber(interactRule.getNumber());
				interact.setDayNumber(interactRule.getDayNumber());

			} else if (null == interactRule.getType()) {
				logger.info("参数错误");
				return Results.failure("参数错误");
			}

			List<Map<String, Object>> goodsRule = interactRule.getGoodsRule();
			if (null != goodsRule && goodsRule.size() > 0) {
				for (Map<String, Object> map : goodsRule) {
					String goodsId = map.get("goodsId").toString();
					Integer userDayNumber = (Integer) map.get("userDayNumber");
					if (StringUtil.isBlank(goodsId) || null == userDayNumber) {
						logger.info("填写商品规则");
						return Results.failure("填写商品规则");
					}
				}

				for (Map<String, Object> map : goodsRule) {
					String goodsId = map.get("goodsId").toString();
					Integer userDayNumber = (Integer) map.get("userDayNumber");
					Inno72InteractGoods interactGoods = new Inno72InteractGoods();
					interactGoods.setInteractId(interactRule.getId());
					interactGoods.setGoodsId(goodsId);

					Condition condition = new Condition(Inno72InteractGoods.class);
					condition.createCriteria().andEqualTo(interactGoods);

					interactGoods.setUserDayNumber(userDayNumber);
					inno72InteractGoodsMapper.updateByConditionSelective(interactGoods, condition);

				}
			}

			inno72InteractMapper.updateByPrimaryKeySelective(interact);

			return Results.success();

		} catch (Exception e) {
			e.printStackTrace();
			logger.info(e.getMessage());
			return Results.failure("操作失败");
		}
	}

	@Override
	public Result<String> next(String interactId, String type) {

		try {
			SessionData session = SessionUtil.sessionData.get();
			Inno72User mUser = Optional.ofNullable(session).map(SessionData::getUser).orElse(null);
			if (mUser == null) {
				logger.info("登陆用户为空");
				return Results.failure("未找到用户登录信息");
			}

			if (StringUtil.isBlank(type)) {
				logger.info("参数错误");
				return Results.failure("参数错误");
			}

			if (type.equals("goods")) {
				// 判断是否有商品

				Inno72InteractGoods ig = new Inno72InteractGoods();
				ig.setInteractId(interactId);
				int n = inno72InteractGoodsMapper.selectCount(ig);

				if (n > 0) {
					return Results.success("成功");
				} else {
					return Results.failure("失败");
				}

			} else if (type.equals("machine")) {
				// 判断是否有机器
				Inno72InteractMachine im = new Inno72InteractMachine();
				im.setInteractId(interactId);
				int n = inno72InteractMachineMapper.selectCount(im);
				if (n > 0) {
					return Results.success("成功");
				} else {
					return Results.failure("失败");
				}
			} else {
				logger.info("参数错误");
				return Results.failure("参数错误");
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.info(e.getMessage());
			return Results.failure("操作失败");
		}
	}

	@Override
	public List<TreeVo> machineTree(String interactId) {

		Map<String, Object> pm = new HashMap<>();
		pm.put("interactId", interactId);
		List<MachineVo> machineList = inno72InteractMachineMapper.getHavingMachines(pm);

		List<TreeVo> firstList = new ArrayList<>();

		for (MachineVo machine : machineList) {

			TreeVo first = new TreeVo();
			first.setKey(machine.getMachineId());
			first.setTitle(machine.getLocalDesc() + "(" + machine.getMachineCode() + ")");
			Map<String, Object> params = new HashMap<>();
			pm.put("interactId", interactId);
			pm.put("machineId", machine.getMachineId());
			List<TreeVo> secondList = new ArrayList<>();
			List<Inno72InteractMachineGoodsVo> list = inno72InteractMachineGoodsMapper.selectMachineGoods(params);
			for (Inno72InteractMachineGoodsVo goodsVo : list) {
				TreeVo second = new TreeVo();
				second.setKey(goodsVo.getGoodsId());
				second.setTitle(goodsVo.getGoodsName());
				secondList.add(second);
			}
			first.setChildren(secondList);

			firstList.add(first);

		}

		return firstList;
	}

	@Override
	public List<TreeVo> merchantTree(String interactId) {

		List<InteractMerchantVo> merchantList = inno72InteractMerchantMapper.selectMerchantByInteractId(interactId);

		List<TreeVo> firstList = new ArrayList<>();

		for (InteractMerchantVo interactMerchantVo : merchantList) {

			TreeVo first = new TreeVo();
			first.setKey(interactMerchantVo.getId());
			first.setTitle(interactMerchantVo.getMerchantName());
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("sellerId", interactMerchantVo.getId());
			List<TreeVo> secondList = inno72InteractShopsMapper.selectMerchantShopsTree(params);
			first.setChildren(secondList);

			for (TreeVo second : secondList) {
				Map<String, Object> p = new HashMap<String, Object>();
				p.put("shopsId", second.getKey());
				List<TreeVo> thirdList = inno72InteractGoodsMapper.selectGoodsTree(p);
				second.setChildren(thirdList);
			}
			firstList.add(first);

		}

		return firstList;
	}

}
