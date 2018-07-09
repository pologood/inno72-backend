package com.inno72.machine.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.inno72.common.*;
import com.inno72.machine.mapper.Inno72SupplyChannelGoodsMapper;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
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

    Logger logger = LoggerFactory.getLogger(this.getClass());
    @Resource
	private Inno72SupplyChannelMapper inno72SupplyChannelMapper;

	@Resource
	private SupplyChannelDictService supplyChannelDictService;

	@Resource
	private Inno72SupplyChannelGoodsMapper inno72SupplyChannelGoodsMapper;

	@Autowired
	private MongoOperations mongoTpl;

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
		supplyChannel.setUpdateTime(LocalDateTime.now());
		inno72SupplyChannelMapper.subCount(supplyChannel);
		supplyChannel = inno72SupplyChannelMapper.selectByParam(map);
		supplyChannel.setRemark("货道商品数量减一");
		addSupplyChannelToMongo(supplyChannel);
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
					supplyChannel.setCreateTime(LocalDateTime.now());
					supplyChannel.setUpdateTime(LocalDateTime.now());
					supplyChannelList.add(supplyChannel);
					supplyChannel.setRemark("初始化货道");
					addSupplyChannelToMongo(supplyChannel);
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
				channel.setUpdateTime(LocalDateTime.now());
				inno72SupplyChannelMapper.updateByParam(channel);
				channel.setRemark("合并货道，由"+fromCode+"合并到"+toCode);
				addSupplyChannelToMongo(channel);
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
				Map<String,Object> childMap = new HashMap<>();
				childMap.put("machineId",machineId);
				childMap.put("parentCode",code);
				Inno72SupplyChannel childChannel = inno72SupplyChannelMapper.selectByParentCode(childMap);
				childChannel.setParentCode(code);
				childChannel.setStatus(0);
				childChannel.setUpdateTime(LocalDateTime.now());
				int count = inno72SupplyChannelMapper.updateChild(childChannel);
				if(count == 1){
					childChannel.setRemark("拆分货道，将货道"+code+"和货道"+childChannel.getCode()+"拆分");
					addSupplyChannelToMongo(childChannel);
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

	/**
	 * 货道清零
	 * @param supplyChannel
	 * @return
	 */
	@Override
	public Result<String> clear(Inno72SupplyChannel supplyChannel) {
		String[] codes = supplyChannel.getCodes();
		String machineId = supplyChannel.getMachineId();
		if(codes == null || StringUtil.isEmpty(machineId)){
			return ResultGenerator.genFailResult("参数有误");
		}
		supplyChannel.setGoodsCount(0);
		Map<String,Object> map = new HashMap<>();
		map.put("codes",codes);
		map.put("machineId",machineId);
		supplyChannel.setUpdateTime(LocalDateTime.now());
		inno72SupplyChannelMapper.updateListByParam(supplyChannel);
		List<Inno72SupplyChannel> list = inno72SupplyChannelMapper.selectListByParam(map);
		for(Inno72SupplyChannel channel:list){
			channel.setRemark("货道商品数量清零");
			addSupplyChannelToMongo(channel);
		}
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
		for(Inno72SupplyChannel channel:supplyChannelList){
			channel.setIsDelete(1);
			channel.setRemark("货道商品全部下架");
			channel.setUpdateTime(LocalDateTime.now());
		}
		return ResultGenerator.genSuccessResult();
	}

    @Override
    public Result<List<Inno72SupplyChannel>> history(Inno72SupplyChannel supplyChannel) {
	    String machineId = supplyChannel.getMachineId();
	    String code = supplyChannel.getCode();
	    if(StringUtil.isEmpty(machineId)){
	        return Results.failure("机器ID不能为空");
        }
        DBCollection coll = mongoTpl.getCollection("supplyChannel");
        BasicDBObject dbObject = new BasicDBObject();
        dbObject.put("machineId",machineId);
        if(StringUtil.isNotEmpty(code)){
            dbObject.put("code",code);
        }
        DBCursor dbCursor = coll.find(dbObject).limit(10).sort(new BasicDBObject("updateTime",-1));
        if(dbCursor != null){
            List<DBObject> list = dbCursor.toArray();
            if(list != null && list.size()>0){
                List<Inno72SupplyChannel> supplyChannelList = new ArrayList<>();
                for(DBObject db:list){
                    Inno72SupplyChannel inno72SupplyChannel = new Inno72SupplyChannel();
                    if(db.get("remark") != null){
                    	  inno72SupplyChannel.setRemark(db.get("remark").toString());
					}
                    inno72SupplyChannel.setMachineId(db.get("machineId").toString());
                    if(db.get("code") != null){
                    	inno72SupplyChannel.setCode(db.get("code").toString());
					}
                    if(db.get("goodsName") != null){
                    	inno72SupplyChannel.setGoodsName(db.get("goodsName").toString());
					}
                    if(db.get("goodsCode") != null){
                    	inno72SupplyChannel.setGoodsCode(db.get("goodsCode").toString());
					}

                    if(db.get("status") != null){
                        inno72SupplyChannel.setStatus(Integer.parseInt(db.get("status").toString()));
                    }
                    inno72SupplyChannel.setIsDelete(Integer.parseInt(db.get("isDelete").toString()));
                    JSONObject jsonObject = JSON.parseObject(db.toString());
                    JSONObject updateTimeObject = jsonObject.getJSONObject("updateTime");
                    JSONObject createTimeObject = jsonObject.getJSONObject("createTime");
                    String createTime = createTimeObject.getString("$date");
                    String updateTime = updateTimeObject.getString("$date");
                    if(StringUtil.isNotEmpty(createTime)){
                    	 createTime = createTime.replace("T"," ");
                    	 createTime = createTime.substring(0,createTime.indexOf("."));
                    	 logger.info("date:"+updateTime);
                    	 inno72SupplyChannel.setCreateTime(LocalDateTime.parse(createTime,DateUtil.DF_FULL_S1));
					}
                    if(StringUtil.isNotEmpty(updateTime)){
                        updateTime = updateTime.replace("T"," ");
                        updateTime = updateTime.substring(0,updateTime.indexOf("."));
                        logger.info("date:"+updateTime);
                        inno72SupplyChannel.setUpdateTime(LocalDateTime.parse(updateTime,DateUtil.DF_FULL_S1));
                    }
                    supplyChannelList.add(inno72SupplyChannel);
                }
                return ResultGenerator.genSuccessResult(supplyChannelList);
            }
        }
	    return null;
    }

    public void addSupplyChannelToMongo(Inno72SupplyChannel supplyChannel){
		DBCollection dbCollection = mongoTpl.getCollection("supplyChannel");
		if(dbCollection == null){
			mongoTpl.createCollection("supplyChannel");
		}
		supplyChannel.setId(null);
		mongoTpl.save(supplyChannel,"supplyChannel");
	}
}
