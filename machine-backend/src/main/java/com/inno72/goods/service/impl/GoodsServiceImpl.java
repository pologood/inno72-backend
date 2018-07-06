package com.inno72.goods.service.impl;

import tk.mybatis.mapper.entity.Condition;

import com.inno72.common.AbstractService;
import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;
import com.inno72.common.Results;
import com.inno72.common.StringUtil;
import com.inno72.goods.mapper.Inno72GoodsMapper;
import com.inno72.goods.model.Inno72Goods;
import com.inno72.goods.service.GoodsService;
import com.inno72.machine.model.Inno72Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
		logger.info("----------------商品修改--------------");
		
		model.setUpdateId("");
		
		super.update(model);
	}

	@Override
	public Result<String> delById(String id) {
		logger.info("----------------商品删除--------------");
		int n=inno72GoodsMapper.selectIsUseing(id);
		if (n>0) {
			return Results.failure("商品使用中，不能删除！");
		}
		
		Inno72Goods model = inno72GoodsMapper.selectByPrimaryKey(id);
		model.setIsDelete(1);//0正常，1下架
		model.setUpdateId("");
		super.update(model);
		return ResultGenerator.genSuccessResult();
	}

	
	@Override
	public List<Inno72Goods> findByPage(String code,String keyword) {
		// TODO 商户分页列表查询
		logger.info("---------------------商户分页列表查询-------------------");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("keyword", keyword);
		params.put("code", code);
		
		return inno72GoodsMapper.selectByPage(params);
	}
	
	
	@Override
	public List<Inno72Goods> getList(Inno72Goods model) {
		// TODO 获取商品列表
		logger.info("---------------------获取商品列表-------------------");
		model.setIsDelete(1);
		Condition condition = new Condition( Inno72Goods.class);
	   	condition.createCriteria().andEqualTo(model);
		return super.findByCondition(condition);
	}

	
	


}
