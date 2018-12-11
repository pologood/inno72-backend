package com.inno72.gameUser.mapper;

import java.util.List;
import java.util.Map;

import com.inno72.common.Mapper;
import com.inno72.gameUser.model.Inno72GameUser;

@org.apache.ibatis.annotations.Mapper
public interface Inno72GameUserMapper extends Mapper<Inno72GameUser> {

	List<Map<String, Object>> selectByPage(Map<String, Object> map);

}
