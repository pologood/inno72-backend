package com.inno72.project.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inno72.common.AbstractService;
import com.inno72.common.Result;
import com.inno72.common.Results;
import com.inno72.project.mapper.Inno72DictionaryMapper;
import com.inno72.project.model.Inno72Dictionary;
import com.inno72.project.service.Inno72DictionaryService;


/**
 * Created by CodeGenerator on 2018/11/16.
 */
@Service
@Transactional
public class Inno72DictionaryServiceImpl extends AbstractService<Inno72Dictionary> implements Inno72DictionaryService {
	@Resource
	private Inno72DictionaryMapper inno72DictionaryMapper;

	@Override
	public Result getBaseDict(String type) {
		Map<String, List<Map<String, String>>> result = new HashMap<>();
		switch (type){
			case Inno72Dictionary.INDUSTRY:
				result.put("industry", this.getDictionary(Inno72Dictionary.INDUSTRY));
			case Inno72Dictionary.CHANNEL:
				result.put("channel", this.findChannelDictionary(Inno72Dictionary.CHANNEL));
			default:
				result.put("industry", this.getDictionary(Inno72Dictionary.INDUSTRY));
				result.put("channel", this.findChannelDictionary(Inno72Dictionary.CHANNEL));

		}

		return Results.success(result);
	}

	private List<Map<String, String>> getDictionary(String code){

		return inno72DictionaryMapper.findDictionary(code);

	}


	private List<Map<String, String>> findChannelDictionary(String code){

		return inno72DictionaryMapper.findChannelDictionary(code);

	}


}
