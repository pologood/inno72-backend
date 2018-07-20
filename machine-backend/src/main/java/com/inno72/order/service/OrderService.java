package com.inno72.order.service;

import java.util.List;

import com.inno72.common.Service;
import com.inno72.order.model.Inno72Order;

public interface OrderService extends Service<Inno72Order> {
	List<Inno72Order> getOrderList(Inno72Order order);
}
