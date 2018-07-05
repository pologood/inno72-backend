package com.inno72.machine.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.inno72.common.*;
import com.inno72.machine.mapper.Inno72SupplyChannelGoodsMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inno72.machine.mapper.Inno72SupplyChannelMapper;
import com.inno72.machine.model.Inno72SupplyChannel;
import com.inno72.machine.model.Inno72SupplyChannelDict;
import com.inno72.machine.service.SupplyChannelDictService;
import com.inno72.machine.service.SupplyChannelService;

/**
 * Created by CodeGenerator on 2018/07/04.
 */
@Service
@Transactional
public class SupplyChannelServiceImpl extends AbstractService<Inno72SupplyChannel> implements SupplyChannelService {
	@Resource
	private Inno72SupplyChannelMapper inno72SupplyChannelMapper;

	@Resource
	private SupplyChannelDictService supplyChannelDictService;

	@Resource
	private Inno72SupplyChannelGoodsMapper inno72SupplyChannelGoodsMapper;

	@Override
	public Result<Inno72SupplyChannel> subCount(Inno72SupplyChannel supplyChannel) {
		String machineId = supplyChannel.getMachineId();
		String code = supplyChannel.getCode();
		if(StringUtil.isEmpty(machineId) || StringUtil.isEmpty(code)){
			return Results.failure("参数错误");
		}
		Map<String,Object> map = new HashMap<>();
		map.put("machineId",machineId);
		map.put("code",code);
		supplyChannel = inno72SupplyChannelMapper.selectByParam(map);
		if(supplyChannel == null){
			return Results.failure("货道不存在");
		}else if(supplyChannel.getGoodsStatus()==1){
			return Results.failure("商品已下架");
		}else if(supplyChannel.getGoodsCount()<=0){
			return Results.failure("商品已无货");
		}
		inno72SupplyChannelMapper.subCount(supplyChannel);
		supplyChannel = inno72SupplyChannelMapper.selectByParam(map);
		return ResultGenerator.genSuccessResult(supplyChannel);
	}

	@Override
	public Result getSupplyChannel(Inno72SupplyChannel supplyChannel) {
		String machineId = supplyChannel.getMachineId();
		String[] goodsCodes = supplyChannel.getGoodsCodes();
		System.out.print(goodsCodes);
		if(StringUtil.isEmpty(machineId) || goodsCodes == null){
			return ResultGenerator.genFailResult("参数有误");
		}
		Map<String,Object> map = new HashMap<>();
		map.put("machineId",machineId);
		map.put("goodsCodes",goodsCodes);
		List<Inno72SupplyChannel> list = inno72SupplyChannelMapper.selectListByParam(map);
		return ResultGenerator.genSuccessResult(list);
	}

	@Override
	public Result<String> init(String machineId) {
		Inno72SupplyChannel channel = new Inno72SupplyChannel();
		channel.setMachineId(machineId);
		List<Inno72SupplyChannel> supplyChannelList = inno72SupplyChannelMapper.select(channel);
		if(supplyChannelList == null || supplyChannelList.size()<= 0 ){
			List<Inno72SupplyChannelDict> dictList = supplyChannelDictService.getAll();
			if(dictList != null && dictList.size()>0){
				supplyChannelList = new ArrayList<>();
				for(Inno72SupplyChannelDict dict:dictList){
					Inno72SupplyChannel supplyChannel = new Inno72SupplyChannel();
					supplyChannel.setMachineId(machineId);
					supplyChannel.setId(StringUtil.getUUID());
					supplyChannel.setName(dict.getName());
					supplyChannel.setCode(dict.getCode());
					supplyChannel.setCreateId("0");
					supplyChannelList.add(supplyChannel);
					super.save(supplyChannel);
				}
			}
		}
		return null;
	}

