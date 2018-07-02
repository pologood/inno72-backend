package com.inno72.service.impl;

import com.inno72.mapper.Inno72GoodsMapper;
import com.inno72.model.Inno72Goods;
import com.inno72.service.GoodsService;
import com.inno72.common.AbstractService;
import com.inno72.common.StringUtil;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import javax.annotation.Resource;


/**
 * Created by CodeGenerator on 2018/06/29.
 */
@Service
@Transactional
public class GoodsServiceImpl extends AbstractService<Inno72Goods> implements GoodsService {
    @Resource
    private Inno72GoodsMapper inno72GoodsMapper;

	@Override
	public void save(Inno72Goods model) {
		// TODO Auto-generated method stub
		System.out.println("----------------商品添加--------------");
		
		model.setId(StringUtil.getUUID());
		model.setCreateId("");
		model.setUpdateId("");
		
		super.save(model);
	}

	@Override
	public void update(Inno72Goods model) {
		// TODO Auto-generated method stub
		System.out.println("----------------商品修改--------------");
		
		model.setCreateId("");
		model.setUpdateId("");
		
		super.update(model);
	}

	@Override
	public void deleteById(String id) {
		// TODO Auto-generated method stub
		System.out.println("----------------商品删除--------------");
		Inno72Goods model = inno72GoodsMapper.selectByPrimaryKey(id);
		model.setState(1);//0正常，1下架
		model.setCreateId("");
		model.setUpdateId("");
		
		super.update(model);;
	}

	@Override
	public void save(Inno72Goods goods, MultipartFile file) {
		
		System.out.println("----------------商品添加--------------");
		
		
		
		goods.setId(StringUtil.getUUID());
		goods.setCreateId("");
		goods.setUpdateId("");
		//调用上传图片
		
		super.save(goods);
	}


}
