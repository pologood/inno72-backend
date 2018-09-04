package com.inno72.msgconsumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.inno72.model.AlarmMessageBean;
import com.inno72.model.GoodsChannelBean;
import com.inno72.model.MachineStatus;
import com.inno72.redis.IRedisUtil;

/**
 * @describtion:测试专用
 */
@RestController
public class TestMsgController {
    @Autowired
    private IRedisUtil redisUtil;

    @RequestMapping(value = "/testMsg", method = {RequestMethod.POST, RequestMethod.GET})
    public void processCustomMsg() {
        MachineStatus machineStatus = new MachineStatus();
        machineStatus.setMachineId("1889824071145");
        JSONArray jsonArray = new JSONArray();
        GoodsChannelBean goodsChannelBean = new GoodsChannelBean();
        goodsChannelBean.setGoodsChannelNum(1);
        goodsChannelBean.setGoodsChannelStatus(2);
        jsonArray.add(goodsChannelBean);
        machineStatus.setGoodsChannelStatus(jsonArray.toJSONString());
        AlarmMessageBean alarmMessageBean = new AlarmMessageBean();
        alarmMessageBean.setSystem("machineChannel");
        alarmMessageBean.setType("machineChannelException");
        alarmMessageBean.setData(machineStatus);
        redisUtil.publish("moniterAlarm", JSONObject.toJSONString(alarmMessageBean));
        // redisUtil.publish("moniterAlarm", "msg---------");

    }

}
