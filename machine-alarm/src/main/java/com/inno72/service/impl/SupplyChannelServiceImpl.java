package com.inno72.service.impl;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.inno72.common.AbstractService;
import com.inno72.common.CommonConstants;
import com.inno72.common.DateUtil;
import com.inno72.common.StringUtil;
import com.inno72.mapper.Inno72SupplyChannelMapper;
import com.inno72.model.AlarmDropGoodsBean;
import com.inno72.model.AlarmLackGoodsBean;
import com.inno72.model.GoodsBean;
import com.inno72.model.Inno72AlarmGroup;
import com.inno72.model.Inno72AlarmParam;
import com.inno72.model.Inno72CheckFault;
import com.inno72.model.Inno72CheckFaultRemark;
import com.inno72.model.Inno72CheckUserPhone;
import com.inno72.model.Inno72Machine;
import com.inno72.model.Inno72SupplyChannel;
import com.inno72.mongo.MongoUtil;
import com.inno72.msg.MsgUtil;
import com.inno72.redis.IRedisUtil;
import com.inno72.service.AlarmGroupService;
import com.inno72.service.AlarmMsgService;
import com.inno72.service.AlarmParamService;
import com.inno72.service.CheckFaultService;
import com.inno72.service.CheckUserService;
import com.inno72.service.MachineService;
import com.inno72.service.SupplyChannelService;

@Service
@Transactional
public class SupplyChannelServiceImpl extends AbstractService<Inno72SupplyChannel> implements SupplyChannelService {
    @Resource
    private Inno72SupplyChannelMapper inno72SupplyChannelMapper;

    @Resource
    private MachineService machineService;

    @Resource
    private AlarmGroupService alarmGroupService;

    @Resource
    private CheckUserService checkUserService;
	@Resource
	private MongoUtil mongoUtil;

	@Autowired
	private MsgUtil msgUtil;

	@Resource
	private AlarmMsgService alarmMsgService;

	@Resource
	private IRedisUtil redisUtil;

	@Resource
	private AlarmParamService alarmParamService;

	@Resource
	private CheckFaultService checkFaultService;

	private Logger log = LoggerFactory.getLogger(this.getClass());
    @Override
    public List<Inno72SupplyChannel> findByPage(Object condition) {
        return null;
    }

    @Override
    public void closeSupply(Inno72SupplyChannel supplyChannel) {
        inno72SupplyChannelMapper.closeSupply(supplyChannel);
    }

	@Override
	public List<Inno72SupplyChannel> selectNormalSupply(String machineId, String goodsId) {
    	Map<String,Object> map = new HashMap<>();
    	map.put("machineId",machineId);
    	map.put("goodsId",goodsId);
    	List<Inno72SupplyChannel> list = inno72SupplyChannelMapper.selectNormalSupply(map);
		return list;
	}

	@Override
	public List<Inno72SupplyChannel> selectByParam(String machineId, String[] channelArray) {
		Map<String,Object> map = new HashMap<>();
		map.put("machineId",machineId);
		map.put("channelArray",channelArray);
		List<Inno72SupplyChannel> list = inno72SupplyChannelMapper.selectByParam(map);
		return list;
	}

	@Override
	public List<AlarmDropGoodsBean> getDropGoodsList() {
    	Query query = new Query();
    	query.addCriteria(Criteria.where("handle").is(0));
    	List<AlarmDropGoodsBean> list = mongoUtil.find(query,AlarmDropGoodsBean.class,"AlarmDropGoodsBean");
		return list;
	}

