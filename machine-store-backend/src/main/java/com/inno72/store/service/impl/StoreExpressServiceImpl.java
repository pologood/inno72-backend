package com.inno72.store.service.impl;

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
import com.inno72.common.AbstractService;
import com.inno72.common.Result;
import com.inno72.common.Results;
import com.inno72.common.SessionData;
import com.inno72.common.SessionUtil;
import com.inno72.common.StringUtil;
import com.inno72.store.mapper.Inno72StoreExpressMapper;
import com.inno72.store.model.Inno72StoreExpress;
import com.inno72.store.model.Inno72Storekeeper;
import com.inno72.store.service.StoreExpressService;
import com.inno72.store.vo.StoreOrderVo;

/**
 * Created by CodeGenerator on 2018/12/28.
 */
@Service
@Transactional
public class StoreExpressServiceImpl extends AbstractService<Inno72StoreExpress> implements StoreExpressService {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Resource
	private Inno72StoreExpressMapper inno72StoreExpressMapper;

	@Override
	public Result<Object> saveModel(StoreOrderVo storeOrderVo) {
		logger.info("保存物流单接口参数:{}", JSON.toJSON(storeOrderVo));
		SessionData session = SessionUtil.sessionData.get();
		Inno72Storekeeper mUser = Optional.ofNullable(session).map(SessionData::getStorekeeper).orElse(null);
		if (mUser == null) {
			logger.info("登陆用户为空");
			return Results.failure("未找到用户登录信息");
		}

		List<Map<String, Object>> expressList = storeOrderVo.getExpressList();
		if (null == expressList || expressList.size() == 0) {
			logger.info("物流单未填写");
			return Results.failure("请选择活动商家");
		}

		List<Inno72StoreExpress> addExpressList = new ArrayList<>();

		for (Map<String, Object> express : expressList) {
			if (StringUtil.isBlank(express.get("id").toString())) {
				Inno72StoreExpress storeExpress = new Inno72StoreExpress();
				storeExpress.setId(StringUtil.getUUID());
				storeExpress.setOrderId(storeOrderVo.getId());
				storeExpress.setCreater(mUser.getName());
				storeExpress.setUpdater(mUser.getName());
				storeExpress.setCreateTime(LocalDateTime.now());
				storeExpress.setUpdateTime(LocalDateTime.now());
				storeExpress.setExpressCompany(express.get("expressCompany").toString());
				storeExpress.setExpressNum(express.get("expressNum").toString());
				storeExpress.setNumber(Integer.parseInt(express.get("number").toString()));

				addExpressList.add(storeExpress);
			} else {
				Inno72StoreExpress storeExpress = inno72StoreExpressMapper.selectByPrimaryKey(express.get("id"));
				storeExpress.setExpressCompany(express.get("expressCompany").toString());
				storeExpress.setExpressNum(express.get("expressNum").toString());
				storeExpress.setNumber(Integer.parseInt(express.get("number").toString()));

				inno72StoreExpressMapper.updateByPrimaryKeySelective(storeExpress);
			}
		}

		inno72StoreExpressMapper.insertStoreExpressList(addExpressList);
		return Results.success();
	}

}
