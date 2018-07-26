package com.inno72.machine.service.impl;

import java.time.LocalDateTime;
import java.util.*;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSONObject;
import com.inno72.check.model.Inno72CheckUser;
import com.inno72.common.*;
import com.inno72.machine.mapper.*;
import com.inno72.machine.model.*;
import com.inno72.machine.service.SupplyChannelService;
import com.inno72.machine.vo.SupplyChannelVo;
import com.inno72.machine.vo.WorkOrderVo;
import com.inno72.model.AlarmMessageBean;
import com.inno72.model.ChannelGoodsAlarmBean;
import com.inno72.redis.IRedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.inno72.utils.page.Pagination;
import com.mongodb.DBCollection;

import tk.mybatis.mapper.entity.Condition;

/**
 * Created by CodeGenerator on 2018/07/04.
 */
@Service
@Transactional
public class SupplyChannelServiceImpl extends AbstractService<Inno72SupplyChannel>
        implements SupplyChannelService {

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



    @Override
    public Result<String> init(String machineId, List<Inno72SupplyChannel> channels) {
        if (channels == null) {
            return Results.failure("货道信息错误");
        }
        Condition condition = new Condition(Inno72SupplyChannel.class);
        condition.createCriteria().andEqualTo("machineId", machineId);
        List<Inno72SupplyChannel> supplyChannelList = inno72SupplyChannelMapper.selectByCondition(condition);
        if (supplyChannelList != null) {
            for (Inno72SupplyChannel channel : channels) {
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
    public Result<String> merge(Inno72SupplyChannel supplyChannel) {
        String code = supplyChannel.getCode();
        String machineId = supplyChannel.getMachineId();
        if (StringUtil.isEmpty(code)|| StringUtil.isEmpty(machineId)) {
            return ResultGenerator.genFailResult("参数有误");
        }
        Integer codeInt = Integer.parseInt(code);
        Map<String, Object> map = new HashMap<>();
        map.put("machineId", machineId);
        Integer[] codes = new Integer[2];
        codes[0] = codeInt;
        Integer childCode = null;
        Integer parentCode;
        if (codeInt % 2 == 0 ) {
            codes[1] = (codeInt - 1);
            childCode = codes[0];
            parentCode = codes[1];
        }else{
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
            inno72SupplyChannelMapper.updateByParam(parentChannel);
            Inno72SupplyChannel channel = new Inno72SupplyChannel();
            channel.setCode(childCode.toString());
            channel.setMachineId(machineId);
            channel.setParentCode(parentCode.toString());
            channel.setStatus(1);
            channel.setUpdateTime(LocalDateTime.now());
            channel.setGoodsCount(0);
            inno72SupplyChannelMapper.updateByParam(channel);
            for(Inno72SupplyChannel supply:list){
                Condition condition = new Condition(Inno72SupplyChannelGoods.class);
                condition.createCriteria().andEqualTo("supplyChannelId",supply.getId());
                inno72SupplyChannelGoodsMapper.deleteByCondition(condition);
            }
            channel.setRemark("合并货道，由" + childCode + "合并到" + parentCode);
            addSupplyChannelToMongo(channel);
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult("货道数据有误");
        }
    }

    @Override
    public Result<String> split(Inno72SupplyChannel supplyChannel) {
        String code = supplyChannel.getCode();
        String machineId = supplyChannel.getMachineId();
        if (StringUtil.isEmpty(code) || StringUtil.isEmpty(machineId)) {
            return ResultGenerator.genFailResult("参数有误");
        }
        Map<String, Object> map = new HashMap<>();
        map.put("machineId", machineId);
        map.put("code", code);
        supplyChannel = inno72SupplyChannelMapper.selectByParam(map);
        if (supplyChannel != null) {
            supplyChannel.setGoodsCount(0);
            supplyChannel.setUpdateTime(LocalDateTime.now());
            inno72SupplyChannelMapper.updateByPrimaryKeySelective(supplyChannel);
            Condition condition = new Condition(Inno72SupplyChannelGoods.class);
            condition.createCriteria().andEqualTo("supplyChannelId",supplyChannel.getId());
            inno72SupplyChannelGoodsMapper.deleteByCondition(condition);
            Map<String, Object> childMap = new HashMap<>();
            childMap.put("machineId", machineId);
            childMap.put("parentCode", code);
            Inno72SupplyChannel childChannel = inno72SupplyChannelMapper.selectByParentCode(childMap);
            if(childChannel != null){
                childChannel.setParentCode(code);
                childChannel.setStatus(0);
                childChannel.setGoodsCount(0);
                childChannel.setUpdateTime(LocalDateTime.now());
                int count = inno72SupplyChannelMapper.updateChild(childChannel);
                Condition childCon = new Condition(Inno72SupplyChannelGoods.class);
                childCon.createCriteria().andEqualTo("supplyChannelId",supplyChannel.getId());
                inno72SupplyChannelGoodsMapper.deleteByCondition(childCon);
                if (count == 1) {
                    childChannel.setRemark("拆分货道，将货道" + code + "和货道" + childChannel.getCode() + "拆分");
                    addSupplyChannelToMongo(childChannel);
                    return ResultGenerator.genSuccessResult();
                } else {
                    return ResultGenerator.genFailResult("货道未合并不能拆分");
                }
            }else{
                return ResultGenerator.genFailResult("货道未合并不能拆分");
            }
        } else {
            return ResultGenerator.genFailResult("操作货道有误");
        }
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
        List<Inno72SupplyChannel> supplyChannelList = mongoTpl.find(query, Inno72SupplyChannel.class,
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
    public List<Inno72SupplyChannel> getListForPage(Inno72SupplyChannel supplyChannel) {
        return inno72SupplyChannelMapper.selectListForPage(supplyChannel);
    }

    @Override
    public List<Inno72SupplyChannel> getList(String machineId) {
        Condition condition = new Condition(Inno72SupplyChannel.class);
        condition.createCriteria().andEqualTo("machineId",machineId).andEqualTo("status",0);
        condition.orderBy("code");
        Map<String,Object> map= new HashMap<>();
        map.put("machineId",machineId);
        map.put("status",0);
        return inno72SupplyChannelMapper.selectListByParam(map);
    }

    @Override
    public List<Inno72Machine> getMachineLackGoods() {
        Inno72CheckUser checkUser = UserUtil.getUser();
        String checkUserId = checkUser.getId();
        List<Inno72Machine> machineList = inno72MachineMapper.getMachine(checkUserId);
        if(machineList != null && machineList.size()>0){
            for(Inno72Machine machine:machineList){
                Integer lackGoodsStatus = 0;
                List<SupplyChannelVo> supplyChannelVoList = machine.getSupplyChannelVoList();
                if(supplyChannelVoList != null && supplyChannelVoList.size()>0){
                    for(SupplyChannelVo supplyChannelVo:supplyChannelVoList){
                        int volumeCount = supplyChannelVo.getVolumeCount();
                        int goodsCount = supplyChannelVo.getGoodsCount();
                        if(goodsCount/volumeCount<0.2){
                            lackGoodsStatus = 1;
                            break;
                        }
                    }
                }
                machine.setLackGoodsStatus(lackGoodsStatus);
                machine.setSupplyChannelVoList(null);
            }
        }
        return machineList;
    }

    @Override
    public List<Inno72Goods> getGoodsLack() {
        Inno72CheckUser checkUser = UserUtil.getUser();
        String checkUserId = checkUser.getId();
        List<Inno72Goods> inno72GoodsList = inno72GoodsMapper.getLackGoods(checkUserId);
        List<Inno72Goods> resultList = new ArrayList<>();
        if(inno72GoodsList != null && inno72GoodsList.size()>0){
            for(Inno72Goods inno72Goods:inno72GoodsList){
                List<SupplyChannelVo> supplyChannelVoList = inno72Goods.getSupplyChannelVoList();
                if(supplyChannelVoList != null && supplyChannelVoList.size()>0){
                    int totalVolumeCount = 0;
                    int totalGoodsCount = 0;
                    for(SupplyChannelVo supplyChannelVo:supplyChannelVoList){
                        int volumeCount = supplyChannelVo.getVolumeCount();
                        int goodsCount = supplyChannelVo.getGoodsCount();
                        totalVolumeCount+=volumeCount;
                        totalGoodsCount+=goodsCount;
                    }
                    if(totalGoodsCount/totalVolumeCount<0.2){
                        inno72Goods.setLackGoodsStatus(1);
                        inno72Goods.setLackGoodsCount(totalVolumeCount-totalGoodsCount);
                        inno72Goods.setSupplyChannelVoList(null);
                        resultList.add(inno72Goods);
                    }
                }
            }
        }
        return resultList;
    }

    @Override
    public List<Inno72Machine> getMachineByLackGoods(String goodsId) {
        Inno72CheckUser checkUser = UserUtil.getUser();
        String checkUserId = checkUser.getId();
        List<Inno72Machine> machineList = inno72MachineMapper.getMachineByLackGoods(checkUserId,goodsId);
        List<Inno72Machine> resultList = new ArrayList<>();
        if(machineList != null && machineList.size()>0){
            for(Inno72Machine machine: machineList){
                List<SupplyChannelVo> supplyChannelVoList = machine.getSupplyChannelVoList();
                int totalVolumeCount = 0;
                int totalGoodsCount = 0;
                for(SupplyChannelVo supplyChannelVo:supplyChannelVoList){
                    int volumeCount = supplyChannelVo.getVolumeCount();
                    int goodsCount = supplyChannelVo.getGoodsCount();
                    totalVolumeCount+=volumeCount;
                    totalGoodsCount+=goodsCount;
                }
                if(totalVolumeCount-totalGoodsCount>0){
                    machine.setLackGoodsCount(totalVolumeCount-totalGoodsCount);
                    machine.setSupplyChannelVoList(null);
                    resultList.add(machine);
                }

            }
        }
        return resultList;
    }

    @Override
    public List<Inno72Goods> getGoodsByMachineId(String machineId) {

        return inno72GoodsMapper.selectByMachineId(machineId);
    }

    @Override
    public Result<String> clearAll(String machineId) {
        Condition condition = new Condition(Inno72SupplyChannel.class);
        condition.createCriteria().andEqualTo("machineId",machineId);
        List<Inno72SupplyChannel> supplyChannelList = inno72SupplyChannelMapper.selectByCondition(condition);
        if(supplyChannelList != null && supplyChannelList.size()>0){
            StringBuffer idBuffer= new StringBuffer();
            supplyChannelList.forEach(supplyChannel -> {
                supplyChannel.setUpdateTime(LocalDateTime.now());
                supplyChannel.setGoodsCount(0);
                inno72SupplyChannelMapper.updateByPrimaryKeySelective(supplyChannel);
                idBuffer.append(supplyChannel.getId());
                idBuffer.append(",");
            });
            inno72SupplyChannelGoodsMapper.deleteByIds(idBuffer.substring(0,idBuffer.length()-1));
        }
        return ResultGenerator.genSuccessResult();
    }

    @Override
    public Result<String> supplyAll(String machineId) {
        Map<String,Object> map = new HashMap<>();
        map.put("machineId",machineId);
        map.put("status",0);
        List<Inno72SupplyChannel> list = inno72SupplyChannelMapper.selectListByParam(map);
        if(list != null && list.size()>0){
            String batchNo = StringUtil.getUUID();
            List<Inno72SupplyChannelHistory> historyList = new ArrayList<>();
            LocalDateTime now = LocalDateTime.now();
            for(Inno72SupplyChannel supplyChannel:list){
                String goodsId = supplyChannel.getGoodsId();
                if(StringUtil.isEmpty(goodsId)){
                    Inno72SupplyChannelHistory history = new Inno72SupplyChannelHistory();
                    history.setId(StringUtil.getUUID());
                    history.setBeforeCount(supplyChannel.getGoodsCount());
                    history.setAfterCount(supplyChannel.getVolumeCount());
                    history.setBatchNo(batchNo);
                    history.setMachineId(supplyChannel.getMachineId());
                    history.setUserId("");
                    history.setCreateTime(now);
                    historyList.add(history);
                    supplyChannel.setGoodsCount(supplyChannel.getVolumeCount());
                    supplyChannel.setUpdateTime(LocalDateTime.now());
                    inno72SupplyChannelMapper.updateByPrimaryKeySelective(supplyChannel);
                }
            }
            inno72SupplyChannelHistoryMapper.insertList(historyList);
        }

        return ResultGenerator.genSuccessResult();
    }

    @Override
    public Result<String> submit(List<Map<String,Object>> mapList) {
        LocalDateTime now = LocalDateTime.now();
        if(mapList != null && mapList.size()>0){
            String batchNo = StringUtil.getUUID();
            mapList.forEach(map -> {
                Inno72SupplyChannel supplyChannel = new Inno72SupplyChannel();
                supplyChannel.setId(map.get("id").toString());
                supplyChannel.setGoodsId(map.get("goodsId").toString());
                supplyChannel.setGoodsCount(Integer.parseInt(map.get("goodsCount").toString()));
                Inno72SupplyChannel oldSupply = inno72SupplyChannelMapper.selectByPrimaryKey(supplyChannel.getId());
                supplyChannel.setUpdateTime(LocalDateTime.now());
                inno72SupplyChannelMapper.updateByPrimaryKeySelective(supplyChannel);
                Inno72SupplyChannelGoods goods = new Inno72SupplyChannelGoods();
                goods.setGoodsId(supplyChannel.getGoodsId());
                goods.setId(StringUtil.getUUID());
                goods.setSupplyChannelId(supplyChannel.getId());
                Condition condition = new Condition(Inno72SupplyChannelGoods.class);
                condition.createCriteria().andEqualTo("supplyChannelId", supplyChannel.getId());
                inno72SupplyChannelGoodsMapper.deleteByCondition(condition);
                inno72SupplyChannelGoodsMapper.insertSelective(goods);
                Inno72SupplyChannelHistory history = new Inno72SupplyChannelHistory();
                history.setId(StringUtil.getUUID());
                history.setBeforeCount(oldSupply.getGoodsCount());
                history.setAfterCount(supplyChannel.getGoodsCount());
                history.setBatchNo(batchNo);
                history.setSupplyChannelId(supplyChannel.getId());
                history.setMachineId(oldSupply.getMachineId());
                history.setUserId(UserUtil.getUser().getId());
                history.setCreateTime(now);
                inno72SupplyChannelHistoryMapper.insertSelective(history);
            });
        }
        return ResultGenerator.genSuccessResult();
    }

    @Override
    public List<WorkOrderVo> workOrderList(String keyword,String findTime) {
        Inno72CheckUser checkUser = UserUtil.getUser();
        String checkUserId = checkUser.getId();
        Map<String,Object> map = new HashMap<>();
        map.put("checkUserId",checkUserId);
        if(StringUtil.isNotEmpty(keyword) && StringUtil.isNotEmpty(keyword.trim())){
            map.put("keyword",keyword.trim());
        }
        if(StringUtil.isNotEmpty(findTime) && StringUtil.isNotEmpty(findTime.trim())){
            map.put("beginTime",findTime.trim()+" 00:00:00");
            map.put("endTime",findTime.trim()+" 23:59:59");
        }
        return inno72SupplyChannelHistoryMapper.getWorkOrderVoList(map);
    }

    @Override
    public Result<WorkOrderVo> workOrderDetail(String machineId, String batchNo) {
        Inno72CheckUser checkUser = UserUtil.getUser();
        String checkUserId = checkUser.getId();
        Map<String,Object> map = new HashMap<>();
        map.put("checkUserId",checkUserId);
        map.put("machineId",machineId);
        map.put("batchNo",batchNo);
        WorkOrderVo workOrderVo = new WorkOrderVo();
        List<Inno72SupplyChannelHistory> historyList = inno72SupplyChannelHistoryMapper.getWorkOrderGoods(map);
        if(historyList != null && historyList.size()>0){
            workOrderVo.setBatchNo(batchNo);
            workOrderVo.setMachineCode(historyList.get(0).getMachineCode());
            workOrderVo.setCreateTime(historyList.get(0).getCreateTime());
            workOrderVo.setLocaleStr(historyList.get(0).getLocaleStr());
            workOrderVo.setMachineId(machineId);
            workOrderVo.setHistoryList(historyList);
        }
        return ResultGenerator.genSuccessResult(workOrderVo);
    }

    @Override
    public void findAndPushByTaskParam(int lackGoodsType) {
        List<Inno72SupplyChannel> list = inno72SupplyChannelMapper.selectTaskParam(lackGoodsType);
        if(list != null && list.size()>0){
            AlarmMessageBean alarmMessageBean = new AlarmMessageBean();
            alarmMessageBean.setSystem("machineLackGoods");
            alarmMessageBean.setType("machineLackGoodsException");
            for(Inno72SupplyChannel supplyChannel:list){
                String id = supplyChannel.getId();
                String key = "LACK_GOODS_TYPE_"+lackGoodsType+"_"+id;
//                redisUtil.del(key);
                String value = redisUtil.get(key);
                if(StringUtil.isEmpty(value)){
                    ChannelGoodsAlarmBean alarmBean = new ChannelGoodsAlarmBean();
                    alarmBean.setChannelNum(supplyChannel.getCode());
                    alarmBean.setGoodsName(supplyChannel.getGoodsName());
                    alarmBean.setLackGoodsType(lackGoodsType);
                    alarmBean.setMachineCode(supplyChannel.getMachineCode());
                    alarmMessageBean.setData(alarmBean);
                    logger.info("货道缺货发送push{}",JSONObject.toJSONString(alarmMessageBean));
                    redisUtil.publish("moniterAlarm",JSONObject.toJSONString(alarmMessageBean));
                    redisUtil.setex(key,60*60*2,"1");

                }
            }
        }
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