	@Override
	public void sendAlarmDropGoods(List<AlarmDropGoodsBean> list) {
    	String text = "";
		String active = System.getenv("spring_profiles_active");
		Inno72AlarmParam alarmParam = alarmParamService.findByAlarmType(1);
		for(AlarmDropGoodsBean dropGoodsBean:list){
			String machineCode = dropGoodsBean.getMachineCode();
			Inno72Machine machine = machineService.findByCode(machineCode);
			Inno72AlarmGroup group = alarmGroupService.selectByParam(machine.getAreaCode());
			Boolean alarmFlag = setAlarmFlag(machine);
			log.info("继续执行掉货异常报警。。。。");

			List<Inno72CheckUserPhone> inno72CheckUserPhones = getInno72CheckUserPhones(machineCode);
			String channelNum = dropGoodsBean.getChannelNum();
			String[] channelArray = null;
			if (StringUtil.isNotEmpty(channelNum)) {
				channelArray = channelNum.split(",");
			}
			String localStr = machine.getLocaleStr();
			List<Inno72SupplyChannel> supplyChannelList = this.selectByParam(machine.getId(),
					channelArray);
			String goodsId = null;
			String goodsName = null;
			if (supplyChannelList != null && supplyChannelList.size() > 0) {
				for (Inno72SupplyChannel inno72SupplyChannel : supplyChannelList) {
					inno72SupplyChannel.setIsDelete(1);
					goodsName = inno72SupplyChannel.getGoodsName();
					goodsId = inno72SupplyChannel.getGoodsId();
					this.closeSupply(inno72SupplyChannel);// 锁货道
				}
			}
			if(alarmParam != null){
				List<Inno72SupplyChannel> normalSupplyList = this.selectNormalSupply(machine.getId(),
						goodsId);
				if (alarmFlag) {
					// 巡检app接口
					String pushStr = "您好，" + localStr + "，机器编号：" + machineCode + "，" + channelNum + "掉货异常，货道已经被锁定，请及时联系巡检人员。";
					Map<String, String> pushMap = new HashMap<>();
					pushMap.put("machineCode", machineCode);
					pushMap.put("localStr", localStr);
					pushMap.put("text",pushStr);
					log.info("machineDropGoods send msg ，params：{}", pushMap.toString());
					// 钉钉报警
					Map<String, String> ddMaram = new HashMap<>();
					ddMaram.put("machineCode", machineCode);
					ddMaram.put("localStr", localStr);
					String textBeaf = "您好，" + localStr + "，机器编号：" + machineCode + "，";
					String title = "";
					if (normalSupplyList != null && normalSupplyList.size() > 0) {// 有未被锁定的货道
						text = channelNum + "掉货异常，货道已经被锁定，请及时联系巡检人员。";
						title = channelNum + "掉货异常";
					} else {// 货道全部被锁
						text = goodsName + "所在的货道全部被锁定，请及时联系巡检人员处理。";
						title = goodsName + "所在的货道全部被锁定";
					}
					String ddStr = textBeaf+text;
					ddMaram.put("text", StringUtil.setText(ddStr, active));
					log.info("group值为：{}", JSON.toJSON(group));
					if (group != null) {
						// 发送钉钉消息
						log.info("发送钉钉消息{}", JSON.toJSON(ddMaram));
						msgUtil.sendDDTextByGroup("dingding_alarm_common", ddMaram, group.getGroupId1(),
								"machineAlarm-RedisReceiver");
						String mdStr = "掉货异常，提醒方式：短信和钉钉，内容："+textBeaf+text;
						StringUtil.logger(CommonConstants.LOG_TYPE_DROPGOODS, machineCode, mdStr);
						log.info("发送掉货异常埋点日志", CommonConstants.LOG_TYPE_DROPGOODS, machineCode, mdStr);
					}
					// 保存接口
					int lackNum = 0;
					alarmMsgService.saveAlarmMsg(CommonConstants.SYS_MACHINE_DROPGOODS, machineCode, title,textBeaf,text, inno72CheckUserPhones);
				}

			}
			Update update = new Update();
			update.set("handle",1);
			Query upQuery = new Query();
			upQuery.addCriteria(Criteria.where("_id").is(dropGoodsBean.getId()));
			mongoUtil.updateFirst(upQuery,update,"AlarmDropGoodsBean");
		}
	}

	@Override
	public List<AlarmLackGoodsBean> getLackGoodsList() {
    	Query query = new Query();
    	query.addCriteria(Criteria.where("handle").is(0));
    	List<AlarmLackGoodsBean> list = mongoUtil.find(query,AlarmLackGoodsBean.class,"AlarmLackGoodsBean");
		return list;
	}

