package com.inno72.order.service;

import java.util.List;
import java.util.Map;

import com.inno72.common.Result;
import com.inno72.common.Service;
import com.inno72.order.model.Inno72OrderRefund;

/**
 * Created by CodeGenerator on 2018/12/17.
 */
public interface OrderRefundService extends Service<Inno72OrderRefund> {

	List<Map<String, Object>> getRefundList(String channel, String time, String status, String auditStatus,
			String keyword);

	Map<String, Object> selectRefundDetail(String id);

	Result<String> refundAudit(String id, String auditStatus, String auditReason);

	Result<String> updateModle(Inno72OrderRefund model, String type);

	Integer refundOrderCount(int countType);

}
