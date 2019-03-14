package com.inno72.order.service.impl;

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
import com.inno72.order.mapper.Inno72OrderGoodsMapper;
import com.inno72.order.mapper.Inno72OrderMapper;
import com.inno72.order.model.Inno72Order;
import com.inno72.order.model.Page;
import com.inno72.order.service.OrderService;

@Service
@Transactional
public class OrderServiceImpl extends AbstractService<Inno72Order> implements OrderService {

	@Resource
	private Inno72OrderMapper inno72OrderMapper;

	@Resource
	private Inno72OrderGoodsMapper inno72OrderGoodsMapper;

	@Override
	public Map<String,Object> getOrderList(Inno72Order order) {
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
		int totalCount = inno72OrderMapper.selectListCount(order);
		Page page = new Page();
		page.setPageNo(pageNo);
		boolean firstPage = false;
		if(pageNo==1){
			firstPage = true;
		}
		page.setFirstPage(firstPage);
		boolean lastPage = false;
		int pageSize = 20;
		int totalPage = totalCount % pageSize == 0 ? totalCount / pageSize : (int) Math.ceil(totalCount / pageSize);
		Map<String,Object> map = new HashMap<>();
		page.setLastPage(lastPage);
		page.setTotalCount(totalCount);
		page.setNextPage(pageNo+1);
		page.setPageSize(pageSize);
		page.setPrePage(pageNo);
		page.setTotalPage(totalPage);
		map.put("page",page);
		map.put("data",orderList);
		map.put("code",0);
		map.put("msg","");
		return map;
	}

	@Override
	public Result<Inno72Order> getOrderDetail(String id) {
		Inno72Order order = inno72OrderMapper.selectOrderDetail(id);
		return ResultGenerator.genSuccessResult(order);
	}
}
