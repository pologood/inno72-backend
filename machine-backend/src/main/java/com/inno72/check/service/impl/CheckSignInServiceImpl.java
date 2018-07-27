package com.inno72.check.service.impl;

import com.inno72.check.mapper.Inno72CheckSignInMapper;
import com.inno72.check.model.Inno72CheckSignIn;
import com.inno72.check.service.CheckSignInService;
import com.inno72.check.vo.Inno72CheckUserVo;
import com.inno72.common.AbstractService;
import com.inno72.common.ExportExcel;
import com.inno72.common.StringUtil;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;


/**
 * Created by CodeGenerator on 2018/07/20.
 */
@Service
@Transactional
public class CheckSignInServiceImpl extends AbstractService<Inno72CheckSignIn> implements CheckSignInService {
    @Resource
    private Inno72CheckSignInMapper inno72CheckSignInMapper;
    //表格
    public static final String[] USERCHARGE={"用户名","手机号","公司","点位","机器编码","打卡时间"};
    public static final String[] USERCOLUMN={"name","phone","enterprise","localeName","machineCode","createTime"};


	@Override
	public List<Inno72CheckUserVo> findByPage(String code,String keyword) {
		Map<String, Object> params = new HashMap<String, Object>();
		keyword=Optional.ofNullable(keyword).map(a->a.replace("'", "")).orElse(keyword);
		if (StringUtil.isNotEmpty(code)) {
			int num = StringUtil.getAreaCodeNum(code);
			if (num < 4) {
				num = 3;
			}
			String likeCode = code.substring(0, num);
			params.put("code", likeCode);
			params.put("num", num);
		}
		params.put("keyword", keyword);
		
		return inno72CheckSignInMapper.selectByPage(params);
	}
	
	/**
	 * 导出excel表格
	 */
	@Override
	public void getExportExcel(String code,String keyword, HttpServletResponse response) {
		// 获取渠道所有数据
		
		Map<String, Object> params = new HashMap<String, Object>();
		keyword=Optional.ofNullable(keyword).map(a->a.replace("'", "")).orElse(keyword);
		if (StringUtil.isNotEmpty(code)) {
			int num = StringUtil.getAreaCodeNum(code);
			if (num < 4) {
				num = 3;
			}
			String likeCode = code.substring(0, num);
			params.put("code", likeCode);
			params.put("num", num);
		}
		params.put("keyword", keyword);
		List<Inno72CheckUserVo> list =inno72CheckSignInMapper.selectByPage(params);
		int size = list.size();
		if (list != null && size > 0) {
			ExportExcel<Inno72CheckUserVo> ee = new ExportExcel<Inno72CheckUserVo>();
			// 导出excel
			ee.setResponseHeader(USERCHARGE, USERCOLUMN, list, response, "打卡记录");
		}
	}

    
    
    
}
