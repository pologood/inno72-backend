package com.inno72.controller;

import com.alibaba.fastjson.JSONObject;
import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;
import com.inno72.redis.IRedisUtil;
import com.inno72.socketio.core.SocketHolder;
import com.inno72.util.AesUtils;
import com.inno72.util.GZIPUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Auther: wxt
 * @Date: 2018/7/5 19:02
 * @Description:发送消息到客户端
 */
@RestController
@RequestMapping("/sendMsgToClient/sendEvent")
public class SendMsgToClientController {

    @Resource
    private IRedisUtil redisUtil;

    @RequestMapping(value = "/sendEvent", method = { RequestMethod.POST, RequestMethod.GET })
    public Result<String> sendMsg() {

        //组装数据
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("eventType",1);
        jsonObject.put("subEventType",1);

        //加密并压缩
        String result = GZIPUtil.compress(AesUtils.encrypt(jsonObject.toString()));

        //从redis中取出sessionId
        String sessionId = redisUtil.get("1827308070495");

        SocketHolder.send(sessionId,result);

        return ResultGenerator.genSuccessResult();
    }
}

