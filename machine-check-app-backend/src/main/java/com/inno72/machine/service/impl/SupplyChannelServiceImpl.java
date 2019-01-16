package com.inno72.machine.service.impl;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.DataFormatException;

import javax.annotation.Resource;

import com.inno72.check.mapper.Inno72CheckGoodsDetailMapper;
import com.inno72.check.mapper.Inno72CheckGoodsNumMapper;
import com.inno72.check.model.Inno72CheckGoodsDetail;
import com.inno72.check.model.Inno72CheckGoodsNum;
import com.inno72.check.vo.SignVo;
import com.inno72.common.DateUtil;
import com.inno72.machine.mapper.Inno72MachineBatchDetailMapper;
import com.inno72.machine.model.Inno72MachineBatchDetail;
import com.inno72.machine.vo.CommonVo;
import com.inno72.machine.vo.SubmitSupplyChannel;
import com.inno72.machine.vo.SupplyRequestVo;

import org.apache.catalina.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.inno72.check.model.Inno72CheckUser;
import com.inno72.common.AbstractService;
import com.inno72.common.CommonConstants;
import com.inno72.common.ImageUtil;
import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;
import com.inno72.common.Results;
import com.inno72.common.StringUtil;
import com.inno72.common.UserUtil;
import com.inno72.machine.mapper.Inno72GoodsMapper;
import com.inno72.machine.mapper.Inno72MachineMapper;
import com.inno72.machine.mapper.Inno72SupplyChannelGoodsMapper;
import com.inno72.machine.mapper.Inno72SupplyChannelHistoryMapper;
import com.inno72.machine.mapper.Inno72SupplyChannelMapper;
import com.inno72.machine.mapper.Inno72SupplyChannelOrderMapper;
import com.inno72.machine.model.Inno72Goods;
import com.inno72.machine.model.Inno72Machine;
import com.inno72.machine.model.Inno72SupplyChannel;
import com.inno72.machine.model.Inno72SupplyChannelGoods;
import com.inno72.machine.model.Inno72SupplyChannelHistory;
import com.inno72.machine.model.Inno72SupplyChannelOrder;
import com.inno72.machine.service.SupplyChannelService;
import com.inno72.machine.vo.SupplyChannelVo;
import com.inno72.machine.vo.WorkOrderVo;
import com.inno72.model.AlarmMessageBean;
import com.inno72.model.ChannelGoodsAlarmBean;
import com.inno72.model.GoodsBean;
import com.inno72.model.MachineDropGoodsBean;
import com.inno72.plugin.statistic.Statistic;
import com.inno72.plugin.statistic.constant.StatisticEnum;
import com.inno72.redis.IRedisUtil;
import com.inno72.utils.page.Pagination;
import com.mongodb.DBCollection;

import tk.mybatis.mapper.entity.Condition;

/**
 * Created by CodeGenerator on 2018/07/04.
 */
