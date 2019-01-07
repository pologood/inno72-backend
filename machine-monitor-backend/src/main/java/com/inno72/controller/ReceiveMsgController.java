package com.inno72.controller;

import static com.inno72.model.MessageBean.EventType.CHECKSTATUS;
import static com.inno72.model.MessageBean.SubEventType.APPSTATUS;
import static com.inno72.model.MessageBean.SubEventType.MACHINESTATUS;
import static com.inno72.model.MessageBean.SubEventType.SCREENSHOT;

import java.text.MessageFormat;
import java.time.LocalDateTime;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.inno72.common.utils.StringUtil;
import com.inno72.model.AlarmMessageBean;
import com.inno72.model.Inno72AppScreenShot;
import com.inno72.model.MachineAppStatus;
import com.inno72.model.MachineStatus;
import com.inno72.model.MessageBean;
import com.inno72.model.SystemStatus;
import com.inno72.redis.IRedisUtil;
import com.inno72.service.AppScreenShotService;
import com.inno72.service.SocketService;
import com.inno72.util.AesUtils;
import com.inno72.util.GZIPUtil;
import com.inno72.util.LogUtil;

@RestController
@RequestMapping("/receiver")
public class ReceiveMsgController {
	Logger log = LoggerFactory.getLogger(this.getClass());

	@Resource
	private IRedisUtil redisUtil;

	@Autowired
	private MongoOperations mongoTpl;

	@Autowired
	private AppScreenShotService appScreenShotService;

	@Autowired
	private SocketService socketService;

	@RequestMapping(value = "/receiveMsg", method = { RequestMethod.POST, RequestMethod.GET })
	public void receiveMsg(@RequestBody String msg, HttpServletRequest request) {
		String targetCode = request.getHeader("TargetCode");
		String msgType = request.getHeader("MsgType");
		if (!StringUtil.isEmpty(targetCode)) {
			if ("message".equals(msgType)) {
				processInfo(targetCode, msg);
			} else if ("monitor".equals(msgType)) {
				monitorResponse(targetCode, msg);
			}
		}

	}

	public void processInfo(String machineId, String msg) {
		// 解压缩及解密
		String message = AesUtils.decrypt(GZIPUtil.uncompress(msg));
		log.info("收到机器：{}发送的消息{}", machineId, message);
		JSONObject $json = JSON.parseObject(message);
		int eventType = $json.getInteger("eventType");
		int subEventType = $json.getInteger("subEventType");
		// 解析数据
		if (CHECKSTATUS.v() == eventType) {
			// 查看机器状态数据
			if (MACHINESTATUS.v() == subEventType) {
				// 转数据类型
				MessageBean<MachineStatus> messageBean = JSONObject.parseObject(message,
						new TypeReference<MessageBean<MachineStatus>>() {
						});
				MachineStatus machineStatus = messageBean.getData();
				machineStatus.setCreateTime(LocalDateTime.now());
				// 将货道故障信息推送到预警系统
				if (!StringUtils.isEmpty(machineStatus.getGoodsChannelStatus())) {
					AlarmMessageBean<MachineStatus> alarmMessageBean = new AlarmMessageBean<>();
					alarmMessageBean.setSystem("machineChannel");
					alarmMessageBean.setType("machineChannelException");
					alarmMessageBean.setData(machineStatus);
					redisUtil.publish("moniterAlarm", JSONObject.toJSONString(alarmMessageBean));
				}
				// 保存到mongo表中--先删除再保存
				Query query = new Query();
				query.addCriteria(Criteria.where("machineId").is(machineId));
				mongoTpl.remove(query, "MachineStatus");
				mongoTpl.save(machineStatus, "MachineStatus");
			} else if (APPSTATUS.v() == subEventType) {
				MessageBean<MachineAppStatus> appStatus = JSONObject.parseObject(message,
						new TypeReference<MessageBean<MachineAppStatus>>() {
						});
				MachineAppStatus apps = appStatus.getData();
				apps.setCreateTime(LocalDateTime.now());
				if (!com.inno72.common.utils.StringUtil.isEmpty(machineId)) {
					// 保存到mongo表中
					Query query = new Query();
					query.addCriteria(Criteria.where("machineId").is(machineId));
					mongoTpl.remove(query, "MachineAppStatus");
					mongoTpl.save(apps, "MachineAppStatus");
					// socketService.checkApp(apps);
				}
			} else if (SCREENSHOT.v() == subEventType) {
				String url = $json.getJSONObject("data").getString("imgUrl");
				Inno72AppScreenShot model = new Inno72AppScreenShot();
				model.setCreateTime(LocalDateTime.now());
				model.setId(StringUtil.getUUID());
				model.setImgUrl(url);
				model.setMachineCode(machineId);
				appScreenShotService.save(model);
			}

		}

	}

	public void monitorResponse(String machineId, String data) {
		String message = AesUtils.decrypt(GZIPUtil.uncompress(data));
		log.info("收到推送监控消息，machineId：{}机器的系统信息已保存,消息内容：{}", machineId, message);
		SystemStatus systemStatus = JSONObject.parseObject(message, SystemStatus.class);
		systemStatus.setCreateTime(LocalDateTime.now());
		systemStatus.setMachineId(machineId);
		Query querySystemStatus = new Query();
		querySystemStatus.addCriteria(Criteria.where("machineId").is(machineId));
		mongoTpl.remove(querySystemStatus, "SystemStatus");
		mongoTpl.save(systemStatus, "SystemStatus");
		socketService.updateNetStatus(systemStatus);
		socketService.recordHeart(machineId);
		String msg = "机器编号：{0}，网速（访问72服务器）：{1}，网速（访问阿里服务器）：{2}，运营商：{3}，网络类型：{4}，SD卡剩余内存：{5}，SD卡总内存：{6}，客流量：{7}";
		String logs = MessageFormat.format(msg, machineId, systemStatus.getPing(), systemStatus.getPing1(),
				systemStatus.getNetworkOperateName(), systemStatus.getNetworkType(), systemStatus.getSdFree(),
				systemStatus.getSdTotle(), systemStatus.getCount());
		LogUtil.logger("1", machineId, logs, JSON.toJSONString(systemStatus));
	}
}
