package com.inno72.project.service;

import com.inno72.common.Result;
import com.inno72.common.Service;
import com.inno72.project.model.Inno72Dictionary;


/**
 * Created by CodeGenerator on 2018/11/16.
 */
public interface Inno72DictionaryService extends Service<Inno72Dictionary> {

	Result getBaseDict(String type);
}
