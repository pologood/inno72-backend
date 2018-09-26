package com.inno72.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.inno72.common.CommonConstants;
import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;
import com.inno72.common.utils.StringUtil;
import com.inno72.model.AppVersion;
import com.inno72.model.Inno72AppMsg;
import com.inno72.model.MachineAppStatus;
import com.inno72.model.MessageBean;
import com.inno72.model.SendMessageBean;
import com.inno72.redis.IRedisUtil;
import com.inno72.service.AppMsgService;
import com.inno72.service.SocketService;
import com.inno72.util.AesUtils;
import com.inno72.util.GZIPUtil;

@RestController
@RequestMapping("/sendMsgToClient")
public class SendMsgToClientController {
	private static Logger logger = LoggerFactory.getLogger(SendMsgToClientController.class);

	@Resource
	private IRedisUtil redisUtil;

	@Autowired
	private AppMsgService appMsgService;
	@Autowired
	private SocketService socketService;
	@Autowired
	private MongoOperations mongoTpl;

	@RequestMapping(value = "/sendMsg", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<Map<String, Object>> sendMsg(@RequestBody SendMessageBean... msgs) {
		Map<String, Object> map = new HashMap<>();
		for (SendMessageBean msg : msgs) {
			logger.info("客户端发送消息：{}", JSON.toJSONString(msg));
			String result = GZIPUtil.compress(AesUtils.encrypt(JSON.toJSONString(msg)));
			String machinKey = CommonConstants.REDIS_SESSION_PATH + msg.getMachineId();
			String sessionId = redisUtil.get(machinKey);
			if (!com.inno72.common.utils.StringUtil.isEmpty(sessionId)) {
				Inno72AppMsg msg1 = new Inno72AppMsg();
				msg1.setId(StringUtil.uuid());
				msg1.setCreateTime(LocalDateTime.now());
				msg1.setMachineCode(msg.getMachineId());
				msg1.setContent(result);
				msg1.setStatus(0);
				msg1.setMsgType(1);
				appMsgService.save(msg1);
				map.put(msg.getMachineId(), "发送成功");
			} else {
				map.put(msg.getMachineId(), "发送失败");
			}
		}
		return ResultGenerator.genSuccessResult(map);
	}

	@RequestMapping(value = "/sendMsgStr", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<Map<String, Object>> sendMsgStr(@RequestBody Map<String, Object> param) {
		Map<String, Object> map = new HashMap<>();
		logger.info("客户端发送消息：{}", JSON.toJSONString(param));
		String result = GZIPUtil.compress(AesUtils.encrypt(JSON.toJSONString(param.get("msg"))));
		String machineCode = (String) param.get("machineCode");
		String machinKey = CommonConstants.REDIS_SESSION_PATH + machineCode;
		String sessionId = redisUtil.get(machinKey);
		if (!com.inno72.common.utils.StringUtil.isEmpty(sessionId)) {
			Inno72AppMsg msg1 = new Inno72AppMsg();
			msg1.setId(StringUtil.uuid());
			msg1.setCreateTime(LocalDateTime.now());
			msg1.setMachineCode(machineCode);
			msg1.setContent(result);
			msg1.setStatus(0);
			msg1.setMsgType(3);
			appMsgService.save(msg1);
			map.put(machineCode, "发送成功");
		} else {
			map.put(machineCode, "发送失败");
		}
		return ResultGenerator.genSuccessResult(map);
	}

	@RequestMapping(value = "/test", method = { RequestMethod.POST, RequestMethod.GET })
	public void test() {
		String s = "{\"data\":{\"machineId\":\"18333245\",\"status\":[{\"appName\":\"\",\"appPackageName\":\"com.inno72.app\",\"appStatus\":0,\"versionCode\":-1},{\"appName\":\"72监控APP\",\"appPackageName\":\"com.inno72.monitorapp\",\"appStatus\":2,\"versionCode\":12,\"versionName\":\"1.1.2\"},{\"appName\":\"72安装器\",\"appPackageName\":\"com.inno72.installer\",\"appStatus\":2,\"versionCode\":4,\"versionName\":\"1.0.3\"},{\"appName\":\"72管理\",\"appPackageName\":\"com.inno72.detection\",\"appStatus\":2,\"versionCode\":6,\"versionName\":\"1.0.5\"},{\"appName\":\"72数据中心\",\"appPackageName\":\"com.inno72.dc\",\"appStatus\":2,\"versionCode\":5,\"versionName\":\"1.0.4\"},{\"appName\":\"壶中界\",\"appPackageName\":\"com.tmall.hudong.worldinpot\",\"appStatus\":2,\"versionCode\":4,\"versionName\":\"0.0.4\"},{\"appName\":\"72上传\",\"appPackageName\":\"com.inno72.upload\",\"appStatus\":0,\"versionCode\":3,\"versionName\":\"1.0.2\"},{\"appName\":\"\",\"appPackageName\":\"com.inno72.demons\",\"appStatus\":0,\"versionCode\":-1},{\"appName\":\"点仙界\",\"appPackageName\":\"com.inno72.pot.limit\",\"appStatus\":1,\"versionCode\":2,\"versionName\":\"1.0.1\"}]},\"eventType\":1,\"subEventType\":2}";
		MessageBean<MachineAppStatus> appStatus = JSONObject.parseObject(s,
				new TypeReference<MessageBean<MachineAppStatus>>() {
				});
		MachineAppStatus apps = appStatus.getData();
		apps.setCreateTime(LocalDateTime.now());
		socketService.checkApp(apps);
	}

	@RequestMapping(value = "/test1", method = { RequestMethod.POST, RequestMethod.GET })
	public void test1() {
		AppVersion app = new AppVersion();
		app.setAppName("72App");
		app.setDownloadUrl("http://inno72.oss.72solo.com/apk/prod/prod_72app1.1.1.apk");
		app.setAppPackageName("com.inno72.app");
		app.setIsUpgrade(2);
		app.setAppVersionCode(5);
		app.setSeq(99);
		app.setStatus(1);
		mongoTpl.save(app);
	}
public static void main(String[] args) {
	String a="H4sIAAAAAAAAABWUywHDMAhDVzKY7zjGmP1HiHJqmroGniRyDm1ecWKUypm69Wr3Dpq+SdR19lmbrxzDu+zN9HqRmtt25rjme0LoztliR3e+kMxrdOh52Y06JTytZPH0WZxSOpr3r2nnxHkyTGcOi89pTrwUf2/tW0IrInztQ7hq7sa972TNqNB5fsvsbdQWFS50I6mZ2XfnfVLxunYY+2jfRXP9aOfhlLX2zvNOmIeet3m/dGWRqAGQEg3Mhcdr6kbsOazE7c+YdmS+1ccqTBbnfpTobqOELOF7dDDFS+mVbNFVGRqWtXY317ETHAKCTVZkcny9stXhqi2i1mei3bjuHdO5LyI3OHQUG2/2eyxneqvcA7wrz3FoFE+495BCged5rqKXIJ3iuQAnI0TocptcfD7yuw/UxuOeix7ZGSNZV1/hvPmY1w0/AII6kMRrnZ+fgr3ZFe1NETqgrHdcqsBADRyXrq2nGtyYk+PgD13GEAeeI8IDtHhksM2lXDkJnC+hYr/I5dX89uN1Nqbyd/jukkmegWS5McswlEmYVh/IbC3//Ynht0/w7N9pVRRz3l/f1uNc5/5Ud+k67SslRphrBdSG7rW9T9R7FdSkCToQ0d7cGdAc0fRtuYhlAv10LPjWnrbRrjWwrv0MhpYtYCFldf5Z9iuNYeTInTcCgyrUi4n1JfwLAtZAxbK2+ZK0pBkizKli02zZsxCPlQ+CmgNKehyKd6ffwJVtHRmrWwroaLyRpWc7R2TVuENoNhnkvP6rffsiiIgo34Xewx/0amFNUwk+0q62QxBgxdEicBAqfNl++0JOoWf1KnMv54KPcRZgYv03wlshY1gvL1UKqa/clElwbFAgJpf4pNkUKiLPsAGgi98gGJUg2m1x5Pwd3Dy2CQE/Bk/FQhP/JsAKaaF+BMtrVB/UxQ/4t65EFG5hCyxUh/5KA7XwqzGihwUxWEG3/J/ZMIY+nLrbsN7EEgIxtiK+i0i+8lg4REzWbcOVnmBx4M/GcsEprcYK9etQF0vp9OQHMteKTmAFAAA=";
			String aa = GZIPUtil.uncompress(AesUtils.decrypt(a));
			System.out.println(aa);
}
}
