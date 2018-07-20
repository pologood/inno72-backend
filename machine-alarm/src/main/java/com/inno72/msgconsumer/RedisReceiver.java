package com.inno72.msgconsumer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.inno72.common.CommonConstants;
import com.inno72.model.*;
import com.inno72.service.AlarmMsgService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author wxt
 * @date 2018/07/19
 */
@Service
public class RedisReceiver {

    Logger log = LoggerFactory.getLogger(this.getClass());

    @Resource
    private AlarmMsgService alarmMsgService;

    @Autowired
    private MongoOperations mongoTpl;


    public void receiveMessage(String message) throws UnsupportedEncodingException {

        JSONObject jsonObject = JSON.parseObject(message);
        String type = jsonObject.getString("type");
        String system = jsonObject.getString("system");
        log.info("当前接收的消息来自于系统：{}，类型：{}", system, type);

        if ((CommonConstants.SYS_MACHINE_CHANNEL).equals(system)) {
            //接收货道信息并转数据类型
            AlarmMessageBean<MachineStatus> alarmMessageBean = JSONObject.parseObject(message,
                    new TypeReference<AlarmMessageBean<MachineStatus>>() {
                    });
            MachineStatus machineStatus = alarmMessageBean.getData();
            String goodsChannelStatus = machineStatus.getGoodsChannelStatus();
            JSONObject jsonObj = JSONObject.parseObject(goodsChannelStatus);
            JSONArray jsonArray = jsonObj.getJSONArray("goodsChannelStatus");
            String js = JSONObject.toJSONString(jsonArray, SerializerFeature.WriteClassName);
            List<GoodsChannelBean> collection = JSONObject.parseArray(js, GoodsChannelBean.class);
            //调用接口查询故障信息

            String machineId = machineStatus.getMachineId();
            log.info("机器货道异常，machineCode:{}", machineId);
            //根据机器编码查询点位信息

            //报警接口
            //保存接口
            Inno72AlarmMsg inno72AlarmMsg = new Inno72AlarmMsg();
            if ((CommonConstants.MACHINE_CHANNEL_EXCEPTION).equals(type)) {
                inno72AlarmMsg.setTitle("货道故障");
            }
            inno72AlarmMsg.setCreateTime(LocalDateTime.now());
            inno72AlarmMsg.setSystem(system);
            alarmMsgService.save(inno72AlarmMsg);

        } else if ((CommonConstants.SYS_MACHINE_DROPGOODS).equals(system)) {
            //接收并转数据类型
            AlarmMessageBean<Map<String, String>> alarmMessageBean = JSONObject.parseObject(message,
                    new TypeReference<AlarmMessageBean<Map<String, String>>>() {
                    });
            Map<String, String> dropGoodsInfo = alarmMessageBean.getData();
            String machineCode = "";
            for (String key : dropGoodsInfo.keySet()) {
                log.info("机器掉货异常，machineCode:{},channelCode:{}", key, dropGoodsInfo.get(key));
                machineCode = key;
            }
            //保存消息次数等信息
            Query query = new Query();
            query.addCriteria(Criteria.where("machineCode").is(machineCode));
            List<DropGoodsExceptionInfo> dropGoodsExceptionInfoList = mongoTpl.find(query, DropGoodsExceptionInfo.class);
            if (dropGoodsExceptionInfoList.size() > 0) {
                for (DropGoodsExceptionInfo dropGoodsExceptionInfo : dropGoodsExceptionInfoList) {
                    Integer updateNum = dropGoodsExceptionInfo.getErrorNum() + 1;
                    //连续掉货两次
                    if (updateNum == 2) {
                        //巡检app接口
                    } else if (updateNum == 5) {
                        //组合报警接口
                    } else if (updateNum > 5 && (updateNum - 5) % 2 == 0) {
                        //钉钉报警接口
                    }
                    dropGoodsExceptionInfo.setErrorNum(updateNum);
                }
            } else {
                DropGoodsExceptionInfo dropGoodsExceptionInfo = new DropGoodsExceptionInfo();
                dropGoodsExceptionInfo.setCreateTime(LocalDateTime.now());
                dropGoodsExceptionInfo.setErrorNum(1);
                dropGoodsExceptionInfo.setMachineCode(machineCode);
                mongoTpl.save(dropGoodsExceptionInfo, "DropGoodsExceptionInfo");
            }


            //根据机器编码查询点位接口

            //调用报警接口

            //保存接口
            Inno72AlarmMsg inno72AlarmMsg = new Inno72AlarmMsg();
            if ((CommonConstants.MACHINE_DROPGOODS_EXCEPTION).equals(type)) {
                inno72AlarmMsg.setTitle("掉货异常报警");
            }
            inno72AlarmMsg.setCreateTime(LocalDateTime.now());
            inno72AlarmMsg.setSystem(system);
            alarmMsgService.save(inno72AlarmMsg);

        } else if ((CommonConstants.SYS_MACHINE_LACKGOODS).equals(system)) {
            //接收并转数据类型
            AlarmMessageBean<String> alarmMessageBean = JSONObject.parseObject(message,
                    new TypeReference<AlarmMessageBean<String>>() {
                    });
            String machineCode = alarmMessageBean.getData();
            log.info("商品缺货消息，machineCode:{}", machineCode);

            //根据机器编码查询点位接口

            //调用报警接口

            //保存接口
            Inno72AlarmMsg inno72AlarmMsg = new Inno72AlarmMsg();
            if ((CommonConstants.MACHINE_LACKGOODS_EXCEPTION).equals(type)) {
                inno72AlarmMsg.setTitle("商品缺货异常报警");
            }
            inno72AlarmMsg.setCreateTime(LocalDateTime.now());
            inno72AlarmMsg.setSystem(system);
            alarmMsgService.save(inno72AlarmMsg);
        } else {
            return;
        }

    }
}
