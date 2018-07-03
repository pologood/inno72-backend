package com.inno72.mapper;

import com.inno72.common.Mapper;
import com.inno72.model.Inno72UserDept;

public interface Inno72UserDeptMapper extends Mapper<Inno72UserDept> {

	int deleteByUserId(String userId);

	int deleteAll();
}