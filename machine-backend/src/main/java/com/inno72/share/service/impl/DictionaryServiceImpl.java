package com.inno72.share.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.inno72.common.AbstractService;
import com.inno72.common.Result;
import com.inno72.common.Results;
import com.inno72.share.mapper.Inno72DictionaryMapper;
import com.inno72.share.model.Inno72Dictionary;
import com.inno72.share.service.DictionaryService;

/**
 * Created by CodeGenerator on 2018/11/16.
 */
@Service
@Transactional
public class DictionaryServiceImpl extends AbstractService<Inno72Dictionary> implements DictionaryService {
	private static final Logger LOGGER = LoggerFactory.getLogger(DictionaryServiceImpl.class);
	@Resource
	private Inno72DictionaryMapper inno72DictionaryMapper;

	@Override
	public Result<Map<String, List<Map<String, String>>>> getBaseDict(String type) {
		Map<String, List<Map<String, String>>> result = new HashMap<>();
		switch (type) {
		case Inno72Dictionary.INDUSTRY:
			result.put("industry", this.getDictionary(Inno72Dictionary.INDUSTRY));
			break;
		case Inno72Dictionary.CHANNEL:
			result.put("channel", this.getDictionary(Inno72Dictionary.CHANNEL));
			break;
		default:
			result.put("industry", this.getDictionary(Inno72Dictionary.INDUSTRY));
			result.put("channel", this.getDictionary(Inno72Dictionary.CHANNEL));
			break;
		}
		LOGGER.info("字典数据 code - {}, result - {}", JSON.toJSONString(result));
		return Results.success(result);
	}

	private List<Map<String, String>> getDictionary(String code) {
		List<Map<String, String>> dictionary = inno72DictionaryMapper.findDictionary(code);
		LOGGER.info("字典数据 code - {}, result - {}", JSON.toJSONString(dictionary));
		return dictionary;

	}

}
