package com.inno72.check.mapper;

import java.util.List;
import java.util.Map;

import com.inno72.check.model.Inno72CheckSignIn;
import com.inno72.check.vo.Inno72CheckUserVo;
import com.inno72.common.Mapper;

@org.apache.ibatis.annotations.Mapper
public interface Inno72CheckSignInMapper extends Mapper<Inno72CheckSignIn> {

	List<Inno72CheckUserVo> selectSignInByPage(Map<String, Object> params);

	List<Inno72CheckUserVo> selectSignInExportList(Map<String, Object> params);
}