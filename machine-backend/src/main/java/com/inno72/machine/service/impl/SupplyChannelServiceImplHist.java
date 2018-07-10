package com.inno72.machine.service.impl;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.inno72.common.AbstractService;
import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;
import com.inno72.common.Results;
import com.inno72.common.StringUtil;
import com.inno72.machine.mapper.Inno72SupplyChannelGoodsMapper;
import com.inno72.machine.mapper.Inno72SupplyChannelHistMapper;
import com.inno72.machine.model.Inno72SupplyChannelHist;
import com.inno72.machine.service.SupplyChannelDictService;
import com.inno72.machine.service.SupplyChannelServiceHist;
import com.inno72.utils.page.Pagination;
import com.mongodb.DBCollection;

import tk.mybatis.mapper.entity.Condition;

/**
 * Created by CodeGenerator on 2018/07/04.
 */
@Service
@Transactional
public class SupplyChannelServiceImplHist extends AbstractService<Inno72SupplyChannelHist>
		implements SupplyChannelServiceHist {

	Logger logger = LoggerFactory.getLogger(this.getClass());
	@Resource
	private Inno72SupplyChannelHistMapper inno72SupplyChannelHistMapper;

	@Resource
	private SupplyChannelDictService supplyChannelDictService;

	@Resource
	private Inno72SupplyChannelGoodsMapper inno72SupplyChannelGoodsMapper;

	@Autowired
	private MongoOperations mongoTpl;

	@Override
	public Result<Inno72SupplyChannelHist> subCount(Inno72SupplyChannelHist supplyChannel) {
		String machineId = supplyChannel.getMachineId();
		String code = supplyChannel.getCode();
		if (StringUtil.isEmpty(machineId) || StringUtil.isEmpty(code)) {
			return Results.failure("参数错误");
		}
		Map<String, Object> map = new HashMap<>();
		map.put("machineId", machineId);
		map.put("code", code);
		supplyChannel = inno72SupplyChannelHistMapper.selectByParam(map);
		if (supplyChannel == null) {
			return Results.failure("货道不存在");
		} else if (supplyChannel.getGoodsStatus() == 1) {
			return Results.failure("商品已下架");
		} else if (supplyChannel.getGoodsCount() <= 0) {
			return Results.failure("商品已无货");
		}
		supplyChannel.setUpdateTime(LocalDateTime.now());
		inno72SupplyChannelHistMapper.subCount(supplyChannel);
		supplyChannel = inno72SupplyChannelHistMapper.selectByParam(map);
		supplyChannel.setRemark("货道商品数量减一");
		addSupplyChannelToMongo(supplyChannel);
		return ResultGenerator.genSuccessResult(supplyChannel);
	}

	@Override
	public Result getSupplyChannel(Inno72SupplyChannelHist supplyChannel) {
		String machineId = supplyChannel.getMachineId();
		String[] goodsCodes = supplyChannel.getGoodsCodes();
		System.out.print(goodsCodes);
		if (StringUtil.isEmpty(machineId) || goodsCodes == null) {
			return ResultGenerator.genFailResult("参数有误");
		}
		Map<String, Object> map = new HashMap<>();
		map.put("machineId", machineId);
		map.put("goodsCodes", goodsCodes);
		List<Inno72SupplyChannelHist> list = inno72SupplyChannelHistMapper.selectListByParam(map);
		return ResultGenerator.genSuccessResult(list);
	}

	@Override
	public Result<String> init(String machineId, List<Inno72SupplyChannelHist> channels) {
		if (channels == null) {
			return Results.failure("货道信息错误");
		}
		Condition condition = new Condition(Inno72SupplyChannelHist.class);
		condition.createCriteria().andEqualTo("machineId", machineId);
		List<Inno72SupplyChannelHist> supplyChannelList = inno72SupplyChannelHistMapper.selectByCondition(condition);
		if (supplyChannelList != null) {
			for (Inno72SupplyChannelHist channel : channels) {
				channel.setMachineId(machineId);
				channel.setId(StringUtil.getUUID());
				channel.setName("货道" + channel.getCode());
				channel.setGoodsCount(0);
				channel.setCreateId("系统");
				channel.setCreateTime(LocalDateTime.now());
				channel.setUpdateTime(LocalDateTime.now());
				channel.setUpdateId("系统");
				super.save(channel);
			}
		}
		return Results.success();
	}

	@Override
	public Result<String> merge(Inno72SupplyChannelHist supplyChannel) {
		String fromCode = supplyChannel.getFromCode();
		String toCode = supplyChannel.getToCode();
		String machineId = supplyChannel.getMachineId();
		if (StringUtil.isEmpty(fromCode) || StringUtil.isEmpty(toCode) || StringUtil.isEmpty(machineId)) {
			return ResultGenerator.genFailResult("参数有误");
		}
		int from = Integer.parseInt(fromCode);
		int to = Integer.parseInt(toCode);
		if (from % 2 == 0 && to == from - 1) {
			Map<String, Object> map = new HashMap<>();
			map.put("machineId", machineId);
			String[] codes = new String[2];
			codes[0] = fromCode;
			codes[1] = toCode;
			map.put("codes", codes);
			List<Inno72SupplyChannelHist> list = inno72SupplyChannelHistMapper.selectListByParam(map);
			if (list != null && list.size() == 2) {
				for (Inno72SupplyChannelHist channel : list) {
					int goodsCount = channel.getGoodsCount();
					if (goodsCount != 0) {
						return ResultGenerator.genFailResult("当前货道未清0不能合并");
					}
				}
				Inno72SupplyChannelHist channel = new Inno72SupplyChannelHist();
				channel.setCode(fromCode);
				channel.setMachineId(machineId);
				channel.setParentCode(toCode);
				channel.setStatus(1);
				channel.setUpdateTime(LocalDateTime.now());
				inno72SupplyChannelHistMapper.updateByParam(channel);
				channel.setRemark("合并货道，由" + fromCode + "合并到" + toCode);
				addSupplyChannelToMongo(channel);
				return ResultGenerator.genSuccessResult();
			} else {
				return ResultGenerator.genFailResult("货道数据有误");
			}

		} else {
			return ResultGenerator.genFailResult("当前货道不能合并");
		}
	}

	@Override
	public Result<String> split(Inno72SupplyChannelHist supplyChannel) {
		String code = supplyChannel.getCode();
		String machineId = supplyChannel.getMachineId();
		if (StringUtil.isEmpty(code) || StringUtil.isEmpty(machineId)) {
			return ResultGenerator.genFailResult("参数有误");
		}
		Map<String, Object> map = new HashMap<>();
		map.put("machineId", machineId);
		map.put("code", code);
		supplyChannel = inno72SupplyChannelHistMapper.selectByParam(map);
		if (supplyChannel != null) {
			int goodsCount = supplyChannel.getGoodsCount();
			if (goodsCount == 0) {
				Map<String, Object> childMap = new HashMap<>();
				childMap.put("machineId", machineId);
				childMap.put("parentCode", code);
				Inno72SupplyChannelHist childChannel = inno72SupplyChannelHistMapper.selectByParentCode(childMap);
				childChannel.setParentCode(code);
				childChannel.setStatus(0);
				childChannel.setUpdateTime(LocalDateTime.now());
				int count = inno72SupplyChannelHistMapper.updateChild(childChannel);
				if (count == 1) {
					childChannel.setRemark("拆分货道，将货道" + code + "和货道" + childChannel.getCode() + "拆分");
					addSupplyChannelToMongo(childChannel);
					return ResultGenerator.genSuccessResult();
				} else {
					return ResultGenerator.genFailResult("货道未合并不能拆分");
				}

			} else {
				return ResultGenerator.genFailResult("货道未清0不能拆分");
			}
		} else {
			return ResultGenerator.genFailResult("操作货道有误");
		}
	}

	/**
	 * 货道清零
	 * 
	 * @param supplyChannel
	 * @return
	 */
	@Override
	public Result<String> clear(Inno72SupplyChannelHist supplyChannel) {
		String[] codes = supplyChannel.getCodes();
		String machineId = supplyChannel.getMachineId();
		if (codes == null || StringUtil.isEmpty(machineId)) {
			return ResultGenerator.genFailResult("参数有误");
		}
		supplyChannel.setGoodsCount(0);
		Map<String, Object> map = new HashMap<>();
		map.put("codes", codes);
		map.put("machineId", machineId);
		supplyChannel.setUpdateTime(LocalDateTime.now());
		inno72SupplyChannelHistMapper.updateListByParam(supplyChannel);
		List<Inno72SupplyChannelHist> list = inno72SupplyChannelHistMapper.selectListByParam(map);
		for (Inno72SupplyChannelHist channel : list) {
			channel.setRemark("货道商品数量清零");
			addSupplyChannelToMongo(channel);
		}
		return ResultGenerator.genSuccessResult();
	}

	@Override
	public Result<String> downAll(Inno72SupplyChannelHist supplyChannel) {
		String machineId = supplyChannel.getMachineId();
		if (StringUtil.isEmpty(machineId)) {
			return Results.failure("参数有误");
		}
		Map<String, Object> map = new HashMap<>();
		map.put("machineId", machineId);
		List<Inno72SupplyChannelHist> supplyChannelList = inno72SupplyChannelHistMapper.selectListByParam(map);
		if (supplyChannelList == null || supplyChannelList.size() <= 0) {
			return Results.failure("本机器未设置货道");
		}
		String[] supplyChannelIds = new String[supplyChannelList.size()];
		for (int i = 0; i < supplyChannelList.size(); i++) {
			supplyChannelIds[i] = supplyChannelList.get(i).getId();
		}
		map.put("supplyChannelIds", supplyChannelIds);
		map.put("isDelete", 1);
		inno72SupplyChannelGoodsMapper.updateGoodsRelation(map);
		for (Inno72SupplyChannelHist channel : supplyChannelList) {
			channel.setIsDelete(1);
			channel.setRemark("货道商品全部下架");
			channel.setUpdateTime(LocalDateTime.now());
		}
		return ResultGenerator.genSuccessResult();
	}

	@Override
	public Result<Map<String, Object>> history(Inno72SupplyChannelHist supplyChannel) {
		String machineId = supplyChannel.getMachineId();
		String code = supplyChannel.getCode();
		if (StringUtil.isEmpty(machineId)) {
			return Results.failure("机器ID不能为空");
		}

		Query query = new Query();
		Criteria criteria = Criteria.where("machineId").is(machineId);
		if (StringUtil.isNotEmpty(code)) {
			criteria.and("code").is(code);
		}
		query.addCriteria(criteria);
		query.with(new Sort(Sort.Direction.DESC, "updateTime"));
		int pageNo = 0;
		int pageSize = 20;
		Long count = mongoTpl.count(query, Inno72SupplyChannelHist.class, "supplyChannel");
		Pagination pagination = new Pagination(pageNo, pageSize, count.intValue());
		query.skip((pageNo - 1) * pageSize).limit(pageSize);
		List<Inno72SupplyChannelHist> supplyChannelList = mongoTpl.find(query, Inno72SupplyChannelHist.class,
				"supplyChannel");
		logger.info("mongo返回data:{}", JSON.toJSON(supplyChannelList));
		Map<String, Object> map = new HashMap<>();
		map.put("data", supplyChannelList);
		map.put("page", pagination);
		map.put("msg", "成功");
		map.put("code", 0);
		return ResultGenerator.genSuccessResult(map);
	}

	@Override
	public List<Inno72SupplyChannelHist> getListForPage(Inno72SupplyChannelHist supplyChannel) {
		List<Inno72SupplyChannelHist> list = inno72SupplyChannelHistMapper.selectListForPage(supplyChannel);
		return list;
	}

	public void addSupplyChannelToMongo(Inno72SupplyChannelHist supplyChannel) {
		DBCollection dbCollection = mongoTpl.getCollection("supplyChannel");
		if (dbCollection == null) {
			mongoTpl.createCollection("supplyChannel");
		}
		supplyChannel.setId(null);
		mongoTpl.save(supplyChannel, "supplyChannel");
	}
}