	@Override
	public Result<String> merge(Inno72SupplyChannel supplyChannel) {
		String fromCode = supplyChannel.getFromCode();
		String toCode = supplyChannel.getToCode();
		String machineId = supplyChannel.getMachineId();
		if(StringUtil.isEmpty(fromCode) || StringUtil.isEmpty(toCode) || StringUtil.isEmpty(machineId)){
			return ResultGenerator.genFailResult("参数有误");
		}
		int from = Integer.parseInt(fromCode);
		int to = Integer.parseInt(toCode);
		if(from%2==0 && to==from-1){
			Map<String,Object> map = new HashMap<>();
			map.put("machineId",machineId);
			String[] codes = new String[2];
			codes[0] = fromCode;
			codes[1] = toCode;
			map.put("codes",codes);
			List<Inno72SupplyChannel> list = inno72SupplyChannelMapper.selectListByParam(map);
			if(list != null && list.size()==2){
				for(Inno72SupplyChannel channel : list){
					int goodsCount = channel.getGoodsCount();
					if(goodsCount != 0){
						return ResultGenerator.genFailResult("当前货道未清0不能合并");
					}
				}
				Inno72SupplyChannel channel = new Inno72SupplyChannel();
				channel.setCode(fromCode);
				channel.setMachineId(machineId);
				channel.setParentCode(toCode);
				channel.setStatus(1);
				inno72SupplyChannelMapper.updateByParam(channel);
				return ResultGenerator.genSuccessResult();
			}else{
				return ResultGenerator.genFailResult("货道数据有误");
			}

		}else{
			return ResultGenerator.genFailResult("当前货道不能合并");
		}
	}

	@Override
	public Result<String> split(Inno72SupplyChannel supplyChannel) {
		String code = supplyChannel.getCode();
		String machineId = supplyChannel.getMachineId();
		if(StringUtil.isEmpty(code) || StringUtil.isEmpty(machineId)){
			return ResultGenerator.genFailResult("参数有误");
		}
		Map<String,Object> map = new HashMap<>();
		map.put("machineId",machineId);
		map.put("code",code);
		supplyChannel = inno72SupplyChannelMapper.selectByParam(map);
		if(supplyChannel != null){
			int goodsCount = supplyChannel.getGoodsCount();
			if(goodsCount == 0){
				Inno72SupplyChannel upChildChannel = new Inno72SupplyChannel();
				upChildChannel.setMachineId(machineId);
				upChildChannel.setParentCode(code);
				upChildChannel.setStatus(0);
				int count = inno72SupplyChannelMapper.updateChild(upChildChannel);
				if(count == 1){
					return ResultGenerator.genSuccessResult();
				}else{
					return ResultGenerator.genFailResult("货道未合并不能拆分");
				}

			}else{
				return ResultGenerator.genFailResult("货道未清0不能拆分");
			}
		}else{
			return ResultGenerator.genFailResult("操作货道有误");
		}
	}

	@Override
	public Result<String> clear(Inno72SupplyChannel supplyChannel) {
		String[] codes = supplyChannel.getCodes();
		String machineId = supplyChannel.getMachineId();
		if(codes == null || StringUtil.isEmpty(machineId)){
			return ResultGenerator.genFailResult("参数有误");
		}
		supplyChannel.setGoodsCount(0);
		inno72SupplyChannelMapper.updateListByParam(supplyChannel);
		return ResultGenerator.genSuccessResult();
	}

	@Override
	public Result<String> downAll(Inno72SupplyChannel supplyChannel) {
		String machineId = supplyChannel.getMachineId();
		if(StringUtil.isEmpty(machineId)){
			return Results.failure("参数有误");
		}
		Map<String,Object> map = new HashMap<>();
		map.put("machineId",machineId);
		List<Inno72SupplyChannel> supplyChannelList = inno72SupplyChannelMapper.selectListByParam(map);
		if(supplyChannelList == null || supplyChannelList.size()<=0){
			return Results.failure("本机器未设置货道");
		}
		String[] supplyChannelIds = new String[supplyChannelList.size()];
		for(int i=0;i<supplyChannelList.size();i++){
			supplyChannelIds[i] = supplyChannelList.get(i).getId();
		}
		map.put("supplyChannelIds",supplyChannelIds);
		map.put("isDelete",1);
		inno72SupplyChannelGoodsMapper.updateGoodsRelation(map);
		return ResultGenerator.genSuccessResult();
	}
}
