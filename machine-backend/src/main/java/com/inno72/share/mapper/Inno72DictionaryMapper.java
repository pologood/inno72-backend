package com.inno72.share.mapper;

import java.util.List;
import java.util.Map;

import com.inno72.common.Mapper;
import com.inno72.share.model.Inno72Dictionary;

@org.apache.ibatis.annotations.Mapper
public interface Inno72DictionaryMapper extends Mapper<Inno72Dictionary> {
	List<Map<String, String>> findDictionary(String code);

	List<Map<String, String>> findChannelDictionary(String code);
}