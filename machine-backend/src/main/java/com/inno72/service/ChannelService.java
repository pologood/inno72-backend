package com.inno72.service;
import com.inno72.model.Inno72Channel;

import java.util.List;

import com.inno72.common.Service;


/**
 * Created by CodeGenerator on 2018/06/29.
 */
public interface ChannelService extends Service<Inno72Channel> {

	List<Inno72Channel> findByPage(Inno72Channel channel);

	List<Inno72Channel> getList(Inno72Channel channel);

}
