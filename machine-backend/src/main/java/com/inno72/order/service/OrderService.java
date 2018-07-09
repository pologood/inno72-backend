package com.inno72.order.service;

import com.inno72.common.Result;
import com.inno72.common.Service;
import com.inno72.order.model.Inno72Order;

import java.util.List;

public interface OrderService extends Service<Inno72Order> {
    Result<List<Inno72Order>> getOrderList(Inno72Order order);
}
