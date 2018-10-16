package com.inno72.machine.service.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inno72.common.AbstractService;
import com.inno72.common.Result;
import com.inno72.common.Results;
import com.inno72.common.SessionData;
import com.inno72.common.SessionUtil;
import com.inno72.common.StringUtil;
import com.inno72.common.datetime.LocalDateTimeUtil;
import com.inno72.machine.mapper.Inno72AppMapper;
import com.inno72.machine.model.Inno72App;
import com.inno72.machine.service.AppService;
import com.inno72.machine.vo.AppVersion;
import com.inno72.machine.vo.AppVersionHistory;
import com.inno72.system.model.Inno72User;

import lombok.extern.slf4j.Slf4j;
import tk.mybatis.mapper.entity.Condition;

/**
 * Created by CodeGenerator on 2018/07/13.
 */
@Slf4j
@Service
@Transactional
public class AppServiceImpl extends AbstractService<Inno72App> implements AppService {
	@Resource
	private Inno72AppMapper inno72AppMapper;
	@Autowired
	private MongoOperations mongoTpl;

	@Override
	public Result<List<AppVersionHistory>> findAppVersionList(String appPackageName, String keyword) {
		Query query = new Query();
		if (StringUtil.isEmpty(appPackageName)) {
			return Results.failure("请选择要搜索的app");
		}
		query.addCriteria(Criteria.where("appPackageName").is(appPackageName));
		if (!StringUtil.isEmpty(keyword)) {
			Pattern pattern = Pattern.compile("^.*" + keyword + ".*$", Pattern.CASE_INSENSITIVE);
			query.addCriteria(new Criteria().orOperator(Criteria.where("updateInfo").regex(pattern),
					Criteria.where("appVersion").is(keyword), Criteria.where("appVersionCode").is(keyword)));
		}

		query.with(new Sort(Sort.Direction.DESC, "createTime"));
		List<AppVersionHistory> appVersions = mongoTpl.find(query, AppVersionHistory.class, "AppVersionHistory");
		return Results.success(appVersions);
	}

	@Override
	public Result<String> saveHistory(AppVersionHistory history) {
		SessionData session = SessionUtil.sessionData.get();
		Inno72User mUser = Optional.ofNullable(session).map(SessionData::getUser).orElse(null);
		if (mUser == null) {
			log.info("登陆用户为空");
			return Results.failure("未找到用户登录信息");
		}
		Condition condition = new Condition(Inno72App.class);
		condition.createCriteria().andEqualTo("appPackageName", history.getAppPackageName());
		List<Inno72App> apps = inno72AppMapper.selectByCondition(condition);
		if (apps == null || apps.size() != 1) {
			return Results.failure("app信息错误");
		}
		Query query = new Query();
		query.addCriteria(Criteria.where("appPackageName").is(history.getAppPackageName()));
		List<AppVersion> appVersions = mongoTpl.find(query, AppVersion.class, "AppVersion");
		if (appVersions != null && appVersions.size() != 1) {
			return Results.failure("app信息错误");
		}
		Inno72App app = apps.get(0);
		app.setUrl(history.getDownloadUrl());
		inno72AppMapper.updateByPrimaryKeySelective(app);
		AppVersion appVersion = appVersions.get(0);
		appVersion.setAppVersion(history.getAppVersion());
		appVersion.setAppVersionCode(Integer.parseInt(history.getAppVersionCode()));
		appVersion.setDownloadUrl(history.getDownloadUrl());
		mongoTpl.remove(query, "AppVersion");
		mongoTpl.save(appVersion, "AppVersion");
		history.setCreateId(mUser.getId());
		history.setCreateUser(mUser.getName());
		history.setId(StringUtil.getUUID());
		history.setCreateTime(
				LocalDateTimeUtil.transfer(LocalDateTime.now(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
		mongoTpl.save(history, "AppVersionHistory");
		return Results.success();
	}

}
