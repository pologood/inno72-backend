package com.inno72.goods.service.impl;

import tk.mybatis.mapper.entity.Condition;

import com.inno72.common.AbstractService;
import com.inno72.common.CommonConstants;
import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;
import com.inno72.common.Results;
import com.inno72.common.StringUtil;
import com.inno72.goods.mapper.Inno72GoodsMapper;
import com.inno72.goods.model.Inno72Goods;
import com.inno72.goods.service.GoodsService;
import com.inno72.oss.OSSUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
	public void save(Inno72Goods goods) {
    	logger.info("----------------商品添加--------------");
		String id=StringUtil.getUUID();
		goods.setId(id);
		goods.setCreateId("");
		goods.setUpdateId("");
		
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
	public Inno72Goods findById(String id) {
		Inno72Goods good =super.findById(id);
		good.setImg(CommonConstants.ALI_OSS+good.getImg());
		return good;
	}

	@Override
	public List<Inno72Goods> findByPage(String code,String keyword) {
		// TODO 商户分页列表查询
		logger.info("---------------------商户分页列表查询-------------------");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("keyword", keyword);
		params.put("code", code);
		List<Inno72Goods> list =inno72GoodsMapper.selectByPage(params);
		for (Inno72Goods inno72Goods : list) {
			inno72Goods.setImg(CommonConstants.ALI_OSS+inno72Goods.getImg());
		}
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

	@Override
	public Result<String> uploadImage(MultipartFile file) {
		if ( file.getSize() > 0) {
			//调用上传图片
			try {
				String originalFilename = file.getOriginalFilename();
				String typeName = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
				String name =StringUtil.getUUID()+ typeName;
				String path = CommonConstants.OSS_PATH+"/good/"+name;
				OSSUtil.uploadByStream(file.getInputStream(),path);
				return Results.success(path);
			} catch (IOException e) {
				e.printStackTrace();
				return Results.failure("图片处理失败！");
			} catch (Exception e) {
				e.printStackTrace();
				return Results.failure("图片处理失败！");
			}
		}
		logger.info("[out-uploadImg]-空");
		return Results.success("");

	}

	
	


}
