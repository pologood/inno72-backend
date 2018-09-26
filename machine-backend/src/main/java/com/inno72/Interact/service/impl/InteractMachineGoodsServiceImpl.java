package com.inno72.Interact.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inno72.Interact.mapper.Inno72InteractMachineGoodsMapper;
import com.inno72.Interact.mapper.Inno72InteractMachineMapper;
import com.inno72.Interact.model.Inno72InteractMachine;
import com.inno72.Interact.model.Inno72InteractMachineGoods;
import com.inno72.Interact.service.InteractMachineGoodsService;
import com.inno72.Interact.vo.Inno72InteractMachineGoodsVo;
import com.inno72.Interact.vo.InteractMachineGoods;
import com.inno72.common.AbstractService;
import com.inno72.common.DateUtil;
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
public class InteractMachineGoodsServiceImpl extends AbstractService<Inno72InteractMachineGoods>
		implements InteractMachineGoodsService {

	private static Logger logger = LoggerFactory.getLogger(InteractMachineGoodsServiceImpl.class);
	@Resource
	private Inno72InteractMachineGoodsMapper inno72InteractMachineGoodsMapper;

	@Resource
	private Inno72InteractMachineMapper inno72InteractMachineMapper;

	@Override
	public Result<String> save(InteractMachineGoods model) {
		try {
			SessionData session = SessionUtil.sessionData.get();
			Inno72User mUser = Optional.ofNullable(session).map(SessionData::getUser).orElse(null);
			if (mUser == null) {
				logger.info("登陆用户为空");
				return Results.failure("未找到用户登录信息");
			}
			String interactId = model.getInteractId();

			List<String> machines = model.getMachines();
			if (null == machines || machines.size() < 1) {
				logger.info("请选择机器");
				return Results.failure("请选择机器");
			}
			List<Inno72InteractMachineGoodsVo> goods = model.getGoods();

			if (null == machines || machines.size() < 1) {
				logger.info("请选择商品");
				return Results.failure("请选择商品");
			}
			for (String machineId : machines) {
				Inno72InteractMachine interactMachine = new Inno72InteractMachine();
				interactMachine.setInteractId(interactId);
				interactMachine.setMachineId(machineId);
				Inno72InteractMachine base = inno72InteractMachineMapper.selectOne(interactMachine);

				for (Inno72InteractMachineGoodsVo machineGoods : goods) {
					machineGoods.setId(StringUtil.getUUID());
					machineGoods.setInteractMachineId(base.getId());
					machineGoods.setStartTime(DateUtil.toDateTime(machineGoods.getStartTimeStr(), DateUtil.DF_FULL_S1));
					machineGoods.setEndTime(DateUtil.toDateTime(machineGoods.getEndTimeStr(), DateUtil.DF_FULL_S1));
				}
			}

			inno72InteractMachineGoodsMapper.insertInteractMachineGoodsList(goods);

		} catch (Exception e) {
			e.printStackTrace();
			logger.info(e.getMessage());
			return Results.failure("操作失败");
		}
		return Results.success("操作成功");
	}

	@Override
	public List<Inno72InteractMachineGoodsVo> selectMachineGoods(String interactId, String machineId) {
		Map<String, Object> pm = new HashMap<>();
		pm.put("interactId", interactId);
		pm.put("machineId", machineId);

		List<Inno72InteractMachineGoodsVo> list = inno72InteractMachineGoodsMapper.selectMachineGoods(pm);
		return list;
	}

}
