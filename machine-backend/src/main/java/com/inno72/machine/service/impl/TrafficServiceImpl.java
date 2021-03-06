package com.inno72.machine.service.impl;

import java.util.List;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.inno72.common.StringUtil;
import com.inno72.machine.service.TrafficService;
import com.inno72.machine.vo.SystemStatus;
import com.inno72.utils.page.Pagination;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TrafficServiceImpl implements TrafficService {

	@Autowired
	private MongoOperations mongoTpl;

	@Override
	public List<SystemStatus> list(String machineCode, Double allTraffic) {
		Query query = new Query();
		if (!StringUtil.isEmpty(machineCode)) {
			Pattern pattern = Pattern.compile("^.*" + machineCode + ".*$", Pattern.CASE_INSENSITIVE);
			query.addCriteria(Criteria.where("machineId").regex(pattern));
		}
		if (allTraffic != null) {
			query.addCriteria(Criteria.where("allTraffic").gte(allTraffic));
		}
		Pagination page = Pagination.threadLocal.get();
		if (page == null) { // 没有设置分页page
			page = new Pagination();
			Pagination.threadLocal.set(page);
		}
		query.with(new Sort(Sort.Direction.DESC, "createTime"));
		List<SystemStatus> systemStatuss = mongoTpl.find(query, SystemStatus.class, "SystemStatus");
		Pagination.threadLocal.get().setTotalCount(systemStatuss.size());
		query.skip(page.getCurrentResult()).limit(page.getPageSize());
		List<SystemStatus> list = mongoTpl.find(query, SystemStatus.class, "SystemStatus");
		log.info(JSON.toJSONString(list));
		return list;
	}

}
