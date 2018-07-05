package com.inno72.socketio;

import com.alibaba.fastjson.JSONObject;
import com.inno72.common.CommonConstants;
import com.inno72.model.AppStatus;
import com.inno72.model.MachineStatus;
import com.inno72.model.MessageBean;
import com.inno72.plugin.http.HttpClient;
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

import javax.annotation.Resource;
import java.util.HashMap;
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
				//解压缩及解密
				String message = AesUtils.decrypt(GZIPUtil.uncompress(data));
				//转数据类型
				MessageBean<Object> messageBean = new MessageBean<>();
				messageBean = JSONObject.parseObject(message, MessageBean.class);

				//解析数据
				if (CHECKSTATUS.equals(messageBean.getEventType())) {
					//查看机器状态数据
					if (MACHINESTATUS.equals(messageBean.getSubEventType())) {

						MachineStatus machineStatus = (MachineStatus) messageBean.getT();
						String machineId = machineStatus.getMachineId();
						//保存到mongo表中--先删除再保存
						Query query = new Query();
						query.addCriteria(Criteria.where("machineId").is(machineId));
						machineStatus = mongoTpl.findAndRemove(query, MachineStatus.class);
						mongoTpl.save(machineStatus, "machineStatus");

					} else if (APPSTATUS.equals(messageBean.getSubEventType())) {
						AppStatus appStatus = new AppStatus();
						String machineId = appStatus.getMachineId();
						//保存到mongo表中
						Query query = new Query();
						query.addCriteria(Criteria.where("machineId").is(machineId));
						appStatus = mongoTpl.findAndRemove(query, AppStatus.class);
						mongoTpl.save(appStatus, "MachineAppStatus");
					}

				}

				return "OK";
			}

			@Override
			public String deviceIdMsg(String key, String data, Map<String, List<String>> params) {
				log.info("获取机器Id方法开始,sessionId=" + key + ",deviceId" + data);

				//解压缩并解密data
				String deviceId = AesUtils.decrypt(GZIPUtil.uncompress(data));

				//从数据库中取
				String machineId = HttpClient.post("http://localhost:8880/machine/generateMachineId?deviceId=" + deviceId, "");
				//存入redis中
				redisUtil.set(machineId, key);

				//加密并压缩
				String result = GZIPUtil.compress(AesUtils.encrypt(machineId));

				log.info("获取机器Id方法结束,sessionId="+key +",machineId=" + result +",deviceId" + data);

				return machineId;
		}

			@Override
			public void monitorResponse(String key, String data, Map<String, List<String>> params) {
				log.info("推送监控消息方法执行开始，data=" + data);
				//解压缩以及解密数据
				String message = AesUtils.decrypt(GZIPUtil.uncompress(data));


				log.info("推送监控消息方法执行结束，data=" + message);
			}

			@Override
			public void connectNotify(String key, Map<String, List<String>> data) {

				//获取机器Id
				String param = Optional.ofNullable(data.get(CommonConstants.MACHINE_ID)).map(a -> a.get(0))
						.orElse("init");

				//解压缩及解密
				String machineId = AesUtils.decrypt(GZIPUtil.uncompress(param));

				//连接上的时候就将缓存
				redisUtil.set(key,machineId);

				log.info("socket连接上");
			}

			@Override
			public void closeNotify(String key, Map<String, List<String>> data) {

				String machineId = Optional.ofNullable(data.get(CommonConstants.MACHINE_ID)).map(a -> a.get(0))
						.orElse("init");
				//断开链接的时候把缓存移除
				redisUtil.del(machineId);
				log.info("socket 断开了");
			}

		};
	}

	@Bean
	public SocketServer socketServer() {
		return new SocketServer("0.0.0.0", 1237, socketServerHandler());
	}
	public static void main(String[] args) throws Exception {
		/*String string = HttpClient.post("http://172.16.18.240:8880/machine/generateMachineId?id=1","");
		System.out.println("我的返回值是" + string);*/
		SocketIOStartHandler socketIOStartHandler = new SocketIOStartHandler();
		Map<String, List<String>> params = new HashMap<>();
		String result = socketIOStartHandler.socketServerHandler().process("123","H4sIAAAAAAAAAA3K2xUAMQQFwJYu8YhyCPovYfd3zoi5oNeoBopbVy39EPJys4u7npnCREwuJXATHTqvf1rHPVRJdiv/y6luSbsbh54xodS4dCZ9V5jEH3d4R1A4GPm6VnSqXcFOK7VAiCtfeRRmH9oIbuygAAAA", params);
		System.out.println("我的返回值是" + result);

	}


}
