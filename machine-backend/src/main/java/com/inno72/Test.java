package com.inno72;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.inno72.utils.page.Pagination;

@Controller
public class Test {
	@Autowired
	private MongoOperations mongoTpl;

	@RequestMapping(value = "/testAdd", method = { RequestMethod.POST, RequestMethod.GET })
	public void test() {
		Map<String, Object> map = new HashMap<>();
		map.put("name", "123");
		map.put("age", 11);
		mongoTpl.save(map, "test");
	}

	@RequestMapping(value = "/testQuery", method = { RequestMethod.POST, RequestMethod.GET })
	public Map<String, Object> query() {
		Map<String, Object> adapter = new HashMap<>();
		Query _forPage = new Query();

		List<Criteria> orOparators = new ArrayList<>();

		int pageSize = 20;

		orOparators.add(Criteria.where("age").is(11));

		if (orOparators.size() > 0) {
			_forPage.addCriteria(new Criteria().andOperator(orOparators.toArray(new Criteria[orOparators.size()])));
		}

		Long count = mongoTpl.count(_forPage, HashMap.class, "test");
		// Long count = mongoTpl.count(_forPage, AA.class,"test");
		int pageNo = 1;
		Pagination pagination = new Pagination(pageNo, pageSize, count.intValue());
		_forPage.skip((pageNo - 1) * pageSize).limit(pageSize);// 分页
		// _forPage.with(new Sort(Sort.Direction.DESC, "createTime"));

		List<HashMap> data = mongoTpl.find(_forPage, HashMap.class, "test");
		pagination.setList(data);

		adapter.put("page", pagination);
		adapter.put("data", data);
		adapter.put("msg", "成功");
		adapter.put("code", 0);
		return adapter;
	}

	@RequestMapping(value = "/testDelete", method = { RequestMethod.POST, RequestMethod.GET })
	public void delete() {

		Query query = new Query();
		query.addCriteria(Criteria.where("name").is("123"));
		mongoTpl.findAllAndRemove(query,"test");
	}


}
