package com.inno72.order.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inno72.common.AbstractService;
import com.inno72.common.StringUtil;
import com.inno72.order.mapper.Inno72OrderRefundMapper;
import com.inno72.order.model.Inno72OrderRefund;
import com.inno72.order.service.OrderRefundService;

/**
 * Created by CodeGenerator on 2018/12/17.
 */
@Service
@Transactional
public class OrderRefundServiceImpl extends AbstractService<Inno72OrderRefund> implements OrderRefundService {
	@Resource
	private Inno72OrderRefundMapper inno72OrderRefundMapper;

	@Override
	public List<Map<String, Object>> getRefundList(String channel, String time, String status, String auditStatus,
			String keyword) {
		Map<String, Object> params = new HashMap<String, Object>();
		keyword = Optional.ofNullable(keyword).map(a -> a.replace("'", "")).orElse(keyword);
		params.put("keyword", keyword);
		params.put("channel", channel);
		params.put("status", status);
		params.put("auditStatus", auditStatus);

		if (StringUtil.isNotBlank(time)) {
			params.put("beginTime", time.trim() + " 00:00:00");
			params.put("endTime", time.trim() + " 23:59:59");
		}

		List<Map<String, Object>> orderList = inno72OrderRefundMapper.selectByPage(params);
		return orderList;
	}

	@Override
	public Map<String, Object> selectRefundDetail(String id) {

		Map<String, Object> orderList = inno72OrderRefundMapper.selectRefundDetail(id);
		return orderList;
	}

}
