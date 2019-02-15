package com.inno72.machine.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inno72.common.AbstractService;
import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;
import com.inno72.common.StringUtil;
import com.inno72.machine.mapper.Inno72CheckGoodsDetailMapper;
import com.inno72.machine.mapper.Inno72CheckGoodsNumMapper;
import com.inno72.machine.model.Inno72CheckGoodsDetail;
import com.inno72.machine.model.Inno72CheckGoodsNum;
import com.inno72.machine.service.SupplyDataService;
import com.inno72.machine.vo.SupplyDataVo;

import tk.mybatis.mapper.entity.Condition;

@Service
@Transactional
public class SupplyDataServiceImpl extends AbstractService<Inno72CheckGoodsNum>  implements SupplyDataService {

	@Resource
	private Inno72CheckGoodsNumMapper inno72CheckGoodsNumMapper;

	@Resource
	private Inno72CheckGoodsDetailMapper inno72CheckGoodsDetailMapper;
	@Override
	public List<Inno72CheckGoodsNum> findListByPage(SupplyDataVo supplyDataVo) {
		Map<String,Object> map = new HashMap<>();
		String keyword = supplyDataVo.getKeyword();
		if(StringUtil.isNotEmpty(keyword)){
			map.put("keyword",keyword);
		}
		return inno72CheckGoodsNumMapper.selectListByPage(map);
	}

	@Override
	public Result<List<Inno72CheckGoodsDetail>> findDetail(String id) {
		Condition condition = new Condition(Inno72CheckGoodsDetail.class);
		condition.createCriteria().andEqualTo("goodsNumId",id);
		condition.orderBy("createTime").desc();
		List<Inno72CheckGoodsDetail> list = inno72CheckGoodsDetailMapper.selectByCondition(condition);
		return ResultGenerator.genSuccessResult(list);
	}
}
