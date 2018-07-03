package com.inno72.service;
import com.inno72.model.Inno72Activity;

import java.util.List;

import com.inno72.common.Service;


/**
 * Created by CodeGenerator on 2018/06/29.
 */
public interface ActivityService extends Service<Inno72Activity> {

	List<Inno72Activity> findByPage(Inno72Activity model);

	List<Inno72Activity> getList(Inno72Activity model);

}
