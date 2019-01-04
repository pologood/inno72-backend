package com.inno72.store.service.impl;

import java.time.LocalDateTime;
import java.util.Optional;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inno72.common.AbstractService;
import com.inno72.common.Result;
import com.inno72.common.Results;
import com.inno72.common.SessionData;
import com.inno72.common.SessionUtil;
import com.inno72.common.StringUtil;
import com.inno72.store.mapper.Inno72StoreMapper;
import com.inno72.store.model.Inno72Store;
import com.inno72.store.model.Inno72Storekeeper;
import com.inno72.store.service.StoreService;

/**
 * Created by CodeGenerator on 2018/12/28.
 */
@Service
@Transactional
public class StoreServiceImpl extends AbstractService<Inno72Store> implements StoreService {

	private static Logger logger = LoggerFactory.getLogger(StoreServiceImpl.class);

	@Resource
	private Inno72StoreMapper inno72StoreMapper;

	@Override
	public Result<Object> saveModel(Inno72Store model) {

		try {
			SessionData session = SessionUtil.sessionData.get();
			Inno72Storekeeper mUser = Optional.ofNullable(session).map(SessionData::getStorekeeper).orElse(null);
			if (mUser == null) {
				logger.info("登陆用户为空");
				return Results.failure("未找到用户登录信息");
			}
			String mUserId = Optional.ofNullable(mUser).map(Inno72Storekeeper::getId).orElse(null);
			model.setId(StringUtil.getUUID());
			model.setCreateId(mUserId);
			model.setUpdateId(mUserId);
			model.setCreateTime(LocalDateTime.now());
			model.setUpdateTime(LocalDateTime.now());

			if (StringUtil.isBlank(model.getName())) {
				logger.info("请填写仓库名称");
				return Results.failure("请填写仓库名称");
			}
			if (StringUtil.isBlank(model.getAreaCode())) {
				logger.info("请选择地区位置");
				return Results.failure("请选择地区位置");
			}
			if (StringUtil.isBlank(model.getArea())) {
				logger.info("请填写详细地址");
				return Results.failure("请填写详细地址");
			}

			inno72StoreMapper.insert(model);

			return Results.warn("操作成功", 0, model.getId());

		} catch (Exception e) {
			e.printStackTrace();
			logger.info(e.getMessage());
			return Results.failure("操作失败");
		}
	}

}
