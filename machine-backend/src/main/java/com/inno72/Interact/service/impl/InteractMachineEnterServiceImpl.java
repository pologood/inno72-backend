package com.inno72.Interact.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inno72.Interact.mapper.Inno72InteractMachineMapper;
import com.inno72.Interact.mapper.Inno72MachineEnterMapper;
import com.inno72.Interact.model.Inno72MachineEnter;
import com.inno72.Interact.service.InteractMachineEnterService;
import com.inno72.Interact.vo.MachineEnterVo;
import com.inno72.common.AbstractService;
import com.inno72.common.Result;
import com.inno72.common.Results;

/**
 * Created by CodeGenerator on 2019/03/15.
 */
@Service
@Transactional
public class InteractMachineEnterServiceImpl extends AbstractService<Inno72MachineEnter>
		implements InteractMachineEnterService {

	private static Logger logger = LoggerFactory.getLogger(InteractMachineEnterServiceImpl.class);

	@Resource
	private Inno72MachineEnterMapper inno72MachineEnterMapper;

	@Resource
	private MachineEnterServiceImpl machineEnterServiceImpl;

	@Resource
	private Inno72InteractMachineMapper inno72InteractMachineMapper;

	@Override
	public List<MachineEnterVo> findByPage(String interactId, Integer status, String machineCode) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("interactId", interactId);
		params.put("status", status);
		params.put("machineCode", machineCode);

		return inno72MachineEnterMapper.selectByPage(params);
	}

	@Override
	public Result<Object> updateBatchEnter(String interactId, String enterType) {

		if (!enterType.equals("1") && !enterType.equals("2")) {
			return Results.failure("入驻平台不存在");

		}

		List<String> failList = new ArrayList<>();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("interactId", interactId);
		params.put("enterType", enterType);
		List<Inno72MachineEnter> machineEnterList = inno72MachineEnterMapper.selectMachineEnterList(params);

		if (null != machineEnterList && machineEnterList.size() > 0) {

			for (Inno72MachineEnter machineEnter : machineEnterList) {
				// 入驻平台：1 蚂蚁金服，2京东金融
				Result<Object> res = new Result<>();
				try {
					if (enterType.equals("1")) {
						res = machineEnterServiceImpl.alipayAutomatUpload(machineEnter.getMachineId());
					} else if (enterType.equals("2")) {
						res = machineEnterServiceImpl.jingdongAutomatUpload(machineEnter.getMachineId());
					}
					if (res.getCode() == 0) {
						// 更新入驻记录
						machineEnter.setEnterStatus(1);
						inno72MachineEnterMapper.updateByPrimaryKeySelective(machineEnter);
					} else {
						failList.add(machineEnter.getMachineCode());
					}
				} catch (Exception e) {
					failList.add(machineEnter.getMachineCode());
					continue;
				}
			}
		} else {
			return Results.failure("无可入驻机器");
		}
		return Results.warn("入驻成功", 0, failList);
	}

	@Override
	public Result<Object> updateEnter(String machineId, String enterType) {

		if (!enterType.equals("1") && !enterType.equals("2")) {
			return Results.failure("入驻平台不存在");
		}
		// 判断机器是否已入驻
		Inno72MachineEnter machineEnter = new Inno72MachineEnter();
		machineEnter.setMachineId(machineId);
		machineEnter.setEnterType(enterType);

		machineEnter = inno72MachineEnterMapper.selectOne(machineEnter);
		if (null != machineEnter && machineEnter.getEnterStatus() == 0) {
			// 入驻平台：1 蚂蚁金服，2京东金融
			Result<Object> res = new Result<>();
			if (enterType.equals("1")) {
				res = machineEnterServiceImpl.alipayAutomatUpload(machineId);
			} else if (enterType.equals("2")) {
				res = machineEnterServiceImpl.jingdongAutomatUpload(machineId);
			}
			if (res.getCode() == 0) {
				// 更新入驻记录
				machineEnter.setEnterStatus(1);
				inno72MachineEnterMapper.updateByPrimaryKeySelective(machineEnter);

				return Results.warn("入驻成功", 0, null);
			} else {
				return Results.failure("入驻失败");
			}

		} else {
			logger.info("入驻信息有误");
			return Results.failure("入驻失败");
		}

	}

}