	@Override
	public void sendAlarmLackGoods(List<AlarmLackGoodsBean> list) {
		String active = System.getenv("spring_profiles_active");
		Inno72AlarmParam alarmParam = alarmParamService.findByAlarmType(2);
		for(AlarmLackGoodsBean lackGoodsBean:list){
			if(alarmParam != null){
				Map<String,Object> map = new HashMap<>();
				map.put("goodsId",lackGoodsBean.getGoodsId());
				map.put("machineCode",lackGoodsBean.getMachineCode());
				Inno72SupplyChannel supplyChannel = inno72SupplyChannelMapper.selectLockGoods(map);
				String machineCode = lackGoodsBean.getMachineCode();
				Inno72Machine machine = machineService.findByCode(machineCode);
				Inno72AlarmGroup group = alarmGroupService.selectByParam(machine.getAreaCode());
				Boolean alarmFlag = setAlarmFlag(machine);
				int surPlusNum = supplyChannel.getGoodsCount();
				List<Inno72CheckUserPhone> inno72CheckUserPhones = getInno72CheckUserPhones(machineCode);
				Map<String, String> param = new HashMap<>();
				String localStr = machine.getLocaleStr();
				String goodsName = supplyChannel.getGoodsName();
				param.put("machineCode", machineCode);
				param.put("localStr", localStr);
				String goodsInfo = "";
				String text = "";
				if (alarmFlag) {
					List<GoodsBean> goodsBeanList = inno72SupplyChannelMapper.selectLockGoodsList(machineCode);
					goodsInfo += goodsName + "剩" + surPlusNum + "，";
					if (goodsBeanList != null && goodsBeanList.size() > 0) {
						for (GoodsBean goodsBean : goodsBeanList) {
							String goods = goodsBean.getGoodsName();
							if (StringUtil.isNotEmpty(goods) && !goods.equals(goodsName)) {
								goodsInfo += goods+"剩" + goodsBean.getTotalCount() + "，";
							}
						}
						if (goodsInfo.lastIndexOf("，") == goodsInfo.length() - 1) {
							goodsInfo = goodsInfo.substring(0, goodsInfo.length() - 1);
						}
					}
					Boolean pushFlag = false;
					log.info("缺货报警剩余商品数量"+surPlusNum);
					String info = alarmParam.getParam();
					if(StringUtil.isNotEmpty(info)){
						String[] infoArray = info.split(",");
						String lackKey = "lackGoods:"+machineCode+":"+lackGoodsBean.getGoodsId();
						String textBeaf = "";
						for(int i=0;i<infoArray.length;i++){
							int count = Integer.parseInt(infoArray[i]);
							boolean flag = redisUtil.sismember(lackKey,count);
							if(surPlusNum > count){
								for(int j=i;j<infoArray.length;j++){
									count = Integer.parseInt(infoArray[j]);
									flag = redisUtil.sismember(lackKey,count);
									if(flag){
										redisUtil.srem(lackKey,count);
									}
								}
							}else if(surPlusNum < count){
								if(!flag){
									redisUtil.sadd(lackKey,count);
									pushFlag = true;
								}
							}else{
								pushFlag = true;
								redisUtil.sadd(lackKey,count);
							}
							log.info("缺货发送报警标记"+pushFlag);
							textBeaf = "您好，" + machine.getLocaleStr() + "，机器编号：" + machineCode + "，";
							if(pushFlag){
								text = goodsInfo+ "请及时联系巡检人员补货";
								String ddStr = textBeaf+text;
								param.put("text", StringUtil.setText(ddStr, active));
								if (group != null) {
									text = goodsName+ "数量已少于" + surPlusNum + "，请及时补货。";
									String mdStr = "缺货报警，提醒方式：钉钉和短信，内容："+textBeaf+ text;
									StringUtil.logger(CommonConstants.LOG_TYPE_LACKGOODS, machineCode, mdStr);
									log.info("发送缺货" + surPlusNum + "报警日志，日志内容为："+machineCode+text);
									msgUtil.sendDDTextByGroup("dingding_alarm_common", param, group.getGroupId2(),
											"machineAlarm-RedisReceiver");
								}
								String title = goodsName+"少于"+surPlusNum+"个";
								alarmMsgService.saveAlarmMsg(CommonConstants.SYS_MACHINE_LACKGOODS,machineCode,title,textBeaf,text,inno72CheckUserPhones);
								pushFlag = false;
								break;
							}
						}
						if(surPlusNum == 19){
							String remark = textBeaf+goodsInfo+"请及时接单";
							Inno72CheckFault checkFault = new Inno72CheckFault();
							String id = StringUtil.getUUID();
							checkFault.setId(id);
							String time = DateUtil.toTimeStr(LocalDateTime.now(), DateUtil.DF_FULL_S2);
							checkFault.setCode("F" + StringUtil.createRandomCode(6) + time);
							checkFault.setSubmitTime(LocalDateTime.now());
							checkFault.setSubmitUser("系统");
							checkFault.setMachineId(machine.getId());
							checkFault.setWorkType(3);//
							checkFault.setSource(3);// 系统派单
							checkFault.setUrgentStatus(2);// 紧急
							checkFault.setSubmitId("admin");
							checkFault.setSubmitUserType(1);// 巡检人员
							checkFault.setStatus(1);// 待接单
							checkFault.setReceiveUser(null);
							checkFault.setReceiveId(null);
							checkFault.setUpdateTime(LocalDateTime.now());
							checkFault.setRemark(remark);
							checkFaultService.saveCheckFault(checkFault);
							Inno72CheckFaultRemark faultRemark = new Inno72CheckFaultRemark();
							faultRemark.setRemark(remark);
							faultRemark.setUserId("admin");
							faultRemark.setName("系统");
							faultRemark.setType(1);
							faultRemark.setCreateTime(LocalDateTime.now());
							faultRemark.setFaultId(id);
							String remarkId = StringUtil.getUUID();
							faultRemark.setId(remarkId);
							checkFaultService.saveCheckFaultRemark(faultRemark);
							String title = "您有未接收的自动补货工单";
							alarmMsgService.saveAlarmMsg(CommonConstants.SYS_SUPPLY_WORK,machineCode,title,textBeaf,text,inno72CheckUserPhones);
						}
					}
				}
			}
			Update update = new Update();
			update.set("handle",1);
			Query upQuery = new Query();
			upQuery.addCriteria(Criteria.where("_id").is(lackGoodsBean.getId()));
			mongoUtil.updateFirst(upQuery,update,"AlarmLackGoodsBean");
		}
	}

