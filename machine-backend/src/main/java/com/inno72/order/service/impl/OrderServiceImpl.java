package com.inno72.order.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inno72.common.AbstractService;
import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;
import com.inno72.common.StringUtil;
import com.inno72.order.mapper.Inno72OrderGoodsMapper;
import com.inno72.order.mapper.Inno72OrderMapper;
import com.inno72.order.model.Inno72Order;
import com.inno72.order.service.OrderService;

@Service
@Transactional
public class OrderServiceImpl extends AbstractService<Inno72Order> implements OrderService {

	@Resource
	private Inno72OrderMapper inno72OrderMapper;

	@Resource
	private Inno72OrderGoodsMapper inno72OrderGoodsMapper;

	@Override
	public List<Inno72Order> getOrderList(Inno72Order order) {
		String keyword = order.getKeyword();
		if (StringUtil.isEmpty(keyword)) {
			order.setKeyword(null);
		}
		String areaCode = order.getAreaCode();
		if(StringUtil.isNotEmpty(areaCode)){
			int num = StringUtil.getAreaCodeNum(areaCode);
			String likeCode = areaCode.substring(0, num);
			order.setNum(num);
			order.setCode(likeCode);
		}
		int pageNo = order.getPageNo();
		order.setPageParam((pageNo-1)*20);
		List<Inno72Order> orderList = inno72OrderMapper.seleByParamForPg(order);
		return orderList;
	}

	@Override
	public Result<Inno72Order> getOrderDetail(String id) {
		Inno72Order order = inno72OrderMapper.selectOrderDetail(id);
		return ResultGenerator.genSuccessResult(order);
	}
}
