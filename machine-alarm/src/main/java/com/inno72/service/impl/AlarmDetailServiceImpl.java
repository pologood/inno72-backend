package com.inno72.service.impl;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.inno72.common.CommonConstants;
import com.inno72.common.DateUtil;
import com.inno72.common.StringUtil;
import com.inno72.machine.vo.PointLog;
import com.inno72.model.AlarmDetailBean;
import com.inno72.model.AlarmExceptionMachineBean;
import com.inno72.model.AlarmMachineBean;
import com.inno72.model.AlarmSendBean;
import com.inno72.model.Inno72AlarmGroup;
import com.inno72.model.Inno72AlarmParam;
import com.inno72.model.Inno72CheckUserPhone;
import com.inno72.model.Inno72Machine;
import com.inno72.mongo.MongoUtil;
import com.inno72.msg.MsgUtil;
import com.inno72.redis.IRedisUtil;
import com.inno72.service.AlarmDetailService;
import com.inno72.service.AlarmGroupService;
import com.inno72.service.AlarmMsgService;
import com.inno72.service.AlarmParamService;
import com.inno72.service.CheckUserService;
import com.inno72.service.MachineService;

@Service
@Transactional
public class AlarmDetailServiceImpl implements AlarmDetailService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource
	private IRedisUtil redisUtil;

	@Autowired
	private MsgUtil msgUtil;

	@Resource
	private CheckUserService checkUserService;

	@Resource
	private AlarmMsgService alarmMsgService;

	@Resource
	private MongoUtil mongoUtil;

	@Resource
	private AlarmGroupService alarmGroupService;

	@Resource
	private MachineService machineService;

	@Resource
	private AlarmParamService alarmParamService;

	@Override
	public void addToMachineBean(List<Inno72Machine> list) {
		Query query = new Query();
		List<AlarmMachineBean> alarmMachineBeanList = mongoUtil.find(query,AlarmMachineBean.class,"AlarmMachineBean");
		Set<String> beanSet = new HashSet<>();
		if(alarmMachineBeanList != null && alarmMachineBeanList.size()>0){
			for(AlarmMachineBean alarmMachineBean:alarmMachineBeanList){
				beanSet.add(alarmMachineBean.getMachineId());
			}
		}
		Set<String> set = new HashSet<>();
		for(Inno72Machine machine:list){
			String machineId = machine.getId();
			set.add(machineId);
			String localeStr = machine.getLocaleStr();
			if(!beanSet.contains(machineId)){
				AlarmMachineBean bean = new AlarmMachineBean();
				Date now = new Date();
				bean.setMachineId(machineId);
				bean.setMachineCode(machine.getMachineCode());
				bean.setMonitorStart(machine.getMonitorStart());
				bean.setMonitorEnd(machine.getMonitorEnd());
				bean.setLocaleStr(localeStr);
				bean.setHeartTime(now);
				bean.setConnectTime(now);
				bean.setCreateTime(now);
				bean.setUpdateTime(now);
				mongoUtil.save(bean,"AlarmMachineBean");
				logger.info("获取全部需要发送报警的机器，向MongoDB中放入机器，机器编号为"+machine.getMachineCode());
			}else if(StringUtil.isNotEmpty(localeStr)){
				Update update = new Update();
				update.set("localeStr",localeStr);
				update.set("monitorStart",machine.getMonitorStart());
				update.set("monitorEnd",machine.getMonitorEnd());
				update.set("machineCode",machine.getMachineCode());
				Query upQuery = new Query();
				upQuery.addCriteria(Criteria.where("machineId").is(machineId));
				mongoUtil.updateFirst(upQuery,update,"AlarmMachineBean");
			}
		}
		if(alarmMachineBeanList != null && alarmMachineBeanList.size()>0){
			for(AlarmMachineBean alarmMachineBean:alarmMachineBeanList){
				String machineId = alarmMachineBean.getMachineId();
				if(!set.contains(machineId)){
					Query delQuery = new Query();
					delQuery.addCriteria(Criteria.where("machineId").is(machineId));
					mongoUtil.remove(delQuery,"AlarmMachineBean");
					logger.info("获取全部需要发送报警的机器，从MongoDB中删除无需发送报警的机器");
					delRedis(machineId);
				}

			}
		}
	}

	@Override
	public void addToExceptionMachineBean() {
		List<AlarmMachineBean> list = mongoUtil.find(new Query(),AlarmMachineBean.class,"AlarmMachineBean");
		if(list != null && list.size()>0){
			Date now = new Date();
			for(AlarmMachineBean bean:list){
				String monitorStart = bean.getMonitorStart();
				String monitorEnd = bean.getMonitorEnd();
				Date startDate = null;
				String nowTime = DateUtil.toStrOld(now,DateUtil.DF_ONLY_YMD_S1_OLD);
				if(StringUtil.isNotEmpty(monitorStart)){
					String startTime = nowTime + " " +monitorStart;
					startDate = DateUtil.toDateOld(startTime,DateUtil.DF_ONLY_YMDHM);
				}
				Date endDate = null;
				if(StringUtil.isNotEmpty(monitorEnd)){
					String endTime = nowTime + " " +monitorEnd;
					endDate = DateUtil.toDateOld(endTime,DateUtil.DF_ONLY_YMDHM);
				}
				Boolean alarmFlag = true;
				if((startDate != null && startDate.after(now) || (endDate != null && endDate.before(now)))){
					alarmFlag = false;//不发报警
				}
				if(alarmFlag && StringUtil.isNotEmpty(bean.getLocaleStr())){
					this.addHeartExceptionMachine(bean,now);
					this.addConnectExceptionMachine(bean,now);
				}

			}
		}
	}

	@Override
	public void sendExceptionMachineAlarm() {
		List<AlarmExceptionMachineBean> list = mongoUtil.find(new Query(),AlarmExceptionMachineBean.class,"AlarmExceptionMachineBean");
		logger.info("发送网络或心跳获取MongoDB数据"+JSON.toJSONString(list));
		String active = System.getenv("spring_profiles_active");
		if(list != null && list.size()>0){
			Map<String,Object> paramMap = new HashMap<>();
			Inno72AlarmParam heartParam = alarmParamService.findByAlarmType(4);
			Inno72AlarmParam connectParam = alarmParamService.findByAlarmType(3);
			for(AlarmExceptionMachineBean bean:list){
				int type = bean.getType();
				int level = bean.getLevel();
				Map<String,String> param = new HashMap<>();
				String localeStr = bean.getLocaleStr();
				String machineCode = bean.getMachineCode();
				List<Inno72CheckUserPhone> inno72CheckUserPhones = getInno72CheckUserPhones(machineCode);
				Inno72Machine machine = machineService.findByCode(machineCode);
				int machineStatus = machine.getMachineStatus();
				int machineType = machine.getMachineType();
				String areaCode = machine.getAreaCode();
				if(StringUtil.isNotEmpty(areaCode)){
					Inno72AlarmGroup group = alarmGroupService.selectByParam(machine.getAreaCode());
					param.put("machineCode", machineCode);
					param.put("localStr", localeStr);
					String text = "";
					String textBeaf = "您好，"+localeStr+"，机器编号："+machineCode+"，";
					Query query = new Query();
					query.addCriteria(Criteria.where("id").is(bean.getDetailId()));
					AlarmDetailBean detailBean = mongoUtil.findOne(query,AlarmDetailBean.class,"AlarmDetailBean");
					String pageInfo = "";
					if(detailBean != null){
						pageInfo = detailBean.getPageInfo();
						if(StringUtil.isNotEmpty(pageInfo)){
							pageInfo = "，页面停留在"+pageInfo;
						}
					}
					if(type == 1 && machineStatus==4 && machineType != 2){
						if(heartParam != null){
							String heart = heartParam.getParam();
							if(StringUtil.isNotEmpty(heart)){
								String [] heartArray = heart.split(",");
								if(level<=heartArray.length){
									text = "出现页面加载异常，已经持续"+heartArray[level-1]+"分钟"+pageInfo+"，请及时联系巡检人员。";
									String ddStr = textBeaf+text;
									param.put("text",StringUtil.setText(ddStr,active));
									if(group != null){
										msgUtil.sendDDTextByGroup("dingding_alarm_common", param, group.getGroupId1(), "machineAlarm-AlarmDetailService");
										logger.info("心跳异常发送钉钉消息："+group.getGroupId1());
										AlarmSendBean alarmSendBean = new AlarmSendBean();
										alarmSendBean.setId(StringUtil.getUUID());
										alarmSendBean.setInfo(text);
										alarmSendBean.setLevel(level);
										alarmSendBean.setMachineId(bean.getMachineId());
										alarmSendBean.setMachineCode(machineCode);
										alarmSendBean.setType(type);
										if(detailBean != null) {
											alarmSendBean.setRemark(detailBean.getRemark());
											alarmSendBean.setPageInfo(detailBean.getPageInfo());
										}
										alarmSendBean.setLocaleStr(localeStr);
										alarmSendBean.setCreateTime(new Date());
										mongoUtil.save(alarmSendBean,"AlarmSendBean");
										String mdStr = "网络异常，提醒方式：钉钉，内容："+textBeaf+text;
										StringUtil.logger(CommonConstants.LOG_TYPE_HEART,machineCode,mdStr);
									}
									String title = "出现页面加载异常";
									alarmMsgService.saveAlarmMsg(CommonConstants.SYS_MACHINE_HEART,machineCode,title,textBeaf,text,inno72CheckUserPhones);
								}
							}
						}

					}else if(type == 2){
						if(connectParam != null){
							if(level == 1){
								if(machineStatus==4){
									textBeaf = "您好，"+localeStr+"，机器编号："+machineCode+"，";
									text = "网络已经连续10分钟未连接成功，请及时联系巡检人员。";
									String title = "网络已经连续10分钟未连接成功";
									alarmMsgService.saveAlarmMsg(CommonConstants.SYS_MACHINE_NET,machineCode,title,textBeaf,text,inno72CheckUserPhones);
									String ddStr = textBeaf+text;
									param.put("text",StringUtil.setText(ddStr,active));
									if(group != null){
										msgUtil.sendDDTextByGroup("dingding_alarm_common", param, group.getGroupId1(), "machineAlarm-AlarmDetailService");
										logger.info("网络连接异常发送钉钉消息："+group.getGroupId1());
										AlarmSendBean alarmSendBean = new AlarmSendBean();
										alarmSendBean.setId(StringUtil.getUUID());
										alarmSendBean.setInfo(textBeaf+text);
										alarmSendBean.setLevel(level);
										alarmSendBean.setMachineId(bean.getMachineId());
										alarmSendBean.setMachineCode(machineCode);
										alarmSendBean.setType(type);
										if(detailBean != null){
											alarmSendBean.setRemark(detailBean.getRemark());
											alarmSendBean.setPageInfo(detailBean.getPageInfo());
										}
										alarmSendBean.setLocaleStr(localeStr);
										alarmSendBean.setCreateTime(new Date());
										mongoUtil.save(alarmSendBean,"AlarmSendBean");
										String mdStr = "网络异常，提醒方式：钉钉，内容："+text;
										StringUtil.logger(CommonConstants.LOG_TYPE_CONNECT,machineCode,mdStr);
									}
								}

							}else if(level == 2){
								textBeaf = "您好，"+localeStr+"，机器编号："+machineCode+"，";
								text = "网络已经连续30分钟未连接成功，请及时联系巡检人员。";
							}

						}
					}
					Query removeQuery = new Query();
					removeQuery.addCriteria(Criteria.where("_id").is(bean.getId()));
					mongoUtil.remove(removeQuery,"AlarmExceptionMachineBean");
				}
			}
		}
	}

	@Override
	public void updateMachineStart() {
		List<AlarmMachineBean> list = mongoUtil.find(new Query(),AlarmMachineBean.class,"AlarmMachineBean");
		if(list != null && list.size()>0) {
			Date now = new Date();
			for (AlarmMachineBean bean : list) {
				String monitorStart = bean.getMonitorStart();
				Date startDate = null;
				String nowTime = DateUtil.toStrOld(now, DateUtil.DF_ONLY_YMD_S1_OLD);
				if (StringUtil.isNotEmpty(monitorStart)) {
					String startTime = nowTime + " " + monitorStart;
					startDate = DateUtil.toDateOld(startTime, DateUtil.DF_ONLY_YMDHM);
					if(DateUtil.subTime(startDate,now,2)<=120 && DateUtil.subTime(startDate,now,2)>=0){
						String machineId = bean.getMachineId();
						Query query = new Query();
						query.addCriteria(Criteria.where("machineId").is(machineId));
						Update update = new Update();
						update.set("heartTime",startDate);
						update.set("connectTime",startDate);
						mongoUtil.updateFirst(query,update,"AlarmMachineBean");
						delRedis(machineId);
					}
				}
			}
		}
	}

	public void addHeartExceptionMachine(AlarmMachineBean bean,Date now){
		Inno72AlarmParam heartParam = alarmParamService.findByAlarmType(4);
		if(heartParam != null){
			String heart = heartParam.getParam();
			if(StringUtil.isNotEmpty(heart)){
				String[] heartArray = heart.split(",");
				String key = CommonConstants.MACHINE_ALARM_HEART_BEF+bean.getMachineId();
				String value = redisUtil.get(key);
				Date heartTime = bean.getHeartTime();
				long sub = DateUtil.subTime(now,heartTime,2);
				AlarmExceptionMachineBean exceptionBean = new AlarmExceptionMachineBean();
				exceptionBean.setId(StringUtil.getUUID());
				exceptionBean.setDetailId(bean.getHeartId());
				exceptionBean.setType(1);
				exceptionBean.setMachineId(bean.getMachineId());
				exceptionBean.setMachineCode(bean.getMachineCode());
				exceptionBean.setCreateTime(now);
				exceptionBean.setLocaleStr(bean.getLocaleStr());
				if(StringUtil.isEmpty(value) && sub>=Integer.parseInt(heartArray[0])) {//间隔时间大于1分钟
					exceptionBean.setLevel(1);
					mongoUtil.save(exceptionBean,"AlarmExceptionMachineBean");
					logger.info("超过"+heartArray[0]+"分钟未发送心跳的机器，编号为：{}",bean.getMachineCode());
					redisUtil.set(key,"2");
				}else{
					for(int i=1;i<heartArray.length;i++){
						int heartInt = Integer.parseInt(heartArray[i]);
						if(((i+1)+"").equals(value) && sub>=heartInt){
							exceptionBean.setLevel(i+1);
							mongoUtil.save(exceptionBean,"AlarmExceptionMachineBean");
							logger.info("超过"+heartInt+"分钟未发送心跳的机器，编号为：{}",bean.getMachineCode());
							redisUtil.set(key,((i+2)+""));
							break;
						}
					}
				}

			}

		}

	}

	public void addConnectExceptionMachine(AlarmMachineBean bean,Date now){
		Date connectTime = bean.getConnectTime();
		long sub = DateUtil.subTime(now,connectTime,2);
		String key = CommonConstants.MACHINE_ALARM_CONNECT_BEF+bean.getMachineId();
		String value = redisUtil.get(key);
		AlarmExceptionMachineBean exceptionBean = new AlarmExceptionMachineBean();
		exceptionBean.setId(StringUtil.getUUID());
		exceptionBean.setDetailId(bean.getConnectId());
		exceptionBean.setType(2);
		exceptionBean.setMachineId(bean.getMachineId());
		exceptionBean.setMachineCode(bean.getMachineCode());
		exceptionBean.setLocaleStr(bean.getLocaleStr());
		exceptionBean.setCreateTime(now);
		if(StringUtil.isEmpty(value) && sub>=10){//间隔时间大于10分钟
			exceptionBean.setLevel(1);
			mongoUtil.save(exceptionBean,"AlarmExceptionMachineBean");
			logger.info("超过10分钟未发送连接的机器，编号为：{}",bean.getMachineCode());
			redisUtil.set(key,"2");
		}else if("2".equals(value) && sub>=30){
			String connectTimeKey = CommonConstants.MACHINE_ALARM_CONNECT_TIME_BEF+bean.getMachineId();
			String connectTimeValue = redisUtil.get(connectTimeKey);
			if(StringUtil.isEmpty(connectTimeValue)){//redis为空时发送
				exceptionBean.setLevel(2);
				mongoUtil.save(exceptionBean,"AlarmExceptionMachineBean");
				logger.info("超过30分钟未发送连接的机器，编号为：{}",bean.getMachineCode());
				redisUtil.setex(connectTimeKey,60*30,"1");//有效时间半个小时
			}
		}
	}

	private List<Inno72CheckUserPhone> getInno72CheckUserPhones(String machineCode) {
		Inno72CheckUserPhone inno72CheckUserPhone = new Inno72CheckUserPhone();
		inno72CheckUserPhone.setMachineCode(machineCode);
		return checkUserService.selectPhoneByMachineCode(inno72CheckUserPhone);
	}

	public void delRedis(String machineId){
		String heartKey = CommonConstants.MACHINE_ALARM_HEART_BEF+machineId;
		redisUtil.del(heartKey);
		String heartTimeKey = CommonConstants.MACHINE_ALARM_HEART_TIME_BEF+machineId;
		redisUtil.del(heartTimeKey);
		String connectKey = CommonConstants.MACHINE_ALARM_CONNECT_BEF+machineId;
		redisUtil.del(connectKey);
		String connectTimeKey = CommonConstants.MACHINE_ALARM_CONNECT_TIME_BEF+machineId;
		redisUtil.del(connectTimeKey);
	}

	public void sendSms(Map<String,String> params,String machineCode,String channel,List<Inno72CheckUserPhone> inno72CheckUserPhones){
		for(Inno72CheckUserPhone userPhone :inno72CheckUserPhones){
			msgUtil.sendSMS(channel, params, userPhone.getPhone(), "machineAlarm-AlarmDetailService");
		}
	}



}
