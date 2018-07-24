package com.inno72.share.service;

import java.util.List;

import com.inno72.common.Service;
import com.inno72.share.model.Inno72AdminArea;


/**
 * Created by CodeGenerator on 2018/06/29.
 */
public interface AdminAreaService extends Service<Inno72AdminArea> {
	
	public List<Inno72AdminArea> getLiset(String code);

}
