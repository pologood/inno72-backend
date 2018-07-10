package com.inno72.socketio;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.inno72.common.CommonConstants;
import com.inno72.model.*;
import com.inno72.redis.IRedisUtil;
import com.inno72.socketio.core.SocketServer;
import com.inno72.socketio.core.SocketServerHandler;
import com.inno72.util.AesUtils;
import com.inno72.util.GZIPUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.inno72.model.MessageBean.EventType.CHECKSTATUS;
import static com.inno72.model.MessageBean.SubEventType.APPSTATUS;
import static com.inno72.model.MessageBean.SubEventType.MACHINESTATUS;

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
				String machine = Optional.ofNullable(params.get(CommonConstants.MACHINE_ID)).map(a -> a.get(0))
						.orElse("");
				log.info("收到机器：{}发送的消息{}", machine, message);
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
						machineStatus.setCreateTime(LocalDateTime.now());
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
						String machineId = apps.getMachineId();
						apps.setCreateTime(LocalDateTime.now());
						// 保存到mongo表中
						Query query = new Query();
						query.addCriteria(Criteria.where("machineId").is(machineId));
						mongoTpl.remove(query, "MachineAppStatus");
						mongoTpl.save(apps, "MachineAppStatus");
					}

				}

				return "OK";
			}

			@Override
			public void monitorResponse(String key, String data, Map<String, List<String>> params) {
				log.info("推送监控消息方法执行开始，data：{}", data);
				// 解压缩以及解密数据
				String message = AesUtils.decrypt(GZIPUtil.uncompress(data));
				log.info("推送监控消息方法执行中，data：{}", message);
				// 获取机器Id
				SystemStatus systemStatus = JSONObject.parseObject(message, SystemStatus.class);
				String machineId = systemStatus.getMachineId();
				MachineLogInfo machineLogInfo = new MachineLogInfo();
				machineLogInfo.setMachineId(machineId);
				machineLogInfo.setCreateTime(LocalDateTime.now());
				// 删除原有数据，保留最新一条
				// 将机器Id与时间缓存到mangoDB中
				Query query = new Query();
				query.addCriteria(Criteria.where("machineId").is(machineId));
				mongoTpl.remove(query, "MachineLogInfo");
				mongoTpl.save(machineLogInfo, "MachineLogInfo");

				log.info("推送监控消息方法执行结束，data：{}", message);
			}

			@Override
			public void connectNotify(String sessionId, Map<String, List<String>> data) {
				String machineId = Optional.ofNullable(data.get(CommonConstants.MACHINE_ID)).map(a -> a.get(0))
						.orElse("");
				log.info("机器machineId:{}连接到服务器", machineId);
				if (!StringUtils.isEmpty(machineId)) {
					String machinKey = CommonConstants.REDIS_BASE_PATH + machineId;
					redisUtil.set(machinKey, sessionId);
				}
			}

			@Override
			public void closeNotify(String key, Map<String, List<String>> data) {
				String machineId = Optional.ofNullable(data.get(CommonConstants.MACHINE_ID)).map(a -> a.get(0))
						.orElse("");
				if (!StringUtils.isEmpty(machineId)) {
					log.info("机器machineId:{}断开连接", machineId);
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
