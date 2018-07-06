package com.inno72.socketio;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
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
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
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
				MessageBean<MachineStatus> messageBean = JSONObject.parseObject(message, new TypeReference<MessageBean<MachineStatus> >(){});

				//解析数据
				if (CHECKSTATUS.v() == messageBean.getEventType()) {
					//查看机器状态数据
					if (MACHINESTATUS.v() == (messageBean.getSubEventType())) {

						MachineStatus machineStatus = messageBean.getData();
						String machineId = machineStatus.getMachineId();
						//保存到mongo表中--先删除再保存
						Query query = new Query();
						query.addCriteria(Criteria.where("machineId").is(machineId));
						machineStatus = mongoTpl.findAndRemove(query, MachineStatus.class);
						mongoTpl.save(machineStatus, "machineStatus");

					} else if (APPSTATUS.v() == messageBean.getSubEventType()) {
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

				String result;
				//从数据库中取
				String machineId = HttpClient.post("http://localhost:8880//machine/machine/initMeachine?deviceId=" + deviceId, "");
				//解析返回数据
				JSONObject jsonObject = JSONObject.parseObject(machineId);
				String res = jsonObject.getString("data");

				if(!StringUtils.isEmpty(res)){
					//存入redis中
					redisUtil.set(res, key);
					//加密并压缩
					result = GZIPUtil.compress(AesUtils.encrypt(res));
					log.info("获取机器Id方法结束,sessionId="+key +",machineId=" + res +",deviceId" + data);
				}else {
					result = "";
				}
				/*String machineId = "1827308070495";
				result = GZIPUtil.compress(AesUtils.encrypt(machineId));*/


				return result;
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

				log.info("socket连接开始");

				//获取机器Id
				String machineId = Optional.ofNullable(data.get(CommonConstants.MACHINE_ID)).map(a -> a.get(0))
						.orElse("init");

				if(!StringUtils.isEmpty(machineId) && "init".equals(machineId)){
					log.info("socket连接中，没有获取到机器Id");
				}else{
					log.info("socket连接中，获取到机器Id,machineId=" + machineId);
					//连接上的时候就将缓存
					redisUtil.set(machineId,key);
				}

				log.info("socket连接结束");
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
		return new SocketServer("0.0.0.0", 1238, socketServerHandler());
	}


}
