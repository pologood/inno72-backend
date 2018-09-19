package com.inno72.socketio;

import static com.inno72.model.MessageBean.EventType.CHECKSTATUS;
import static com.inno72.model.MessageBean.SubEventType.APPSTATUS;
import static com.inno72.model.MessageBean.SubEventType.MACHINESTATUS;
import static com.inno72.model.MessageBean.SubEventType.SCREENSHOT;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.inno72.common.CommonConstants;
import com.inno72.common.StringUtil;
import com.inno72.model.AlarmMessageBean;
import com.inno72.model.Inno72AppScreenShot;
import com.inno72.model.MachineAppStatus;
import com.inno72.model.MachineStatus;
import com.inno72.model.MessageBean;
import com.inno72.model.SystemStatus;
import com.inno72.redis.IRedisUtil;
import com.inno72.service.AppScreenShotService;
import com.inno72.service.SocketService;
import com.inno72.socketio.core.SocketServer;
import com.inno72.socketio.core.SocketServerHandler;
import com.inno72.util.AesUtils;
import com.inno72.util.GZIPUtil;
import com.inno72.util.LogUtil;

@Configuration
public class SocketIOStartHandler {
	Logger log = LoggerFactory.getLogger(this.getClass());

	@Resource
	private IRedisUtil redisUtil;

	@Autowired
	private MongoOperations mongoTpl;

	@Autowired
	private AppScreenShotService appScreenShotService;

	@Autowired
	private SocketService socketService;

	private SocketServerHandler socketServerHandler() {

		return new SocketServerHandler() {

			@Override
			public String process(String key, String data, Map<String, List<String>> params) {
				// 解压缩及解密
				String message = AesUtils.decrypt(GZIPUtil.uncompress(data));
				String machineId = Optional.ofNullable(params.get(CommonConstants.MACHINE_ID)).map(a -> a.get(0))
						.orElse("");
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
						// 保存到mongo表中
						Query query = new Query();
						query.addCriteria(Criteria.where("machineId").is(machineId));
						mongoTpl.remove(query, "MachineAppStatus");
						mongoTpl.save(apps, "MachineAppStatus");
						socketService.checkApp(apps);
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

				return "OK";
			}

			@Override
			public void monitorResponse(String key, String data, Map<String, List<String>> params) {
				String machineId = Optional.ofNullable(params.get(CommonConstants.MACHINE_ID)).map(a -> a.get(0))
						.orElse("");
				String machinKey = CommonConstants.REDIS_SESSION_PATH + machineId;
				redisUtil.setex(machinKey, 60 * 2, key);
				// 将机器系统监控信息存入mongo数据库中
				String message = AesUtils.decrypt(GZIPUtil.uncompress(data));
				SystemStatus systemStatus = JSONObject.parseObject(message, SystemStatus.class);
				systemStatus.setCreateTime(LocalDateTime.now());
				systemStatus.setMachineId(machineId);
				Query querySystemStatus = new Query();
				querySystemStatus.addCriteria(Criteria.where("machineId").is(machineId));
				mongoTpl.remove(querySystemStatus, "SystemStatus");
				mongoTpl.save(systemStatus, "SystemStatus");
				log.info("收到推送监控消息，sessionId:{},machineId：{}机器的系统信息已保存,消息内容：{}", key, machineId, message);
				socketService.updateNetStatus(systemStatus);
				socketService.recordHeart(machineId);
				LogUtil.logger("1", machineId, JSON.toJSONString(systemStatus));
			}

			@Override
			public void connectNotify(String sessionId, Map<String, List<String>> data) {
				String machineId = Optional.ofNullable(data.get(CommonConstants.MACHINE_ID)).map(a -> a.get(0))
						.orElse("");
				log.info("socket连接到服务器，机器machineId:{}", machineId);
			}

			@Override
			public void closeNotify(String key, Map<String, List<String>> data) {
				String machineId = Optional.ofNullable(data.get(CommonConstants.MACHINE_ID)).map(a -> a.get(0))
						.orElse("");
				log.info("断开连接，机器machineId:{}", machineId);
			}

			@Override
			public void remoteResponse(String string, byte[] data, Map<String, List<String>> urlParams) {
			}

		};
	}

	@Bean
	public SocketServer socketServer() {
		return new SocketServer("0.0.0.0", 1244, socketServerHandler());
	}

}
