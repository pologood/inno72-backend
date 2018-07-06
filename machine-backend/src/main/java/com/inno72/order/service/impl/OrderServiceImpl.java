package com.inno72.order.service.impl;

import com.inno72.common.AbstractService;
import com.inno72.order.mapper.Inno72OrderMapper;
import com.inno72.order.model.Inno72Order;
import com.inno72.order.service.OrderService;
import org.apache.ibatis.exceptions.TooManyResultsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.util.List;

@Service
@Transactional
public class OrderServiceImpl extends AbstractService<Inno72Order> implements OrderService {

    @Resource
    private Inno72OrderMapper inno72OrderMapper;
}