	public Boolean setAlarmFlag(Inno72Machine machine) {
		log.info("机器信息：{}", JSON.toJSON(machine));
		boolean alarmFlag = false;
		if (machine != null && machine.getOpenStatus() == 0) {
			String monitorStart = machine.getMonitorStart();
			String monitorEnd = machine.getMonitorEnd();
			if (StringUtil.isNotEmpty(monitorStart) && StringUtil.isNotEmpty(monitorEnd)) {
				Date now = new Date();
				Date startDate = null;
				String nowTime = DateUtil.toStrOld(now, DateUtil.DF_ONLY_YMD_S1_OLD);
				if (StringUtil.isNotEmpty(monitorStart)) {
					String startTime = nowTime + " " + monitorStart;
					startDate = DateUtil.toDateOld(startTime, DateUtil.DF_ONLY_YMDHM);
				}
				Date endDate = null;
				if (StringUtil.isNotEmpty(monitorEnd)) {
					String endTime = nowTime + " " + monitorEnd;
					endDate = DateUtil.toDateOld(endTime, DateUtil.DF_ONLY_YMDHM);
				}
				if ((startDate != null && now.after(startDate) && (endDate != null && now.before(endDate)))) {
					alarmFlag = true;// 发送报警
				}
			} else {
				alarmFlag = true;
			}
		}
		log.info("返回报警标记" + alarmFlag);
		return alarmFlag;
	}


	private List<Inno72CheckUserPhone> getInno72CheckUserPhones(String machineCode) {
		Inno72CheckUserPhone inno72CheckUserPhone = new Inno72CheckUserPhone();
		inno72CheckUserPhone.setMachineCode(machineCode);
		return checkUserService.selectPhoneByMachineCode(inno72CheckUserPhone);
	}
}
