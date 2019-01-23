package com.inno72.project.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.inno72.Interact.mapper.Inno72InteractMapper;
import com.inno72.Interact.model.Inno72Interact;
import com.inno72.common.AbstractService;
import com.inno72.common.JSR303Util;
import com.inno72.common.Result;
import com.inno72.common.Results;
import com.inno72.common.SessionData;
import com.inno72.common.SessionUtil;
import com.inno72.common.StringUtil;
import com.inno72.project.mapper.Inno72ActivityIndexMapper;
import com.inno72.project.mapper.Inno72ActivityInfoDescMapper;
import com.inno72.project.mapper.Inno72MerchantUserMapper;
import com.inno72.project.model.Inno72ActivityIndex;
import com.inno72.project.model.Inno72ActivityInfoDesc;
import com.inno72.project.service.Inno72ActivityIndexService;
import com.inno72.project.vo.Inno72ActivityIndexVo;
import com.inno72.system.model.Inno72User;


/**
 * Created by CodeGenerator on 2019/01/11.
 */
@Service
@Transactional
public class Inno72ActivityIndexServiceImpl extends AbstractService<Inno72ActivityIndex> implements Inno72ActivityIndexService {

	private static final Logger LOGGER = LoggerFactory.getLogger(Inno72ActivityIndexServiceImpl.class);

    @Resource
    private Inno72ActivityIndexMapper inno72ActivityIndexMapper;

	@Resource
	private Inno72ActivityInfoDescMapper inno72ActivityInfoDescMapper;

	@Resource
	private Inno72InteractMapper inno72InteractMapper;

	@Resource
	private Inno72MerchantUserMapper inno72MerchantUserMapper;

	@Override
	public Result<Inno72ActivityIndexVo> activityInfo(String merchantId, String activityId) {

		if (StringUtil.isEmpty(merchantId) || StringUtil.isEmpty(activityId)){
			return Results.failure("参数不存在!");
		}

		List<Inno72ActivityIndex> indexList = inno72ActivityIndexMapper.selectIndex(merchantId, activityId,
				null);

		List<Inno72ActivityInfoDesc> infoList = inno72ActivityInfoDescMapper.selectInfoDesc(merchantId, activityId);

		return Results.success(new Inno72ActivityIndexVo(indexList, infoList));
	}

	@Override
	public Result<String> saveIndex(String indexStr) {

		if (StringUtil.isEmpty(indexStr)){
			return Results.failure("没有参数哟!");
		}

		LOGGER.info("保存核心指标参数 -> {}", indexStr);

		List<Inno72ActivityIndex> indexList;

		try {

			indexList = JSON.parseArray(indexStr, Inno72ActivityIndex.class);

		}catch (Exception e){
			return Results.failure("参数解析异常");
		}

		if (indexList.size() == 0){
			return Results.failure("解析参数为空!");
		}

		Result<String> valid = JSR303Util.valid(indexList);
		if (valid.getCode() == Result.FAILURE){
			return valid;

		}
		SessionData session = SessionUtil.sessionData.get();
		Inno72User mUser = Optional.ofNullable(session).map(SessionData::getUser).orElse(null);

		if (mUser == null){
			return Results.failure("登录用户不存在!");
		}

		LocalDateTime time = LocalDateTime.now();

		List<Inno72ActivityIndex> insertS = new ArrayList<>();
		// 去重用
		List<String> types = new ArrayList<>();
		List<String> curTypes = new ArrayList<>();
		curTypes.add("1");
		curTypes.add("2");
		curTypes.add("3");

		for (Inno72ActivityIndex index : indexList){

			String activityId = index.getActivityId();
			Inno72Interact inno72Interact = inno72InteractMapper.selectByPrimaryKey(activityId);
			if (inno72Interact == null){
				return Results.failure("活动不在呀!");
			}

			String machineId = index.getMerchantId();
			List<Map<String, String>> activity = inno72MerchantUserMapper.activity(machineId);
			if (activity.size() == 0){
				return Results.failure("活动配置错误!");
			}
			List<String> actIds = new ArrayList<>(activity.size());
			for (Map<String, String> map : activity){
				actIds.add(map.get("actId"));
			}

			if (!actIds.contains(activityId)){
				return Results.failure("提交的活动不匹配!");
			}

			String activityIndexType = index.getActivityIndexType();
			if (!curTypes.contains(activityIndexType)){
				return Results.failure("指标类型错误!");
			}

			if (types.contains(activityIndexType)){
				return Results.failure("核心指标重复!");
			}else {
				types.add(activityIndexType);
			}

			if (StringUtil.isEmpty(index.getId())){
				List<Inno72ActivityIndex> sss = inno72ActivityIndexMapper.selectIndex(machineId, activityId, activityIndexType);
				if (sss.size() > 0){
					return Results.failure("提交的指标重复");
				}

				index.setCreateTime(time);
				index.setUpdateTime(time);
				index.setOperator(mUser.getName());
				index.setCreator(mUser.getName());
				index.setId(StringUtil.getUUID());
				index.setActivityName(inno72Interact.getName());
				insertS.add(index);

			}else {

				String id = index.getId();
				Inno72ActivityIndex oIndex = inno72ActivityIndexMapper.selectByPrimaryKey(id);
				oIndex.setUpdateTime(time);
				oIndex.setOperator(mUser.getName());
				oIndex.setActivityIndex(index.getActivityIndex());

				LOGGER.info("更新核心指标 -> {}", JSON.toJSONString(oIndex));
				int i = inno72ActivityIndexMapper.updateByPrimaryKeySelective(oIndex);
				LOGGER.info("更新核心指标完成 -> {}", i);

			}

		}

		int i = inno72ActivityIndexMapper.insertList(insertS);
		LOGGER.info("插入核心指标 -> {}, {}", JSON.toJSONString(insertS), i);

		return Results.success();
	}
}
