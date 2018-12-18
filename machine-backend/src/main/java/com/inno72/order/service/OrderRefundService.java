package com.inno72.order.service;

import java.util.List;
import java.util.Map;

import com.inno72.common.Service;
import com.inno72.order.model.Inno72OrderRefund;

/**
 * Created by CodeGenerator on 2018/12/17.
 */
public interface OrderRefundService extends Service<Inno72OrderRefund> {

	List<Map<String, Object>> getRefundList(String channel, String time, String status, String auditStatus,
			String keyword);

}
