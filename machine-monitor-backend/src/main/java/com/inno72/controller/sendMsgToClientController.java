package com.inno72.controller;

import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;
import com.inno72.socketio.core.SocketHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Auther: wxt
 * @Date: 2018/7/5 19:02
 * @Description:发送消息到客户端
 */
@RestController
@RequestMapping("/sendMsgToClient")
public class sendMsgToClientController {


    @RequestMapping(value = "/sendMsgToClient", method = { RequestMethod.POST, RequestMethod.GET })
    public Result<String> sendMsgToClient() {

        SocketHolder.send("message","{\n" +
                " \"eventType\": 1,\n" +
                " \"subEventType\": 1\n" +
                "}");

        return ResultGenerator.genSuccessResult();
    }
}

