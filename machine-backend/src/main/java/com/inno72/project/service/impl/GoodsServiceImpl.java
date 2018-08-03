package com.inno72.project.service.impl;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.inno72.common.AbstractService;
import com.inno72.common.CommonConstants;
import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;
import com.inno72.common.Results;
import com.inno72.common.SessionData;
import com.inno72.common.StringUtil;
import com.inno72.common.UploadUtil;
import com.inno72.project.mapper.Inno72GoodsMapper;
import com.inno72.project.model.Inno72Goods;
import com.inno72.project.service.GoodsService;
import com.inno72.system.model.Inno72User;

import tk.mybatis.mapper.entity.Condition;

/**
 * Created by CodeGenerator on 2018/06/29.
 */
@Service
@Transactional
public class GoodsServiceImpl extends AbstractService<Inno72Goods> implements GoodsService {
	private static Logger logger = LoggerFactory.getLogger(GoodsServiceImpl.class);

	@Resource
	private Inno72GoodsMapper inno72GoodsMapper;

	Pattern pattern = Pattern.compile("^([+]?\\d{0,6})(\\.\\d{0,2})?");

	@Override
	public Result<String> saveModel(Inno72Goods model) {
		logger.info("----------------商品添加--------------");
		try {
			SessionData session = CommonConstants.SESSION_DATA;
			Inno72User mUser = Optional.ofNullable(session).map(SessionData::getUser).orElse(null);
			if (mUser == null) {
				logger.info("登陆用户为空");
				return Results.failure("未找到用户登录信息");
			}
			int n = inno72GoodsMapper.getCount(model.getCode());
			if (n > 0) {
				logger.info("商品编码已存在");
				return Results.failure("商品编码已存在");
			}
			if (null != model.getPrice()) {
				Matcher match = pattern.matcher(model.getPrice().toString());
				if (!match.matches()) {
					return Results.failure("商品价格最大整数6位，小数点后两位");
				}
			}

			String userId = Optional.ofNullable(mUser).map(Inno72User::getId).orElse(null);
			String id = StringUtil.getUUID();
			model.setId(id);
			model.setCreateId(userId);
			model.setUpdateId(userId);

			super.save(model);
		} catch (Exception e) {
			logger.info(e.getMessage());
			return Results.failure("操作失败");
		}

		return Results.success("操作成功");
	}

	@Override
	public Result<String> updateModel(Inno72Goods model) {
		logger.info("----------------商品修改--------------");
		try {
			SessionData session = CommonConstants.SESSION_DATA;
			Inno72User mUser = Optional.ofNullable(session).map(SessionData::getUser).orElse(null);
			if (mUser == null) {
				logger.info("登陆用户为空");
				return Results.failure("未找到用户登录信息");
			}
			if (StringUtil.isNotBlank(model.getCode())) {
				int n = inno72GoodsMapper.getCount(model.getCode());
				Inno72Goods g = inno72GoodsMapper.selectByPrimaryKey(model.getId());
				if (n > 0 && !g.getCode().equals(model.getCode())) {
					logger.info("商品编码已存在");
					return Results.failure("商品编码已存在,请确认");
				}
			}
			if (null != model.getPrice()) {
				Matcher match = pattern.matcher(model.getPrice().toString());
				if (!match.matches()) {
					return Results.failure("商品价格最大整数6位，小数点后两位");
				}
			}

			String userId = Optional.ofNullable(mUser).map(Inno72User::getId).orElse(null);

			model.setUpdateId(userId);
			model.setUpdateTime(LocalDateTime.now());
			super.update(model);
		} catch (Exception e) {
			logger.info(e.getMessage());
			return Results.failure("操作失败");
		}

		return Results.success("操作成功");

	}

	@Override
	public Result<String> delById(String id) {
		logger.info("----------------商品删除--------------");
		SessionData session = CommonConstants.SESSION_DATA;
		Inno72User mUser = Optional.ofNullable(session).map(SessionData::getUser).orElse(null);
		if (mUser == null) {
			logger.info("登陆用户为空");
			return Results.failure("未找到用户登录信息");
		}
		int n = inno72GoodsMapper.selectIsUseing(id);
		int m = inno72GoodsMapper.selectIsUseing1(id);
		if (n > 0 || m > 0) {
			return Results.failure("商品使用中，不能删除！");
		}
		String userId = Optional.ofNullable(mUser).map(Inno72User::getId).orElse(null);
		Inno72Goods model = inno72GoodsMapper.selectByPrimaryKey(id);
		model.setIsDelete(1);// 0正常，1下架
		model.setUpdateId(userId);
		model.setUpdateTime(LocalDateTime.now());
		super.update(model);
		return ResultGenerator.genSuccessResult();
	}

	@Override
	public Inno72Goods findById(String id) {
		Inno72Goods good = super.findById(id);
		good.setImg(CommonConstants.ALI_OSS + good.getImg());
		return good;
	}

	@Override
	public List<Inno72Goods> findByPage(String code, String keyword) {
		// TODO 商户分页列表查询
		logger.info("---------------------商户分页列表查询-------------------");
		Map<String, Object> params = new HashMap<String, Object>();
		keyword = Optional.ofNullable(keyword).map(a -> a.replace("'", "")).orElse(keyword);
		params.put("keyword", keyword);
		params.put("code", code);
		List<Inno72Goods> list = inno72GoodsMapper.selectByPage(params);
		for (Inno72Goods inno72Goods : list) {
			inno72Goods.setImg(CommonConstants.ALI_OSS + inno72Goods.getImg());
		}
		return inno72GoodsMapper.selectByPage(params);
	}

	@Override
	public List<Inno72Goods> getList(Inno72Goods model) {
		// TODO 获取商品列表
		logger.info("---------------------获取商品列表-------------------");
		model.setIsDelete(0);
		Condition condition = new Condition(Inno72Goods.class);
		condition.createCriteria().andEqualTo(model);
		return super.findByCondition(condition);
	}

	@Override
	public Result<String> uploadImage(MultipartFile file) {
		if (file.getSize() > 0) {
			// 调用上传图片
			return UploadUtil.uploadImage(file, "goods");
		}
		logger.info("[out-uploadImg]-空");
		return Results.success("");

	}

}
