package com.inno72.project.service;

import java.util.List;

import com.inno72.common.Result;
import com.inno72.common.Service;
import com.inno72.project.model.Inno72Channel;


/**
 * Created by CodeGenerator on 2018/06/29.
 */
public interface ChannelService extends Service<Inno72Channel> {


	List<Inno72Channel> getList(Inno72Channel channel);

	List<Inno72Channel> findByPage(String keyword);

	Result<String> delById(String id);

}
