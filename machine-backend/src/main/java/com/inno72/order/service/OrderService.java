package com.inno72.order.service;

import java.util.List;
import java.util.Map;

import com.inno72.common.Result;
import com.inno72.common.Service;
import com.inno72.order.model.Inno72Order;

public interface OrderService extends Service<Inno72Order> {
	Map<String,Object> getOrderList(Inno72Order order);

	Result<Inno72Order> getOrderDetail(String id);
}
