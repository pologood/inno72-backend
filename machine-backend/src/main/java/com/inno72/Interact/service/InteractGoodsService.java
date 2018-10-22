package com.inno72.Interact.service;

import java.util.List;

import com.inno72.Interact.model.Inno72InteractGoods;
import com.inno72.Interact.vo.InteractGoodsVo;
import com.inno72.common.Result;
import com.inno72.common.Service;

/**
 * Created by CodeGenerator on 2018/09/19.
 */
public interface InteractGoodsService extends Service<Inno72InteractGoods> {

	Result<String> save(InteractGoodsVo interactGoods);

	InteractGoodsVo findGoodsById(String id, Integer type);

	Result<String> update(InteractGoodsVo model);

	List<InteractGoodsVo> getList(String interactId, String shopsId);

	Result<String> deleteById(String interactId, String goodsId);

}
