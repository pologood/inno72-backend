package com.inno72.service.impl;

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
import com.inno72.model.Inno72CheckUserPhone;
import com.inno72.model.Inno72Machine;
import com.inno72.model.Inno72SupplyChannel;
import com.inno72.mongo.MongoUtil;
import com.inno72.msg.MsgUtil;
import com.inno72.redis.IRedisUtil;
import com.inno72.service.AlarmGroupService;
import com.inno72.service.AlarmMsgService;
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
    	if(list != null && list.size()>0){
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
				List<Inno72SupplyChannel> normalSupplyList = this.selectNormalSupply(machine.getId(),
						goodsId);
				if (alarmFlag) {
					// 巡检app接口
					Map<String, String> pushMap = new HashMap<>();
					pushMap.put("machineCode", machineCode);
					pushMap.put("localStr", localStr);
					pushMap.put("text",
							"您好，" + localStr + "，机器编号：" + machineCode + "，" + channelNum + "掉货异常，货道已经被锁定，请及时联系巡检人员。");
					log.info("machineDropGoods send msg ，params：{}", pushMap.toString());
					// 钉钉报警
					Map<String, String> ddMaram = new HashMap<>();
					ddMaram.put("machineCode", machineCode);
					ddMaram.put("localStr", localStr);
					Map<String, String> smsMap = new HashMap<>();
					smsMap.put("machineCode", machineCode);
					String address = machine.getAddress();
					if (StringUtil.isNotEmpty(address)) {
						if (address.length() > 10) {
							address = address.substring(address.length() - 10, address.length());
						}
						smsMap.put("localStr", address);
					}
					if (normalSupplyList != null && normalSupplyList.size() > 0) {// 有未被锁定的货道
						text = "您好，" + localStr + "，机器编号：" + machineCode + "，" + channelNum + "掉货异常，货道已经被锁定，请及时联系巡检人员。";
					} else {// 货道全部被锁
						text = "您好，" + localStr + "，机器编号：" + machineCode + "，" + goodsName + "所在的货道全部被锁定，请及时联系巡检人员处理。";
					}
					ddMaram.put("text", StringUtil.setText(text, active));
					log.info("group值为：{}", JSON.toJSON(group));
					if (group != null) {
						// 发送钉钉消息
						log.info("发送钉钉消息{}", JSON.toJSON(ddMaram));
						msgUtil.sendDDTextByGroup("dingding_alarm_common", ddMaram, group.getGroupId1(),
								"machineAlarm-RedisReceiver");
						text = "掉货异常，提醒方式：短信和钉钉，内容：您好，" + localStr + "机器编号：" + machineCode + "," + channelNum
								+ "掉货异常，货道已被锁定，请及时联系巡检人员。";
						StringUtil.logger(CommonConstants.LOG_TYPE_DROPGOODS, machineCode, text);
						log.info("发送掉货异常埋点日志", CommonConstants.LOG_TYPE_DROPGOODS, machineCode, text);
					}
				}
				// 保存接口
				int lackNum = 0;
				alarmMsgService.saveAlarmMsg(CommonConstants.SYS_MACHINE_DROPGOODS, machineCode, lackNum, localStr, inno72CheckUserPhones);
				Update update = new Update();
				update.set("handle",1);
				Query upQuery = new Query();
				upQuery.addCriteria(Criteria.where("_id").is(dropGoodsBean.getId()));
				mongoUtil.updateFirst(upQuery,update,"AlarmDropGoodsBean");
			}
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
		if(list != null && list.size()>0){
			for(AlarmLackGoodsBean lackGoodsBean:list){
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
					if (goodsName.length() > 3) {
						goodsInfo += goodsName.substring(goodsName.length() - 3, goodsName.length()) + "剩" + surPlusNum
								+ "，";
					} else {
						goodsInfo += goodsName + "剩" + surPlusNum + "，";
					}
					if (goodsBeanList != null && goodsBeanList.size() > 0) {
						for (GoodsBean goodsBean : goodsBeanList) {
							String goods = goodsBean.getGoodsName();
							if (StringUtil.isNotEmpty(goods) && !goods.equals(goodsName)) {
								int goodsSize = goods.length();
								if (goodsSize > 3) {
									goodsInfo += goods.substring(goodsSize - 3, goodsSize);
								} else {
									goodsInfo += goods;
								}
								goodsInfo += "剩" + goodsBean.getTotalCount() + "，";
							}
						}
						if (goodsInfo.lastIndexOf("，") == goodsInfo.length() - 1) {
							goodsInfo = goodsInfo.substring(0, goodsInfo.length() - 1);
						}
					}

					String info = "20,10,5";
					String[] infoArray = info.split(",");
					String lackKey = "lackGoods:"+machineCode+":"+lackGoodsBean.getGoodsId();
					Boolean pushFlag = false;
					for(int i=0;i<infoArray.length;i++){
						int count = Integer.parseInt(infoArray[i]);
						if(surPlusNum != count){
							if(i==0){
								redisUtil.del(lackKey);
							}else{
								String value = redisUtil.getSet(lackKey,infoArray[i]);
								if(StringUtil.isEmpty(value)){
									pushFlag = true;
									redisUtil.sadd(lackKey,count);
								}else{
									if(i<infoArray.length-1){
										redisUtil.srem(lackKey,infoArray[i+1]);
									}
								}
							}
						}else{
							pushFlag = true;
							redisUtil.sadd(lackKey,count);
						}
						if(pushFlag){
							text = "您好，" + machine.getLocaleStr() + "，机器编号：" + machineCode + "，" + goodsInfo
									+ "请及时联系巡检人员补货";
							param.put("text", StringUtil.setText(text, active));
							if (group != null) {
								text = "缺货报警，提醒方式：钉钉和短信，内容：您好，" + localStr + "，机器编号：" + machineCode + "," + goodsName
										+ "数量已少于" + surPlusNum + "，请及时补货。";
								StringUtil.logger(CommonConstants.LOG_TYPE_LACKGOODS, machineCode, text);
								log.info("发送缺货" + surPlusNum + "报警日志，日志内容为：{}", machineCode, text);
								msgUtil.sendDDTextByGroup("dingding_alarm_common", param, group.getGroupId2(),
										"machineAlarm-RedisReceiver");
							}
							alarmMsgService.saveAlarmMsg(CommonConstants.SYS_MACHINE_LACKGOODS,machineCode,surPlusNum,localStr,inno72CheckUserPhones);
							pushFlag = false;
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
