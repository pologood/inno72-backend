package com.inno72.check.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
import com.inno72.common.Result;
import com.inno72.common.Results;
import com.inno72.common.SessionData;
import com.inno72.common.SessionUtil;
import com.inno72.common.StringUtil;
import com.inno72.system.model.Inno72User;

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
	public List<Inno72CheckUserVo> findByPage(String code, String keyword, String status, String startTime,
			String endTime) {
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
		params.put("status", status);

		return inno72CheckSignInMapper.selectSignInByPage(params);
	}

	/**
	 * 导出excel表格
	 */
	@Override
	public void getExportExcel(String code, String keyword, String status, String startTime, String endTime,
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
		params.put("status", status);
		List<Inno72CheckUserVo> list = inno72CheckSignInMapper.selectSignInExportList(params);
		int size = list.size();
		if (list != null && size > 0) {
			ExportExcel<Inno72CheckUserVo> ee = new ExportExcel<Inno72CheckUserVo>();
			// 导出excel
			ee.setResponseHeader(USERCHARGE, USERCOLUMN, list, response, "打卡记录");
		}
	}

	@Override
	public Result<String> updateStatus(String ids, String status) {

		SessionData session = SessionUtil.sessionData.get();
		Inno72User mUser = Optional.ofNullable(session).map(SessionData::getUser).orElse(null);
		if (mUser == null) {
			return Results.failure("未找到用户登录信息");
		}
		if (StringUtil.isBlank(ids)) {
			return Results.failure("请选择要操作记录");
		}
		if (StringUtil.isBlank(status)) {
			return Results.failure("标为是否有效");
		}
		try {
			String[] idArray = ids.split(",");
			LocalDate now = LocalDate.now();
			int n = now.getDayOfMonth();
			for (String id : idArray) {
				Inno72CheckSignIn signIn = inno72CheckSignInMapper.selectByPrimaryKey(id);
				// 判断时间 当月可修改，5号以后不可修改上月数据
				LocalDateTime signInTime = signIn.getCreateTime();
				if (n >= 5 && !now.getMonth().equals(signInTime.getMonth())) {
					return Results.failure("记录已超时，不能操作");
				}
				signIn.setStatus(Integer.parseInt(status));
				signIn.setUpdateTime(LocalDateTime.now());
				inno72CheckSignInMapper.updateByPrimaryKey(signIn);
			}

		} catch (Exception e) {
			return Results.failure("操作失败");
		}
		return Results.success();
	}

}
