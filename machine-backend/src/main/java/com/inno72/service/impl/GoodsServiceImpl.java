package com.inno72.service.impl;

import com.inno72.mapper.Inno72GoodsMapper;
import com.inno72.model.Inno72Goods;
import com.inno72.service.GoodsService;
import tk.mybatis.mapper.entity.Condition;

import com.inno72.common.AbstractService;
import com.inno72.common.StringUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	private static Logger logger = LoggerFactory.getLogger(GoodsServiceImpl.class);
	
    @Resource
    private Inno72GoodsMapper inno72GoodsMapper;

    @Override
	public void save(Inno72Goods goods, MultipartFile file) {
    	logger.info("----------------商品添加--------------");
		
		goods.setId(StringUtil.getUUID());
		goods.setCreateId("");
		goods.setUpdateId("");
		//调用上传图片
		
		super.save(goods);
	}

	@Override
	public void update(Inno72Goods model) {
		// TODO Auto-generated method stub
		logger.info("----------------商品修改--------------");
		
		model.setCreateId("");
		model.setUpdateId("");
		
		super.update(model);
	}

	@Override
	public void deleteById(String id) {
		// TODO Auto-generated method stub
		logger.info("----------------商品删除--------------");
		Inno72Goods model = inno72GoodsMapper.selectByPrimaryKey(id);
		model.setState(1);//0正常，1下架
		model.setCreateId("");
		model.setUpdateId("");
		
		super.update(model);;
	}

	@Override
	public List<Inno72Goods> findByPage(Inno72Goods model) {
		// TODO 分页列表查询
		logger.info("---------------------分页列表查询-------------------");
		Condition condition = new Condition( Inno72Goods.class);
	   	condition.createCriteria().andEqualTo(model);
	   	
		return super.findByPage(condition);
	}
	
	@Override
	public List<Inno72Goods> getList(Inno72Goods model) {
		// TODO 获取商品列表
		logger.info("---------------------获取商品列表-------------------");
		model.setState(0);
		Condition condition = new Condition( Inno72Goods.class);
	   	condition.createCriteria().andEqualTo(model);
		return super.findByCondition(condition);
	}

	
	


}
