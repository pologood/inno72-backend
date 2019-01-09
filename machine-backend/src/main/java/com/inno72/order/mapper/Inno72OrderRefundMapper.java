package com.inno72.order.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.inno72.common.Mapper;
import com.inno72.order.model.Inno72OrderRefund;

@org.apache.ibatis.annotations.Mapper
public interface Inno72OrderRefundMapper extends Mapper<Inno72OrderRefund> {

	List<Map<String, Object>> selectByPage(Map<String, Object> map);

	Map<String, Object> selectRefundDetail(@Param("id") String id);

	int selectRefundOrderCount(@Param("countType") int countType);
}