@Service
@Transactional
public class SupplyChannelServiceImpl extends AbstractService<Inno72SupplyChannel> implements SupplyChannelService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Resource
	private Inno72SupplyChannelMapper inno72SupplyChannelMapper;

	@Resource
	private Inno72SupplyChannelGoodsMapper inno72SupplyChannelGoodsMapper;

	@Resource
	private Inno72MachineMapper inno72MachineMapper;

	@Resource
	private Inno72GoodsMapper inno72GoodsMapper;

	@Resource
	private Inno72SupplyChannelHistoryMapper inno72SupplyChannelHistoryMapper;

	@Resource
	private MongoOperations mongoTpl;

	@Resource
	private IRedisUtil redisUtil;

	@Resource
	private Inno72SupplyChannelOrderMapper inno72SupplyChannelOrderMapper;

	@Resource
	private Inno72MachineBatchDetailMapper inno72MachineBatchDetailMapper;

	@Resource
	private Statistic statistic;

	@Resource
	private Inno72CheckGoodsNumMapper inno72CheckGoodsNumMapper;

	@Resource
	private Inno72CheckGoodsDetailMapper inno72CheckGoodsDetailMapper;

	@Override
	public Result<String> merge(Inno72SupplyChannel supplyChannel) {
		String code = supplyChannel.getCode();
		String machineId = supplyChannel.getMachineId();
		if (StringUtil.isEmpty(code) || StringUtil.isEmpty(machineId)) {
			return ResultGenerator.genFailResult("参数有误");
		}
		Inno72Machine machine = inno72MachineMapper.getMachineById(machineId);
		String machineCode = machine.getMachineCode();
		Integer codeInt = Integer.parseInt(code);
		Inno72MachineBatchDetail batchDetail = getBatchDetail(machineCode,codeInt);
		if(batchDetail == null || (batchDetail.getType() != 1 && batchDetail.getType() != 2)){
			return Results.failure("当前货道不能合并");
		}
		Map<String, Object> map = new HashMap<>();
		map.put("machineId", machineId);
		Integer[] codes = new Integer[2];
		codes[0] = codeInt;
		Integer childCode;
		Integer parentCode;
		if (codeInt % 2 == 0) {
			codes[1] = (codeInt - 1);
			childCode = codes[0];
			parentCode = codes[1];
		} else {
			codes[1] = (codeInt + 1);
			childCode = codes[1];
			parentCode = codes[0];
		}

		map.put("codes", codes);
		List<Inno72SupplyChannel> list = inno72SupplyChannelMapper.selectListByParam(map);
		if (list != null && list.size() == 2) {
			Inno72SupplyChannel parentChannel = new Inno72SupplyChannel();
			parentChannel.setCode(parentCode.toString());
			parentChannel.setMachineId(machineId);
			parentChannel.setGoodsCount(0);
			parentChannel.setUpdateTime(LocalDateTime.now());
			inno72SupplyChannelMapper.updateByParam(parentChannel);// 修改主货道
			Inno72SupplyChannel childChannel = new Inno72SupplyChannel();
			childChannel.setCode(childCode.toString());
			childChannel.setMachineId(machineId);
			childChannel.setGoodsCount(0);
			childChannel.setStatus(1);
			childChannel.setUpdateTime(LocalDateTime.now());
			childChannel.setIsRemove(1);
			inno72SupplyChannelMapper.updateByParam(childChannel);
			for (Inno72SupplyChannel supply : list) {
				Condition condition = new Condition(Inno72SupplyChannelGoods.class);
				condition.createCriteria().andEqualTo("supplyChannelId", supply.getId());
				inno72SupplyChannelGoodsMapper.deleteByCondition(condition);// 删除货道关联商品
				int supplyCount =supply.getGoodsCount();
				String goodsId = supply.getGoodsId();
				if(StringUtil.isNotEmpty(goodsId)){
					this.saveGoodsNum(goodsId,machine.getActivityId(),supplyCount);
				}
			}
			String detail = "合并货道:在"+machine.getLocaleStr()+"点位处的机器对"+parentCode+"货道和"+childCode+"货道进行了合并，合并后为"+parentCode+"货道，"+childCode+"货道已被清除";
			StringUtil.logger(CommonConstants.LOG_TYPE_MERGE_CHANNEL,machineCode,detail);
			return ResultGenerator.genSuccessResult();
		} else {
			return ResultGenerator.genFailResult("该货道已合并");
		}
	}

	@Override
	public Result<String> split(Inno72SupplyChannel supplyChannel) {
		String code = supplyChannel.getCode();
		Inno72CheckUser checkUser = UserUtil.getUser();
		String machineId = supplyChannel.getMachineId();
		if (StringUtil.isEmpty(code) || StringUtil.isEmpty(machineId)) {
			return Results.failure("参数有误");
		}
		int codeInteger = Integer.parseInt(code);
		if (codeInteger % 2 == 0) {
			return Results.failure("当前货道不能拆分");
		}
		Map<String, Object> map = new HashMap<>();
		map.put("machineId", machineId);
		map.put("code", code);
		supplyChannel = inno72SupplyChannelMapper.selectByParam(map);
		if (supplyChannel != null) {
			String machineCode = supplyChannel.getMachineCode();
			Inno72MachineBatchDetail batchDetail = getBatchDetail(machineCode,codeInteger);
			if(batchDetail == null || (batchDetail.getType() != 1 && batchDetail.getType() != 2)){
				return Results.failure("当前货道不能拆分");
			}
			Integer newCode = codeInteger + 1;
			map.put("code", newCode.toString());
			Inno72SupplyChannel childChannel = inno72SupplyChannelMapper.selectByParam(map);
			if (childChannel != null && childChannel.getIsRemove()==0) {
				return Results.failure("该货道已拆分");
			}
			supplyChannel.setGoodsCount(0);
			supplyChannel.setUpdateTime(LocalDateTime.now());
			inno72SupplyChannelMapper.updateByPrimaryKeySelective(supplyChannel);
			Condition condition = new Condition(Inno72SupplyChannelGoods.class);
			condition.createCriteria().andEqualTo("supplyChannelId", supplyChannel.getId());
			inno72SupplyChannelGoodsMapper.deleteByCondition(condition);
			int volumeCount = batchDetail.getVolumeCount();
			if(childChannel != null){
				childChannel.setIsRemove(0);
				childChannel.setVolumeCount(volumeCount);
				childChannel.setStatus(0);
				childChannel.setGoodsCount(0);
				inno72SupplyChannelMapper.updateByParam(childChannel);
			}else{
				Map<String,Object> paramMap = new HashMap<>();
				paramMap.put("machineId",machineId);
				String supplyCode = newCode.toString();
				paramMap.put("code",supplyCode);
				Inno72SupplyChannel sChannel = inno72SupplyChannelMapper.selectOneBy(paramMap);
				if(supplyChannel==null){
					sChannel = new Inno72SupplyChannel();
					sChannel.setId(StringUtil.getUUID());
					sChannel.setMachineId(machineId);
					sChannel.setCode(supplyCode);
					sChannel.setName("货道"+supplyCode);
					sChannel.setStatus(0);
					sChannel.setVolumeCount(batchDetail.getVolumeCount());
					if(checkUser != null){
						supplyChannel.setCreateId(checkUser.getId());
						supplyChannel.setUpdateId(checkUser.getId());
					}else{
						supplyChannel.setCreateId("系统");
						supplyChannel.setUpdateId("系统");
					}
					supplyChannel.setCreateTime(LocalDateTime.now());
					supplyChannel.setUpdateTime(LocalDateTime.now());
					supplyChannel.setIsRemove(0);
					supplyChannel.setIsDelete(0);
					supplyChannel.setWorkStatus(0);
					inno72SupplyChannelMapper.insertSelective(supplyChannel);
				}else{
					supplyChannel.setStatus(0);
					supplyChannel.setVolumeCount(batchDetail.getVolumeCount());
					if(checkUser != null){
						supplyChannel.setCreateId(checkUser.getId());
						supplyChannel.setUpdateId(checkUser.getId());
					}else{
						supplyChannel.setCreateId("系统");
						supplyChannel.setUpdateId("系统");
					}
					supplyChannel.setCreateTime(LocalDateTime.now());
					supplyChannel.setUpdateTime(LocalDateTime.now());
					supplyChannel.setIsDelete(0);
					supplyChannel.setWorkStatus(0);
					supplyChannel.setIsRemove(0);
					inno72SupplyChannelMapper.updateByPrimaryKeySelective(supplyChannel);
				}
			}
			Inno72Machine machine = inno72MachineMapper.getMachineById(machineId);
			String detail = "拆分货道：在"+machine.getLocaleStr()+"点位处的机器对"+code+"货道进行了拆分，拆分后变为"+code+"货道和"+newCode+"货道";
			StringUtil.logger(CommonConstants.LOG_TYPE_SPLIT_CHANNEL,machine.getMachineCode(),detail);
			return ResultGenerator.genSuccessResult();
		} else {
			return ResultGenerator.genFailResult("操作货道有误");
		}
	}


	public Inno72MachineBatchDetail getBatchDetail(String machineCode,int codeInteger){
		Map<String,Object> detailMap = new HashMap<>();
		detailMap.put("machineCode",machineCode);
		int rowNo = 0;
		rowNo=codeInteger/10;
		rowNo++;
		detailMap.put("rowNo",rowNo);
		Inno72MachineBatchDetail batchDetail = inno72MachineBatchDetailMapper.selectByParam(detailMap);
		return batchDetail;
	}

	/**
	 * 货道清零
	 *
	 */
	@Override
	public Result<String> clear(Inno72SupplyChannel supplyChannel) {
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
		inno72SupplyChannelMapper.updateListByParam(supplyChannel);
		List<Inno72SupplyChannel> list = inno72SupplyChannelMapper.selectListByParam(map);
		for (Inno72SupplyChannel channel : list) {
			channel.setRemark("货道商品数量清零");
			addSupplyChannelToMongo(channel);
		}
		return ResultGenerator.genSuccessResult();
	}

	@Override
	public Result<Map<String, Object>> history(Inno72SupplyChannel supplyChannel) {
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
		Long count = mongoTpl.count(query, Inno72SupplyChannel.class, "supplyChannel");
		Pagination pagination = new Pagination(pageNo, pageSize, count.intValue());
		query.skip((pageNo - 1) * pageSize).limit(pageSize);
		List<Inno72SupplyChannel> supplyChannelList = mongoTpl.find(query, Inno72SupplyChannel.class, "supplyChannel");
		logger.info("mongo返回data:{}", JSON.toJSON(supplyChannelList));
		Map<String, Object> map = new HashMap<>();
		map.put("data", supplyChannelList);
		map.put("page", pagination);
		map.put("msg", "成功");
		map.put("code", 0);
		return ResultGenerator.genSuccessResult(map);
	}

	@Override
	public Result<List<Inno72SupplyChannel>> getList(String machineId) {
		if (StringUtil.isEmpty(machineId)) {
			return Results.failure("参数不能为空");
		}
		Condition condition = new Condition(Inno72SupplyChannel.class);
		condition.createCriteria().andEqualTo("machineId", machineId).andEqualTo("status", 0);
		condition.orderBy("code");
		Map<String, Object> map = new HashMap<>();
		map.put("machineId", machineId);
		map.put("status", 0);
		List<Inno72SupplyChannel> list = inno72SupplyChannelMapper.selectListByParam(map);
		return ResultGenerator.genSuccessResult(list);
	}

	@Override
	public Result<List<Inno72Machine>> getMachineLackGoods(CommonVo commonVo) {
		Inno72CheckUser checkUser = UserUtil.getUser();
		String checkUserId = checkUser.getId();
		Map<String,Object> paramMap = new HashMap<>();
		paramMap.put("checkUserId",checkUserId);
		String keyword = commonVo.getKeyword();
		if(StringUtil.isNotEmpty(keyword)){
			paramMap.put("keyword",keyword);
		}
		List<Inno72Machine> machineList = inno72MachineMapper.getMachineLack(paramMap);
		if (machineList != null && machineList.size() > 0) {
			for (Inno72Machine machine : machineList) {
				Integer lackGoodsStatus = 0;
				List<SupplyChannelVo> supplyChannelVoList = machine.getSupplyChannelVoList();
				if (supplyChannelVoList != null && supplyChannelVoList.size() > 0) {
					Map<String, Integer> map = new HashMap<>();
					for (SupplyChannelVo supplyChannelVo : supplyChannelVoList) {
						String goodsId = supplyChannelVo.getGoodsId();
						if (StringUtil.isNotEmpty(goodsId)) {
							int goodsCount = supplyChannelVo.getGoodsCount();
							if (map.containsKey(goodsId)) {
								int count = map.get(goodsId);
								count += goodsCount;
								map.put(goodsId, count);
							} else {
								map.put(goodsId, goodsCount);
							}
						}
					}
					for (Integer value : map.values()) {
						if (value < 10) {
							lackGoodsStatus = 1;
							break;
						}
					}
				}
				machine.setLackGoodsStatus(lackGoodsStatus);
				machine.setSupplyChannelVoList(null);
			}
		}
		Collections.sort(machineList);
		logger.info("机器缺货返回数据：{}", JSON.toJSON(machineList));
		return ResultGenerator.genSuccessResult(machineList);
	}

	@Override
	public Result<List<Inno72Goods>> getGoodsLack(CommonVo commonVo) {
		Inno72CheckUser checkUser = UserUtil.getUser();
		String checkUserId = checkUser.getId();
		Map<String,Object> paramMap = new HashMap<>();
		paramMap.put("checkUserId",checkUserId);
		String keyword = commonVo.getKeyword();
		if(StringUtil.isNotEmpty(keyword)){
			paramMap.put("keyword",keyword);
		}
		List<Inno72Goods> inno72GoodsList = inno72GoodsMapper.getLackGoods(paramMap);
		logger.info("商品数据：{}", JSON.toJSON(inno72GoodsList));
		List<Inno72Goods> resultList = new ArrayList<>();
		if (inno72GoodsList != null && inno72GoodsList.size() > 0) {

			for (Inno72Goods inno72Goods : inno72GoodsList) {
				List<SupplyChannelVo> supplyChannelVoList = inno72Goods.getSupplyChannelVoList();
				if (supplyChannelVoList != null && supplyChannelVoList.size() > 0) {
					int totalVolumeCount = 0;
					int totalGoodsCount = 0;
					for (SupplyChannelVo supplyChannelVo : supplyChannelVoList) {
						int volumeCount = supplyChannelVo.getVolumeCount();
						int goodsCount = supplyChannelVo.getGoodsCount();
						totalVolumeCount += volumeCount;
						totalGoodsCount += goodsCount;
					}
					inno72Goods.setLackGoodsCount(totalVolumeCount - totalGoodsCount);
					inno72Goods.setSupplyChannelVoList(null);
					inno72Goods.setImg(ImageUtil.getLongImageUrl(inno72Goods.getImg()));
					inno72Goods.setTotalGoodsCount(totalGoodsCount);
					if(totalVolumeCount != totalGoodsCount){
						resultList.add(inno72Goods);
					}
				}
			}
		}
		return ResultGenerator.genSuccessResult(resultList);
	}

	@Override
	public Result<List<Inno72Machine>> getMachineByLackGoods(String goodsId) {
		if (StringUtil.isEmpty(goodsId)) {
			return Results.failure("参数不能为空");
		}
		Inno72CheckUser checkUser = UserUtil.getUser();
		String checkUserId = checkUser.getId();
		List<Inno72Machine> machineList = inno72MachineMapper.getMachineByLackGoods(checkUserId, goodsId);
		List<Inno72Machine> resultList = new ArrayList<>();
		if (machineList != null && machineList.size() > 0) {
			for (Inno72Machine machine : machineList) {
				List<SupplyChannelVo> supplyChannelVoList = machine.getSupplyChannelVoList();
				int totalVolumeCount = 0;
				int totalGoodsCount = 0;
				for (SupplyChannelVo supplyChannelVo : supplyChannelVoList) {
					int volumeCount = supplyChannelVo.getVolumeCount();
					int goodsCount = supplyChannelVo.getGoodsCount();
					totalVolumeCount += volumeCount;
					totalGoodsCount += goodsCount;
				}
				if (totalVolumeCount - totalGoodsCount > 0) {
					machine.setLackGoodsCount(totalVolumeCount - totalGoodsCount);
					machine.setSupplyChannelVoList(null);
					machine.setTotalGoodsCount(totalGoodsCount);
					resultList.add(machine);
				}
			}
		}
		return ResultGenerator.genSuccessResult(resultList);
	}

	@Override
	public Result<List<Inno72Goods>> getGoodsByMachineId(String machineId) {
		if (StringUtil.isEmpty(machineId)) {
			return Results.failure("参数不能为空");
		}
		List<Inno72Goods> list = inno72GoodsMapper.selectByMachineId(machineId);
		return ResultGenerator.genSuccessResult(list);
	}

	@Override
	public Result<String> clearAll(String machineId) {
		if (StringUtil.isEmpty(machineId)) {
			return Results.failure("参数不能为空");
		}
		String userId = UserUtil.getUser().getId();
		String userName = UserUtil.getUser().getName();
		StringBuffer detail = new StringBuffer("");
		Inno72Machine machine = inno72MachineMapper.getMachineById(machineId);
		String machineCode = machine.getMachineCode();
		List<Inno72SupplyChannel> supplyChannelList = inno72SupplyChannelMapper.selectAllSupply(machineId);
		if (supplyChannelList != null && supplyChannelList.size() > 0) {
			StringBuffer idBuffer = new StringBuffer();
			String batchNo = StringUtil.getUUID();
			LocalDateTime now = LocalDateTime.now();
			int supplyFlag = 0;
			for(Inno72SupplyChannel supplyChannel:supplyChannelList){
				String goodsId = supplyChannel.getGoodsId();
				String code = supplyChannel.getCode();
				int beforeCount = supplyChannel.getGoodsCount();
				supplyChannel.setUpdateTime(now);
				supplyChannel.setGoodsCount(0);
				inno72SupplyChannelMapper.updateByPrimaryKeySelective(supplyChannel);
				idBuffer.append(supplyChannel.getId());
				idBuffer.append(",");
				if(StringUtil.isNotEmpty(goodsId)){
					Inno72SupplyChannelHistory history = new Inno72SupplyChannelHistory();
					history.setId(StringUtil.getUUID());
					history.setBeforeCount(beforeCount);
					history.setAfterCount(0);
					history.setBatchNo(batchNo);
					history.setSupplyChannelId(supplyChannel.getId());
					history.setMachineId(supplyChannel.getMachineId());
					history.setUserId(userId);
					history.setCreateTime(now);
					history.setType(1);
					history.setGoodsId(goodsId);
					int supplyCount = 0-beforeCount;
					history.setSupplyCount(supplyCount);
					history.setSupplyType(2);
					inno72SupplyChannelHistoryMapper.insertSelective(history);
					this.saveStatistic(machine,code,goodsId,beforeCount,0,userId);
					detail.append("为"+machineCode+"机器"+code+"货道补货，补货数量为"+supplyCount+"；");
					supplyFlag++;
				}
			}
			if(supplyFlag>0){
				this.saveHistoryOrder(batchNo,machineId,userId,now);
			}
			this.saveToLog(detail,userName,machineCode);
			inno72SupplyChannelGoodsMapper.deleteByIds(idBuffer.substring(0, idBuffer.length() - 1));
		}
		return ResultGenerator.genSuccessResult();
	}

	@Override
	public Result<String> supplyAll(String machineId) {
		if (StringUtil.isEmpty(machineId)) {
			return Results.failure("参数不能为空");
		}
		Map<String, Object> map = new HashMap<>();
		map.put("machineId", machineId);
		map.put("status", 0);
		String userId = UserUtil.getUser().getId();
		String userName = UserUtil.getUser().getName();
		StringBuffer detail = new StringBuffer("");
		Inno72Machine machine = inno72MachineMapper.getMachineById(machineId);
		String machineCode = machine.getMachineCode();
		List<Inno72SupplyChannel> list = inno72SupplyChannelMapper.selectListByParam(map);
		if (list != null && list.size() > 0) {
			int supplyFlag = 0;
			String batchNo = StringUtil.getUUID();
			LocalDateTime now = LocalDateTime.now();
			for (Inno72SupplyChannel supplyChannel : list) {
				String goodsId = supplyChannel.getGoodsId();
				if (StringUtil.isEmpty(goodsId)) {
					Inno72SupplyChannelHistory history = new Inno72SupplyChannelHistory();
					int beforeCount = supplyChannel.getGoodsCount();
					int afterCount = supplyChannel.getVolumeCount();
					String code = supplyChannel.getCode();
					history.setId(StringUtil.getUUID());
					history.setBeforeCount(beforeCount);
					history.setAfterCount(afterCount);
					history.setBatchNo(batchNo);
					history.setSupplyChannelId(supplyChannel.getId());
					history.setMachineId(supplyChannel.getMachineId());
					history.setUserId(userId);
					history.setCreateTime(now);
					history.setType(1);
					history.setGoodsId(goodsId);
					int supplyCount = afterCount-beforeCount;
					history.setSupplyCount(supplyCount);
					history.setSupplyType(1);
					supplyChannel.setGoodsCount(afterCount);
					supplyChannel.setUpdateTime(LocalDateTime.now());
					inno72SupplyChannelMapper.updateByPrimaryKeySelective(supplyChannel);
					inno72SupplyChannelHistoryMapper.insertSelective(history);
					this.saveStatistic(machine,code,goodsId,beforeCount,afterCount,userId);
					detail.append("为"+machineCode+"机器"+code+"货道补货，补货数量为"+supplyCount+"；");
					supplyFlag++;
				}
			}
			if(supplyFlag>0){
				this.saveHistoryOrder(batchNo,machineId,userId,now);
			}
			this.saveToLog(detail,userName,machineCode);
		}
		return ResultGenerator.genSuccessResult();
	}

	@Override
	public Result<String> submit(List<Map<String, Object>> mapList) {
		if (mapList == null || mapList.size() == 0) {
			return Results.failure("参数不能为空");
		}
		Inno72CheckUser checkUser = UserUtil.getUser();
		String userId = checkUser.getId();
		StringBuffer ids = new StringBuffer();
		String [] suuplyChannelArray = new String[mapList.size()];
		for(int i=0;i<mapList.size();i++){
			suuplyChannelArray[i]=mapList.get(i).get("id").toString();
		}
		List<Inno72SupplyChannel> supplyChannelList = inno72SupplyChannelMapper.selectSupplyAndGoods(suuplyChannelArray);
		if (supplyChannelList != null && supplyChannelList.size() > 0) {
			String machineId = supplyChannelList.get(0).getMachineId();
			Inno72Machine machine = inno72MachineMapper.getMachineById(machineId);
			String machineCode = machine.getMachineCode();
			String batchNo = StringUtil.getUUID();
			LocalDateTime now = LocalDateTime.now();
			StringBuffer detail = new StringBuffer("");
			int historyCount = 0;
			Map<String,Integer> checkGoodsCountMap = new HashMap<>();
			for(Inno72SupplyChannel supplyChannel:supplyChannelList){
				String supplyChannelId = supplyChannel.getId();
				for(Map<String, Object> map:mapList){
					String id = map.get("id").toString();
					String dataGoodsId = supplyChannel.getGoodsId();//数据库查到的商品ID
					if (id.equals(supplyChannelId)) {
						Object goodsCount = map.get("goodsCount");
						int afterGoodsCount = 0;
						if (goodsCount != null) {
							afterGoodsCount = Integer.parseInt(map.get("goodsCount").toString());
						}
						int beforeGoodsCount = supplyChannel.getGoodsCount();
						supplyChannel.setUpdateTime(now);
						Object goodsId = map.get("goodsId");
						String goodsIdStr = "";//APP传入
						if (goodsId != null) {
							goodsIdStr = map.get("goodsId").toString();
						}
						supplyChannel.setGoodsCount(afterGoodsCount);
						supplyChannel.setIsDelete(0);
						inno72SupplyChannelMapper.updateByPrimaryKeySelective(supplyChannel);
						int supplyCount = 0;
						String code = supplyChannel.getCode();
						if(StringUtil.isNotEmpty(dataGoodsId)){
							if(dataGoodsId.equals(goodsIdStr) && afterGoodsCount != beforeGoodsCount){
								if(afterGoodsCount>beforeGoodsCount){
									supplyCount = afterGoodsCount-beforeGoodsCount;
								}else {
									supplyCount = afterGoodsCount;
								}
								Inno72SupplyChannelHistory history = new Inno72SupplyChannelHistory();
								history.setId(StringUtil.getUUID());
								history.setBeforeCount(beforeGoodsCount);
								history.setAfterCount(afterGoodsCount);
								history.setBatchNo(batchNo);
								history.setSupplyChannelId(supplyChannelId);
								history.setMachineId(supplyChannel.getMachineId());
								history.setUserId(UserUtil.getUser().getId());
								history.setCreateTime(now);
								history.setType(1);
								history.setGoodsId(goodsIdStr);
								history.setSupplyCount(supplyCount);
								history.setSupplyType(1);
								inno72SupplyChannelHistoryMapper.insertSelective(history);
								this.setCheckGoodsNumMap(checkGoodsCountMap,goodsIdStr,supplyCount);
								this.saveStatistic(machine,code,goodsIdStr,beforeGoodsCount,afterGoodsCount,userId);
								detail.append("为"+machineCode+"机器"+code+"货道补货，补货数量为"+supplyCount+"；");
								historyCount++;
							}else if(!dataGoodsId.equals(goodsIdStr)){
								Condition condition = new Condition(Inno72SupplyChannelGoods.class);
								condition.createCriteria().andEqualTo("supplyChannelId", supplyChannel.getId());
								inno72SupplyChannelGoodsMapper.deleteByCondition(condition);
								Inno72SupplyChannelHistory history = new Inno72SupplyChannelHistory();
								history.setId(StringUtil.getUUID());
								history.setBeforeCount(beforeGoodsCount);
								history.setAfterCount(0);
								history.setBatchNo(batchNo);
								history.setSupplyChannelId(supplyChannelId);
								history.setMachineId(supplyChannel.getMachineId());
								history.setUserId(UserUtil.getUser().getId());
								history.setCreateTime(now);
								history.setType(1);
								history.setGoodsId(dataGoodsId);
								supplyCount = -beforeGoodsCount;
								history.setSupplyCount(supplyCount);
								history.setSupplyType(2);
								inno72SupplyChannelHistoryMapper.insertSelective(history);
								this.setCheckGoodsNumMap(checkGoodsCountMap,dataGoodsId,supplyCount);
								this.saveStatistic(machine,code,dataGoodsId,beforeGoodsCount,0,userId);
								detail.append("为"+machineCode+"机器"+code+"货道补货，补货数量为"+supplyCount+"；");
								historyCount++;
								if (StringUtil.isNotEmpty(goodsIdStr)) {
									Inno72SupplyChannelGoods goods = new Inno72SupplyChannelGoods();
									goods.setGoodsId(goodsIdStr);
									goods.setId(StringUtil.getUUID());
									goods.setSupplyChannelId(supplyChannelId);
									inno72SupplyChannelGoodsMapper.insertSelective(goods);
									Inno72SupplyChannelHistory newHistory = new Inno72SupplyChannelHistory();
									newHistory.setId(StringUtil.getUUID());
									newHistory.setBeforeCount(0);
									newHistory.setAfterCount(afterGoodsCount);
									newHistory.setBatchNo(batchNo);
									newHistory.setSupplyChannelId(supplyChannelId);
									newHistory.setMachineId(supplyChannel.getMachineId());
									newHistory.setUserId(UserUtil.getUser().getId());
									newHistory.setCreateTime(now);
									newHistory.setType(1);
									newHistory.setGoodsId(goodsIdStr);
									supplyCount = afterGoodsCount;
									newHistory.setSupplyCount(supplyCount);
									newHistory.setSupplyType(1);
									inno72SupplyChannelHistoryMapper.insertSelective(newHistory);
									this.setCheckGoodsNumMap(checkGoodsCountMap,goodsIdStr,supplyCount);
									this.saveStatistic(machine,code,goodsIdStr,0,afterGoodsCount,userId);
									detail.append("为"+machineCode+"机器"+code+"货道补货，补货数量为"+supplyCount+"；");
									historyCount++;
								}
							}
						}else if(StringUtil.isNotEmpty(goodsIdStr)){
							Condition condition = new Condition(Inno72SupplyChannelGoods.class);
							condition.createCriteria().andEqualTo("supplyChannelId", supplyChannel.getId());
							inno72SupplyChannelGoodsMapper.deleteByCondition(condition);
							Inno72SupplyChannelGoods goods = new Inno72SupplyChannelGoods();
							goods.setGoodsId(goodsIdStr);
							goods.setId(StringUtil.getUUID());
							goods.setSupplyChannelId(supplyChannelId);
							inno72SupplyChannelGoodsMapper.insertSelective(goods);
							Inno72SupplyChannelHistory history = new Inno72SupplyChannelHistory();
							history.setId(StringUtil.getUUID());
							history.setBeforeCount(0);
							history.setAfterCount(afterGoodsCount);
							history.setBatchNo(batchNo);
							history.setSupplyChannelId(supplyChannelId);
							history.setMachineId(supplyChannel.getMachineId());
							history.setUserId(UserUtil.getUser().getId());
							history.setCreateTime(now);
							history.setType(1);
							history.setGoodsId(goodsIdStr);
							supplyCount = afterGoodsCount;
							history.setSupplyCount(supplyCount);
							history.setSupplyType(1);
							inno72SupplyChannelHistoryMapper.insertSelective(history);
							this.setCheckGoodsNumMap(checkGoodsCountMap,goodsIdStr,supplyCount);
							this.saveStatistic(machine,code,goodsIdStr,0,afterGoodsCount,userId);
							detail.append("为"+machineCode+"机器"+code+"货道补货，补货数量为"+supplyCount+"；");
							historyCount++;
						}
					}
				}
			}

			for(String goodsKey:checkGoodsCountMap.keySet()){
				int goodsVelue = checkGoodsCountMap.get(goodsKey);
				if(goodsVelue != 0){
					this.saveGoodsNum(goodsKey,machine.getActivityId(),goodsVelue);
				}
			}
			if(historyCount>0){
				this.saveHistoryOrder(batchNo,machineId,checkUser.getId(),now);
			}
			if("".equals(detail)){
				detail.append("补货");
			}
			this.saveToLog(detail,checkUser.getName(),machineCode);
		}
		return ResultGenerator.genSuccessResult();
	}


	public Map<String,Integer> setCheckGoodsNumMap(Map<String,Integer> map,String goodsIdStr,int supplyCount){
		int checkGoodsCount = 0;
		if(map.containsKey(goodsIdStr)){
			checkGoodsCount = map.get(goodsIdStr);
			checkGoodsCount += supplyCount;
		}else{
			checkGoodsCount = supplyCount;
		}
		map.put(goodsIdStr,checkGoodsCount);
		return map;
	}


	public void saveGoodsNum(String goodsId,String activityId,int goodsCount){
		Map<String,Object> goodsMap = new HashMap<>();
		goodsMap.put("goodsId",goodsId);
		goodsMap.put("checkUserId",UserUtil.getUser().getId());
		//					goodsMap.put("activityId",activityId);
		Inno72CheckGoodsNum goodsNum = inno72CheckGoodsNumMapper.selectByparam(goodsMap);
		if(goodsNum != null){
			int receiveTotalCount = goodsNum.getReceiveTotalCount();
			int supplyTotalCount = goodsNum.getSupplyTotalCount();
			supplyTotalCount += goodsCount;
			int differTotalCount = receiveTotalCount-supplyTotalCount;
			goodsNum.setSupplyTotalCount(supplyTotalCount);
			goodsNum.setDifferTotalCount(differTotalCount);
			inno72CheckGoodsNumMapper.updateByPrimaryKeySelective(goodsNum);
		}else{
			int receiveTotalCount = 0;
			int supplyTotalCount = goodsCount;
			int differTotalCount = -supplyTotalCount;
			goodsNum = new Inno72CheckGoodsNum();
			goodsNum.setId(StringUtil.getUUID());
			goodsNum.setReceiveTotalCount(receiveTotalCount);
			goodsNum.setSupplyTotalCount(supplyTotalCount);
			goodsNum.setDifferTotalCount(differTotalCount);
			goodsNum.setGoodsId(goodsId);
			goodsNum.setCheckUserId(UserUtil.getUser().getId());
			goodsNum.setActivityId(activityId);
			inno72CheckGoodsNumMapper.insertSelective(goodsNum);
		}
		Inno72CheckGoodsDetail goodsDetail = new Inno72CheckGoodsDetail();
		goodsDetail.setGoodsNumId(goodsNum.getId());
		goodsDetail.setId(StringUtil.getUUID());
		goodsDetail.setReceiveCount(0);
		goodsDetail.setSupplyCount(goodsCount);
		goodsDetail.setDifferCount(-goodsCount);
		goodsDetail.setCreateTime(LocalDateTime.now());
		inno72CheckGoodsDetailMapper.insertSelective(goodsDetail);
	}

	/**
	 * 保存埋点
	 * @param detail
	 * @param userName
	 * @param machineCode
	 */
	public void saveToLog(StringBuffer detail,String userName,String machineCode){
		String detailStr = detail.toString();
		logger.info("补货添加日志：{}",detailStr);
		if(!detailStr.equals("")){
			logger.info("补货添加日志：{}","用户"+userName+detailStr);
			StringUtil.logger(CommonConstants.LOG_TYPE_MACHINE_SUPPLY,machineCode,"用户"+userName+"在巡检APP"+detailStr);
		}
	}


	/**
	 * 插入补货记录主表和快照
	 * @param batchNo
	 * @param machineId
	 * @param userId
	 * @param now
	 */
	public void saveHistoryOrder(String batchNo,String machineId,String userId,LocalDateTime now){
		Inno72SupplyChannelOrder order = new Inno72SupplyChannelOrder();
		order.setId(batchNo);
		order.setCreateTime(now);
		order.setMachineId(machineId);
		order.setType(1);
		order.setUserId(userId);
		inno72SupplyChannelOrderMapper.insertSelective(order);
		logger.info("插入补货记录主表",JSON.toJSONString(order));
		List<Inno72SupplyChannel> list = inno72SupplyChannelMapper.selectAllSupply(machineId);
		if(!list.isEmpty()){
			for(Inno72SupplyChannel c:list){
				SubmitSupplyChannel ssc = new SubmitSupplyChannel();
				BeanUtils.copyProperties(c,ssc,SubmitSupplyChannel.class);
				ssc.setSupplyChannelId(c.getId());
				ssc.setNowTime(LocalDateTime.now());
				ssc.setBatchNo(batchNo);
				mongoTpl.save(ssc,"SubmitSupplyChannel");
			}
		}
	}

	/**
	 * 插入BI
	 * @param machine
	 * @param code
	 * @param goodsId
	 * @param beforeNum
	 * @param afterNum
	 * @param userId
	 */
	public void saveStatistic(Inno72Machine machine,String code,String goodsId,int beforeNum,int afterNum,String userId){
		try{
			Map<String, Object> infos = new HashMap<>();
			infos.put("machineCode",machine.getMachineCode());
			infos.put("channelId",code);
			infos.put("localId",machine.getLocaleId());
			infos.put("userId",userId);
			infos.put("areaId",machine.getAreaCode());
			infos.put("goodsId",goodsId);
			infos.put("beforeNum",beforeNum);
			infos.put("afterNum",afterNum);
			infos.put("time",new Date());
			statistic.put(StatisticEnum.REPLENISHMENT,infos);
			logger.info("插入补货记录BI",JSON.toJSONString(infos));
		}catch (Exception e){
			e.printStackTrace();
		}

	}

	@Override
	public List<WorkOrderVo> findByPage(String keyword, String findTime) {
		Inno72CheckUser checkUser = UserUtil.getUser();
		String checkUserId = checkUser.getId();
		Map<String, Object> map = new HashMap<>();
		map.put("checkUserId", checkUserId);
		if (StringUtil.isNotEmpty(keyword) && StringUtil.isNotEmpty(keyword.trim())) {
			map.put("keyword", keyword.trim());
		}
		if (StringUtil.isNotEmpty(findTime) && StringUtil.isNotEmpty(findTime.trim())) {
			map.put("beginTime", findTime.trim() + " 00:00:00");
			map.put("endTime", findTime.trim() + " 23:59:59");
		}
		return inno72SupplyChannelOrderMapper.selectByPage(map);
	}

	@Override
	public Result<List<WorkOrderVo>> workOrderDetail(SupplyRequestVo vo) {
		Inno72CheckUser checkUser = UserUtil.getUser();
		String checkUserId = checkUser.getId();
		Map<String,Object> map = new HashMap<>();
		String machineId = vo.getMachineId();
		String findTime = vo.getFindTime();
		if(StringUtil.isEmpty(machineId) || StringUtil.isEmpty(findTime)){
			return Results.failure("参数缺失");
		}
		map.put("checkUserId",checkUserId);
		map.put("machineId",machineId);
		map.put("findTime",findTime);
		List<WorkOrderVo> list = inno72SupplyChannelOrderMapper.selectWorkOrderDetail(map);
		return ResultGenerator.genSuccessResult(list);
	}

	@Override
	public void findAndPushByTaskParam() {
		List<Inno72SupplyChannel> list = inno72SupplyChannelMapper.selectTask();
		if (list != null && list.size() > 0) {
			AlarmMessageBean alarmMessageBean = new AlarmMessageBean();
			alarmMessageBean.setSystem("machineLackGoods");
			alarmMessageBean.setType("machineLackGoodsException");
			for (Inno72SupplyChannel supplyChannel : list) {
				int totalCount = supplyChannel.getGoodsCount();
				if(totalCount<5){
					ChannelGoodsAlarmBean alarmBean = new ChannelGoodsAlarmBean();
					alarmBean.setGoodsName(supplyChannel.getGoodsName());
					alarmBean.setMachineCode(supplyChannel.getMachineCode());
					alarmBean.setSurPlusNum(supplyChannel.getGoodsCount());
					alarmBean.setLocaleStr(supplyChannel.getLocaleStr());
					alarmMessageBean.setData(alarmBean);
					logger.info("货道缺货发送push{}", JSONObject.toJSONString(alarmMessageBean));
					redisUtil.publish("moniterAlarm", JSONObject.toJSONString(alarmMessageBean));
				}
			}
		}
	}

	@Override
	public Result<List<WorkOrderVo>> findOrderByMonth(SupplyRequestVo vo) {
		Inno72CheckUser checkUser = UserUtil.getUser();
		String checkUserId = checkUser.getId();
		String machineId = vo.getMachineId();
		String findTime = vo.getFindTime();
		if(StringUtil.isEmpty(machineId) || StringUtil.isEmpty(findTime)){
			return Results.failure("参数缺失");
		}
		Map<String,Object> map = new HashMap<>();
		map.put("checkUserId",checkUserId);
		map.put("machineId",machineId);
		map.put("findTime",findTime);
		List<WorkOrderVo> list = inno72SupplyChannelOrderMapper.selectOrderByMonth(map);
		List<WorkOrderVo> resultList = new ArrayList<>();
		LocalDate localDate = DateUtil.toDate(findTime,DateUtil.DF_ONLY_YMD_S1);
		List<String> dateList = DateUtil.getMonthFullDay(localDate);

		Map<String,WorkOrderVo> workOrderMap = new HashMap<>();
		for(WorkOrderVo orderVo:list){
			String date = orderVo.getCreateDate();
			if(!workOrderMap.containsKey(date)){
				workOrderMap.put(date,orderVo);
			}
		}
		for(String key:dateList){
			WorkOrderVo workOrderVo = null;
			if(!workOrderMap.containsKey(key)){
				workOrderVo = new WorkOrderVo();
				workOrderVo.setCreateDate(key);
				workOrderVo.setSupplyFlag(0);
			}else{
				workOrderVo = workOrderMap.get(key);
				workOrderVo.setSupplyFlag(1);
			}
			workOrderVo.setMachineId(machineId);
			resultList.add(workOrderVo);
		}
		return ResultGenerator.genSuccessResult(resultList);
	}

	@Override
	public Result<List<Inno72SupplyChannel>> exceptionList(SupplyRequestVo vo) {
		List<Inno72SupplyChannel> list = inno72SupplyChannelMapper.selectExceptionList(vo.getMachineId());
		return ResultGenerator.genSuccessResult(list);
	}

	@Override
	public Result<String> openSupplyChannel(SupplyRequestVo vo) {
		String machineId = vo.getMachineId();
		if(StringUtil.isEmpty(machineId)){
			return Results.failure("参数缺失");
		}
		Inno72Machine machine = inno72MachineMapper.selectByPrimaryKey(machineId);
		String machineCode = machine.getMachineCode();
		String userName = UserUtil.getUser().getName();
		Map<String,Object> map = new HashMap<>();
		map.put("machineId",machineId);
		String userId = UserUtil.getUser().getId();
		map.put("updateId",userId);
		inno72SupplyChannelMapper.updateOpen(map);
		StringUtil.logger(CommonConstants.LOG_TYPE_ENABLE_CHANNEL,machine.getMachineCode(),"用户"+userName+"在巡检APP启用了货道");
		logger.info("用户"+userName+"对"+machineCode+"机器启用了货道");
		return ResultGenerator.genSuccessResult();
	}

	@Override
	public Result<String> updateSupplyChannel(Map<String, Object> map) {
		Inno72CheckUser checkUser = UserUtil.getUser();
		String machineCode = (String) map.get("machineCode");
		Inno72Machine machine = inno72MachineMapper.getMachineByCode(machineCode);
		List<String> paramList = (List<String>) map.get("list");
		Map<String, Object> param = new HashMap<>();
		param.put("machineCode", machineCode);
		List<Inno72SupplyChannel> list = inno72SupplyChannelMapper.selectListByParam(param);
		Map<String,Inno72SupplyChannel> supplyChannelMap = new HashMap<>();
		if(!list.isEmpty()){
			for(Inno72SupplyChannel sc:list){
				supplyChannelMap.put(sc.getCode(),sc);
			}
		}
		if(!paramList.isEmpty()){
			for(String supplyCode :paramList){
				if(!supplyChannelMap.containsKey(supplyCode)){
					int codeInt = Integer.parseInt(supplyCode);
					int rowNo = codeInt/10;
					rowNo++;
					Map<String,Object> detailMap = new HashMap<>();
					detailMap.put("rowNo",rowNo);
					detailMap.put("machineCode",machineCode);
					Inno72MachineBatchDetail batchDetail = inno72MachineBatchDetailMapper.selectByParam(detailMap);
					if(batchDetail != null){
						Map<String,Object> paramMap = new HashMap<>();
						paramMap.put("machineId",machine.getId());
						paramMap.put("code",supplyCode);
						Inno72SupplyChannel supplyChannel = inno72SupplyChannelMapper.selectOneBy(paramMap);
						if(supplyChannel==null){
							supplyChannel = new Inno72SupplyChannel();
							supplyChannel.setId(StringUtil.getUUID());
							supplyChannel.setMachineId(machine.getId());
							supplyChannel.setCode(supplyCode);
							supplyChannel.setName("货道"+supplyCode);
							supplyChannel.setStatus(0);
							supplyChannel.setVolumeCount(batchDetail.getVolumeCount());
							if(checkUser != null){
								supplyChannel.setCreateId(checkUser.getId());
								supplyChannel.setUpdateId(checkUser.getId());
							}else{
								supplyChannel.setCreateId("系统");
								supplyChannel.setUpdateId("系统");
							}
							supplyChannel.setCreateTime(LocalDateTime.now());
							supplyChannel.setUpdateTime(LocalDateTime.now());
							supplyChannel.setIsDelete(0);
							supplyChannel.setIsRemove(0);
							supplyChannel.setWorkStatus(0);
							inno72SupplyChannelMapper.insertSelective(supplyChannel);
						}else{
							supplyChannel.setStatus(0);
							supplyChannel.setVolumeCount(batchDetail.getVolumeCount());
							if(checkUser != null){
								supplyChannel.setCreateId(checkUser.getId());
								supplyChannel.setUpdateId(checkUser.getId());
							}else{
								supplyChannel.setCreateId("系统");
								supplyChannel.setUpdateId("系统");
							}
							supplyChannel.setCreateTime(LocalDateTime.now());
							supplyChannel.setUpdateTime(LocalDateTime.now());
							supplyChannel.setIsDelete(0);
							supplyChannel.setWorkStatus(0);
							supplyChannel.setIsRemove(0);
							inno72SupplyChannelMapper.updateByPrimaryKeySelective(supplyChannel);
						}
					}
				}
			}
			for(String key:supplyChannelMap.keySet()){
				if(!paramList.contains(key)){
					Inno72SupplyChannel supplyChannel = new Inno72SupplyChannel();
					supplyChannel.setMachineId(machine.getId());
					supplyChannel.setCode(key);
					supplyChannel.setIsRemove(1);
					inno72SupplyChannelMapper.updateByParam(supplyChannel);
				}
			}
		}else {
			Results.failure("参数不合法");
		}


		return ResultGenerator.genSuccessResult();
	}

	@Override
	public Result<Boolean> supplyCheck(List<Map<String, Object>> mapList) {
		if (mapList == null || mapList.size() == 0) {
			return Results.failure("参数不能为空");
		}
		Inno72CheckUser checkUser = UserUtil.getUser();
		String [] suuplyChannelArray = new String[mapList.size()];
		for(int i=0;i<mapList.size();i++){
			suuplyChannelArray[i]=mapList.get(i).get("id").toString();
		}
		List<Inno72SupplyChannel> supplyChannelList = inno72SupplyChannelMapper.selectSupplyAndGoods(suuplyChannelArray);
		if (supplyChannelList != null && supplyChannelList.size() > 0) {
			String machineId = supplyChannelList.get(0).getMachineId();
			Inno72Machine machine = inno72MachineMapper.getMachineById(machineId);
			Map<String,Integer> checkGoodsCountMap = new HashMap<>();
			for(Inno72SupplyChannel supplyChannel:supplyChannelList){
				String supplyChannelId = supplyChannel.getId();
				for(Map<String, Object> map:mapList){
					String id = map.get("id").toString();
					String dataGoodsId = supplyChannel.getGoodsId();//数据库查到的商品ID
					if (id.equals(supplyChannelId)) {
						Object goodsCount = map.get("goodsCount");
						int afterGoodsCount = 0;
						if (goodsCount != null) {
							afterGoodsCount = Integer.parseInt(map.get("goodsCount").toString());
						}
						int beforeGoodsCount = supplyChannel.getGoodsCount();
						Object goodsId = map.get("goodsId");
						String goodsIdStr = "";//APP传入
						if (goodsId != null) {
							goodsIdStr = map.get("goodsId").toString();
						}
						int supplyCount = 0;
						if(StringUtil.isNotEmpty(dataGoodsId)){
							if(dataGoodsId.equals(goodsIdStr) && afterGoodsCount != beforeGoodsCount){
								if(afterGoodsCount>beforeGoodsCount){
									supplyCount = afterGoodsCount-beforeGoodsCount;
								}else {
									supplyCount = afterGoodsCount;
								}
								this.setCheckGoodsNumMap(checkGoodsCountMap,goodsIdStr,supplyCount);
							}else if(!dataGoodsId.equals(goodsIdStr)){
								supplyCount = -beforeGoodsCount;
								this.setCheckGoodsNumMap(checkGoodsCountMap,goodsIdStr,supplyCount);
								if (StringUtil.isNotEmpty(goodsIdStr)) {
									supplyCount = afterGoodsCount;
									this.setCheckGoodsNumMap(checkGoodsCountMap,goodsIdStr,supplyCount);
								}
							}
						}else if(StringUtil.isNotEmpty(goodsIdStr)){
							supplyCount = afterGoodsCount;
							this.setCheckGoodsNumMap(checkGoodsCountMap,goodsIdStr,supplyCount);
						}
					}
				}
			}

			for(String goodsKey:checkGoodsCountMap.keySet()){
				int goodsVelue = checkGoodsCountMap.get(goodsKey);
				if(goodsVelue != 0){
					Map<String,Object> goodsMap = new HashMap<>();
					goodsMap.put("goodsId",goodsKey);
					goodsMap.put("checkUserId",checkUser.getId());
					goodsMap.put("activityId",machine.getActivityId());
					Inno72CheckGoodsNum goodsNum = inno72CheckGoodsNumMapper.selectByparam(goodsMap);
					if(goodsNum != null){
						int receiveTotalCount = goodsNum.getReceiveTotalCount();
						int supplyTotalCount = goodsNum.getSupplyTotalCount();
						supplyTotalCount += goodsVelue;
						int differTotalCount = receiveTotalCount-supplyTotalCount;
						if(differTotalCount<0){
							return ResultGenerator.genSuccessResult(false);
						}
					}else{
						return ResultGenerator.genSuccessResult(false);
					}
				}
			}
		}
		return ResultGenerator.genSuccessResult(true);
	}

	public void addSupplyChannelToMongo(Inno72SupplyChannel supplyChannel) {
		DBCollection dbCollection = mongoTpl.getCollection("supplyChannel");
		if (dbCollection == null) {
			mongoTpl.createCollection("supplyChannel");
		}
		supplyChannel.setId(null);
		mongoTpl.save(supplyChannel, "supplyChannel");
	}

}
