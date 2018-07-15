package com.inno72.project.mapper;

import org.apache.ibatis.annotations.Param;

import com.inno72.common.Mapper;
import com.inno72.project.model.Inno72Coupon;

@org.apache.ibatis.annotations.Mapper
public interface Inno72CouponMapper extends Mapper<Inno72Coupon> {
	
	int deleteByPlanId(@Param("planId")String planId);
}