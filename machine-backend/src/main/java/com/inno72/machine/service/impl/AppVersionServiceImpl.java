package com.inno72.machine.service.impl;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.inno72.common.AbstractService;
import com.inno72.common.StringUtil;
import com.inno72.common.datetime.LocalDateTimeUtil;
import com.inno72.machine.model.Inno72App;
import com.inno72.machine.service.AppVersionService;
import com.inno72.machine.vo.AppStatus;
import com.inno72.machine.vo.AppVersion;
import com.inno72.machine.vo.MachineAppStatus;
import com.inno72.machine.vo.MachineAppVersionVo;
import com.inno72.utils.page.Pagination;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by CodeGenerator on 2018/07/13.
 */
@Slf4j
@Service
@Transactional
public class AppVersionServiceImpl extends AbstractService<Inno72App> implements AppVersionService {
	@Autowired
	private MongoOperations mongoTpl;

	@Override
	public List<MachineAppVersionVo> appVersion(String appPackage, Integer versionCode, String keyword) {
		List<MachineAppVersionVo> list = new ArrayList<>();
		Query query = new Query();
		if (versionCode != null) {
			Criteria cri = Criteria.where("appPackageName").is(appPackage)
					.andOperator(Criteria.where("versionCode").lte(versionCode));
			query.addCriteria(Criteria.where("status").elemMatch(cri));
		}
		if (!StringUtil.isEmpty(keyword)) {
			Pattern pattern = Pattern.compile("^.*" + keyword + ".*$", Pattern.CASE_INSENSITIVE);
			query.addCriteria(new Criteria().orOperator(Criteria.where("machineId").regex(pattern),
					Criteria.where("status.appName").regex(pattern)));
		}
		Pagination page = Pagination.threadLocal.get();
		if (page == null) { // 没有设置分页page
			page = new Pagination();
		}
		query.with(new Sort(Sort.Direction.DESC, "createTime")).skip(page.getCurrentResult()).limit(page.getPageSize());
		List<MachineAppStatus> appVersions = mongoTpl.find(query, MachineAppStatus.class, "MachineAppStatus");
		if (appVersions != null) {
			Map<String, Integer> map = new HashMap<>();
			List<AppVersion> versions = mongoTpl.find(new Query(), AppVersion.class, "AppVersion");
			for (AppVersion v : versions) {
				map.put(v.getAppPackageName(), v.getAppVersionCode());
			}
			for (MachineAppStatus version : appVersions) {
				MachineAppVersionVo vo = new MachineAppVersionVo();
				vo.setMachineCode(version.getMachineId());
				vo.setCreateTime(LocalDateTimeUtil.transfer(version.getCreateTime(),
						DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
				List<AppStatus> sta = version.getStatus();
				if (sta != null) {
					List<String> appsInfo = new ArrayList<>();
					for (AppStatus statu : sta) {
						if (statu.getVersionCode() == -1) {
							continue;
						}
						Integer code = map.get(statu.getAppPackageName());
						if (code == null) {
							continue;
						}
						if (code == statu.getVersionCode()) {
							appsInfo.add(statu.getAppName() + " " + statu.getVersionName() + " (最新版本)");
						} else {
							appsInfo.add(statu.getAppName() + " " + statu.getVersionName());
						}
					}
					vo.setAppInfo(appsInfo);
				}
				list.add(vo);
			}
		}
		log.info(JSON.toJSONString(list));
		return list;
	}

}
