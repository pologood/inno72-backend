package com.inno72.Interact.service;

import java.util.List;

import com.inno72.Interact.model.Inno72InteractMachineGoods;
import com.inno72.Interact.vo.Inno72InteractMachineGoodsVo;
import com.inno72.Interact.vo.InteractMachineGoods;
import com.inno72.common.Result;
import com.inno72.common.Service;

/**
 * Created by CodeGenerator on 2018/09/19.
 */
public interface InteractMachineGoodsService extends Service<Inno72InteractMachineGoods> {

	Result<String> save(InteractMachineGoods interactMachineGoods);

	List<Inno72InteractMachineGoodsVo> selectMachineGoods(String interactId, String merchantId);

	Result<String> deleteById(String interactId, String machineId, String goodsId);

}
