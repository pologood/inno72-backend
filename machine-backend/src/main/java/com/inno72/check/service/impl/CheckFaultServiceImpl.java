package com.inno72.check.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inno72.check.mapper.Inno72CheckFaultMapper;
import com.inno72.check.mapper.Inno72CheckFaultRemarkMapper;
import com.inno72.check.model.Inno72CheckFault;
import com.inno72.check.model.Inno72CheckFaultRemark;
import com.inno72.check.service.CheckFaultService;
import com.inno72.check.vo.Inno72CheckFaultVo;
import com.inno72.common.AbstractService;
import com.inno72.common.CommonConstants;
import com.inno72.common.Result;
import com.inno72.common.Results;
import com.inno72.common.SessionData;
import com.inno72.common.StringUtil;
import com.inno72.system.model.Inno72User;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Resource;


/**
 * Created by CodeGenerator on 2018/07/19.
 */
@Service
@Transactional
public class CheckFaultServiceImpl extends AbstractService<Inno72CheckFault> implements CheckFaultService {
	private static Logger logger = LoggerFactory.getLogger(CheckFaultServiceImpl.class);
	
	
    @Resource
    private Inno72CheckFaultMapper inno72CheckFaultMapper;
    @Resource
    private Inno72CheckFaultRemarkMapper inno72CheckFaultRemarkMapper;
    

	@Override
	public List<Inno72CheckFault> findByPage(String keyword,String status,String type,String startTime,String endTime) {
		Map<String, Object> params = new HashMap<String, Object>();
		keyword=Optional.ofNullable(keyword).map(a->a.replace("'", "")).orElse(keyword);
		params.put("status", status);
		params.put("keyword", keyword);
		params.put("type", type);
		params.put("startTime", startTime);
		if (StringUtil.isNotBlank(endTime)) {
			params.put("endTime", endTime +" 23:59:59");
		}
		
		return inno72CheckFaultMapper.selectByPage(params);
	}
	
	@Override
	public Result<String> faultAnswer(String id,String remark) {
		
		try {
			SessionData session = CommonConstants.SESSION_DATA;
			Inno72User mUser = Optional.ofNullable(session).map(SessionData::getUser).orElse(null);
			if (mUser == null) {
				logger.info("登陆用户为空");
				return Results.failure("未找到用户登录信息");
			}
			if (StringUtil.isBlank(remark)) {
				return Results.failure("回复内容不能为空！");
			}
			String userId = Optional.ofNullable(mUser).map(Inno72User::getId).orElse(null);
			Inno72CheckFaultRemark faultRemark=  new  Inno72CheckFaultRemark();
		
			faultRemark.setId(StringUtil.getUUID());
			faultRemark.setFaultId(id);
			faultRemark.setRemark(remark);
			faultRemark.setUserId(userId);
			faultRemark.setType(2);
			faultRemark.setCreateTime(LocalDateTime.now());
			
			inno72CheckFaultRemarkMapper.insert(faultRemark);
			
		} catch (Exception e) {
			logger.info(e.getMessage());
			return Results.failure("操作失败");
		}
		return Results.success("操作成功");
	}
	
	@Override
	public Inno72CheckFaultVo selectFaultDetail(String id) {
		return inno72CheckFaultMapper.selectFaultDetail(id);
	}


}
