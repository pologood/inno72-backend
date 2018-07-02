package com.inno72.service;
import com.inno72.model.Inno72AdminArea;

import java.util.List;

import com.inno72.common.Service;


/**
 * Created by CodeGenerator on 2018/06/29.
 */
public interface AdminAreaService extends Service<Inno72AdminArea> {
	
	public List<Inno72AdminArea> getLiset(String code);

}
