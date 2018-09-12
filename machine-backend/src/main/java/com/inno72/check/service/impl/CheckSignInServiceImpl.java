package com.inno72.check.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inno72.check.mapper.Inno72CheckSignInMapper;
import com.inno72.check.model.Inno72CheckSignIn;
import com.inno72.check.service.CheckSignInService;
import com.inno72.check.vo.Inno72CheckUserVo;
import com.inno72.common.AbstractService;
import com.inno72.common.ExportExcel;
import com.inno72.common.StringUtil;

/**
 * Created by CodeGenerator on 2018/07/20.
 */
@Service
@Transactional
public class CheckSignInServiceImpl extends AbstractService<Inno72CheckSignIn> implements CheckSignInService {
	@Resource
	private Inno72CheckSignInMapper inno72CheckSignInMapper;
	// 表格
	public static final String[] USERCHARGE = { "用户名", "手机号", "公司", "点位", "机器编码", "打卡时间" };
	public static final String[] USERCOLUMN = { "name", "phone", "enterprise", "localeName", "machineCode",
			"createTime" };

	@Override
	public List<Inno72CheckUserVo> findByPage(String code, String keyword, String startTime, String endTime) {
		Map<String, Object> params = new HashMap<String, Object>();
		keyword = Optional.ofNullable(keyword).map(a -> a.replace("'", "")).orElse(keyword);
		if (StringUtil.isNotEmpty(code)) {
			int num = StringUtil.getAreaCodeNum(code);

			String likeCode = code.substring(0, num);
			params.put("code", likeCode);
			params.put("num", num);
		}
		if (StringUtil.isNotBlank(startTime) && StringUtil.isNotBlank(endTime)) {
			params.put("startTime", startTime);
			params.put("endTime", endTime + " 23:59:59");
		}
		params.put("keyword", keyword);

		return inno72CheckSignInMapper.selectSignInByPage(params);
	}

	/**
	 * 导出excel表格
	 */
	@Override
	public void getExportExcel(String code, String keyword, String startTime, String endTime,
			HttpServletResponse response) {

		Map<String, Object> params = new HashMap<String, Object>();
		keyword = Optional.ofNullable(keyword).map(a -> a.replace("'", "")).orElse(keyword);
		if (StringUtil.isNotEmpty(code)) {
			int num = StringUtil.getAreaCodeNum(code);
			String likeCode = code.substring(0, num);
			params.put("code", likeCode);
			params.put("num", num);
		}
		if (StringUtil.isNotBlank(startTime) && StringUtil.isNotBlank(endTime)) {
			params.put("startTime", startTime);
			params.put("endTime", endTime + " 23:59:59");
		}
		params.put("keyword", keyword);
		List<Inno72CheckUserVo> list = inno72CheckSignInMapper.selectSignInExportList(params);
		int size = list.size();
		if (list != null && size > 0) {
			ExportExcel<Inno72CheckUserVo> ee = new ExportExcel<Inno72CheckUserVo>();
			// 导出excel
			ee.setResponseHeader(USERCHARGE, USERCOLUMN, list, response, "打卡记录");
		}
	}

}
