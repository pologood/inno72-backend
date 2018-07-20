package com.inno72.order.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inno72.common.AbstractService;
import com.inno72.common.StringUtil;
import com.inno72.order.mapper.Inno72OrderGoodsMapper;
import com.inno72.order.mapper.Inno72OrderMapper;
import com.inno72.order.model.Inno72Order;
import com.inno72.order.model.Inno72OrderGoods;
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
		List<Inno72Order> orderList = inno72OrderMapper.seleByParamForPage(order);
		if (orderList != null && orderList.size() > 0) {
			for (Inno72Order inno72Order : orderList) {
				Inno72OrderGoods inno72OrderGoods = new Inno72OrderGoods();
				inno72OrderGoods.setOrderId(inno72Order.getId());
				List<Inno72OrderGoods> orderGoodsList = inno72OrderGoodsMapper.seleByParam(inno72OrderGoods);
				if (orderGoodsList != null && orderGoodsList.size() > 0) {
					inno72Order.setOrderGoodsList(orderGoodsList);
				}
			}
		}
		return orderList;
	}
}
