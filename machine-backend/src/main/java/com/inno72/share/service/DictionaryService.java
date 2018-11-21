package com.inno72.share.service;

import java.util.List;
import java.util.Map;

import com.inno72.common.Result;
import com.inno72.common.Service;
import com.inno72.share.model.Inno72Dictionary;

/**
 * Created by CodeGenerator on 2018/11/16.
 */
public interface DictionaryService extends Service<Inno72Dictionary> {

	Result<Map<String, List<Map<String, String>>>> getBaseDict(String type);
}
