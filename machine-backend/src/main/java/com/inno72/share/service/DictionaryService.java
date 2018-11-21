package com.inno72.share.service;

import com.inno72.common.Result;
import com.inno72.common.Service;
import com.inno72.share.model.Inno72Dictionary;

/**
 * Created by CodeGenerator on 2018/11/16.
 */
public interface DictionaryService extends Service<Inno72Dictionary> {

	Result getBaseDict(String type);
}
