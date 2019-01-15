package com.inno72.store.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.inno72.common.AbstractService;
import com.inno72.common.Result;
import com.inno72.common.Results;
import com.inno72.common.StringUtil;
import com.inno72.common.UserUtil;
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
		Inno72Storekeeper mUser = UserUtil.getKepper();
		if (mUser == null) {
			logger.info("登陆用户为空");
			return Results.failure("未找到用户登录信息");
		}

		List<Inno72StoreExpress> expressList = storeOrderVo.getExpressList();
		if (null == expressList || expressList.size() == 0) {
			logger.info("物流单未填写");
			return Results.failure("请选择活动商家");
		}

		List<Inno72StoreExpress> addExpressList = new ArrayList<>();

		for (Inno72StoreExpress express : expressList) {
			if (StringUtil.isBlank(express.getId())) {
				express.setId(StringUtil.getUUID());
				express.setOrderId(storeOrderVo.getId());
				express.setCreater(mUser.getName());
				express.setUpdater(mUser.getName());
				express.setCreateTime(LocalDateTime.now());
				express.setUpdateTime(LocalDateTime.now());

				addExpressList.add(express);
			} else {
				Inno72StoreExpress storeExpress = inno72StoreExpressMapper.selectByPrimaryKey(express.getId());

				inno72StoreExpressMapper.updateByPrimaryKeySelective(storeExpress);
			}
		}

		inno72StoreExpressMapper.insertStoreExpressList(addExpressList);
		return Results.success();
	}

}
