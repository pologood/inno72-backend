package com.inno72.socketio;

import static com.inno72.model.MessageBean.EventType.CHECKSTATUS;
import static com.inno72.model.MessageBean.SubEventType.APPSTATUS;
import static com.inno72.model.MessageBean.SubEventType.MACHINESTATUS;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Resource;

import com.inno72.model.MachineLogInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.inno72.common.CommonConstants;
import com.inno72.model.MachineAppStatus;
import com.inno72.model.MachineStatus;
import com.inno72.model.MessageBean;
import com.inno72.plugin.http.HttpClient;
import com.inno72.redis.IRedisUtil;
import com.inno72.socketio.core.SocketServer;
import com.inno72.socketio.core.SocketServerHandler;
import com.inno72.util.AesUtils;
import com.inno72.util.GZIPUtil;

@Configuration
public class SocketIOStartHandler {
	Logger log = LoggerFactory.getLogger(this.getClass());

	@Resource
	private IRedisUtil redisUtil;

	@Autowired
	private MongoOperations mongoTpl;

	private SocketServerHandler socketServerHandler() {

		return new SocketServerHandler() {

			@Override
			public String process(String key, String data, Map<String, List<String>> params) {
				// 解压缩及解密
				String message = AesUtils.decrypt(GZIPUtil.uncompress(data));

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
						String machineId = machineStatus.getMachineId();
						machineStatus.setCreateTime(new Date());
						// 保存到mongo表中--先删除再保存
						Query query = new Query();
						query.addCriteria(Criteria.where("machineId").is(machineId));
						mongoTpl.findAndRemove(query, MachineStatus.class);
						mongoTpl.save(machineStatus, "MachineStatus");

					} else if (APPSTATUS.v() == subEventType) {
						MessageBean<MachineAppStatus> appStatus = JSONObject.parseObject(message,
								new TypeReference<MessageBean<MachineAppStatus>>() {
								});
						MachineAppStatus apps = appStatus.getData();
						String machineId = apps.getMachineId();
						apps.setCreateTime(new Date());
						// 保存到mongo表中
						Query query = new Query();
						query.addCriteria(Criteria.where("machineId").is(machineId));
						mongoTpl.findAndRemove(query, MachineAppStatus.class);
						mongoTpl.save(apps, "MachineAppStatus");
					}

				}

				return "OK";
			}

			@Override
			public String deviceIdMsg(String sessionId, String data, Map<String, List<String>> params) {
				log.info("获取机器Id方法开始,sessionId=" + sessionId + ",deviceId" + data);

				// 解压缩并解密data
				String deviceId = AesUtils.decrypt(GZIPUtil.uncompress(data));

				// 从数据库中取
				String machineIdResult = HttpClient
						.post("http://localhost:8880/machine/machine/initMachine?deviceId=" + deviceId, "");
				// 解析返回数据
				JSONObject jsonObject = JSONObject.parseObject(machineIdResult);
				String machineId = jsonObject.getString("data");
				if (!StringUtils.isEmpty(machineId)) {
					String machinKey = CommonConstants.REDIS_BASE_PATH + machineId;
					// 存入redis中
					redisUtil.set(machinKey, sessionId);
					// 加密并压缩
					String result = GZIPUtil.compress(AesUtils.encrypt(machineId));
					log.info("获取机器Id方法结束,sessionId=" + sessionId + ",machineId=" + machineId + ",deviceId" + data);
					return result;
				}
				return "";
			}

			@Override
			public void monitorResponse(String key, String data, Map<String, List<String>> params) {
				log.info("推送监控消息方法执行开始，data=" + data);
				// 解压缩以及解密数据
				String message = AesUtils.decrypt(GZIPUtil.uncompress(data));
				log.info("推送监控消息方法执行中，data=" + message);
				//获取机器Id
				String machineId = "1827308070495";

				MachineLogInfo machineLogInfo = new MachineLogInfo();
				machineLogInfo.setMachineId(machineId);
				//获取当前时间
				machineLogInfo.setCreateTime(new Date());

				//将机器Id与时间缓存到mangoDB中
				mongoTpl.save(machineLogInfo, "MachineAppStatus");

				log.info("推送监控消息方法执行结束，data=" + message);
			}



			@Override
			public void connectNotify(String sessionId, Map<String, List<String>> data) {
				log.info("socket连接开始");
				// 获取机器Id
				String machineId = Optional.ofNullable(data.get(CommonConstants.MACHINE_ID)).map(a -> a.get(0))
						.orElse("");
				if (StringUtils.isEmpty(machineId)) {
					log.info("socket连接中，没有获取到机器Id");
				} else {
					log.info("socket连接中，获取到机器Id,machineId=" + machineId);
					// 连接上的时候就将缓存
					String machinKey = CommonConstants.REDIS_BASE_PATH + machineId;
					redisUtil.set(machinKey, sessionId);
				}
				log.info("socket连接结束");
			}

			@Override
			public void closeNotify(String key, Map<String, List<String>> data) {

				String machineId = Optional.ofNullable(data.get(CommonConstants.MACHINE_ID)).map(a -> a.get(0))
						.orElse("");
				// 断开链接的时候把缓存移除
				if (StringUtils.isEmpty(machineId)) {
					log.info("socket连接中，没有获取到机器Id");
				} else {
					String machinKey = CommonConstants.REDIS_BASE_PATH + machineId;
					redisUtil.del(machinKey);
				}
				log.info("socket 断开了");
			}

		};
	}

	@Bean
	public SocketServer socketServer() {
		return new SocketServer("0.0.0.0", 1244, socketServerHandler());
	}

}
