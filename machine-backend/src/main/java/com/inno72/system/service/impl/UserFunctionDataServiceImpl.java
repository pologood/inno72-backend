package com.inno72.system.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inno72.common.AbstractService;
import com.inno72.common.Result;
import com.inno72.common.Results;
import com.inno72.common.SessionData;
import com.inno72.common.SessionUtil;
import com.inno72.common.utils.StringUtil;
import com.inno72.dataauth.DataAutherInterceptor;
import com.inno72.system.mapper.Inno72FunctionDataMapper;
import com.inno72.system.mapper.Inno72FunctionMapper;
import com.inno72.system.mapper.Inno72UserFunctionDataMapper;
import com.inno72.system.model.Inno72Function;
import com.inno72.system.model.Inno72FunctionData;
import com.inno72.system.model.Inno72RoleFunction;
import com.inno72.system.model.Inno72User;
import com.inno72.system.model.Inno72UserFunctionData;
import com.inno72.system.service.UserFunctionDataService;
import com.inno72.system.vo.FunctionTreeResultVo;
import com.inno72.system.vo.FunctionTreeResultVo.FunctionTreeVo;
import com.inno72.system.vo.UserAreaDataVo;

import tk.mybatis.mapper.entity.Condition;

/**
 * Created by CodeGenerator on 2018/09/03.
 */
@Service
@Transactional
public class UserFunctionDataServiceImpl extends AbstractService<Inno72UserFunctionData>
		implements UserFunctionDataService {

	private static Logger logger = LoggerFactory.getLogger(UserFunctionDataServiceImpl.class);
	@Resource
	private Inno72UserFunctionDataMapper inno72UserFunctionDataMapper;
	@Resource
	private Inno72FunctionMapper inno72FunctionMapper;
	@Resource
	private Inno72FunctionDataMapper inno72FunctionDataMapper;

	@Override
	public List<Inno72UserFunctionData> list(String userId) {

		List<Inno72UserFunctionData> userFunctionData = inno72UserFunctionDataMapper.selectUserFunctionDataList(userId);
		/*
		 * for (Inno72UserFunctionData inno72UserFunctionData :
		 * userFunctionData) { inno72UserFunctionData.setFunctionLevel(3); }
		 */

		return userFunctionData;
	}

	@Override
	public Result<Object> updateFunctionData(UserAreaDataVo userData) {

		try {
			SessionData session = SessionUtil.sessionData.get();
			Inno72User mUser = Optional.ofNullable(session).map(SessionData::getUser).orElse(null);
			if (mUser == null) {
				logger.info("登陆用户为空");
				return Results.failure("未找到用户登录信息");
			}
			String mUserId = Optional.ofNullable(mUser).map(Inno72User::getId).orElse(null);

			String userId = userData.getUserId();
			if (StringUtil.isBlank(userId)) {
				logger.info("请选择人员");
				return Results.failure("请选择人员");
			}

			Condition condition = new Condition(Inno72UserFunctionData.class);
			condition.createCriteria().andEqualTo("userId", userId);
			inno72UserFunctionDataMapper.deleteByCondition(condition);

			List<Inno72UserFunctionData> functionDataList = userData.getColumnList();
			// 去除子节点
			List<Inno72UserFunctionData> sonData = new ArrayList<Inno72UserFunctionData>();
			if (null != functionDataList && functionDataList.size() > 0) {
				List<Inno72UserFunctionData> level1 = new ArrayList<Inno72UserFunctionData>();
				List<Inno72UserFunctionData> level2 = new ArrayList<Inno72UserFunctionData>();
				List<Inno72UserFunctionData> level3 = new ArrayList<Inno72UserFunctionData>();

				for (Inno72UserFunctionData functionData : functionDataList) {
					if (functionData.getFunctionLevel() == 1) {
						level1.add(functionData);
					} else if (functionData.getFunctionLevel() == 2) {
						level2.add(functionData);
					} else if (functionData.getFunctionLevel() == 3) {
						level3.add(functionData);
					}
				}
				if (level1.size() > 0) {
					for (Inno72UserFunctionData functionData : level1) {
						String level1FunctionId = functionData.getFunctionId();
						List<Inno72Function> level2All = inno72FunctionMapper
								.selectFunctionByParentId(level1FunctionId);
						for (Inno72Function functionData2 : level2All) {
							List<Inno72UserFunctionData> level3All = inno72UserFunctionDataMapper
									.selectFunctionDataByParentId(functionData2.getId());
							sonData.addAll(level3All);
						}
					}
				}
				if (level2.size() > 0) {
					for (Inno72UserFunctionData functionData2 : level2) {
						String level2FunctionId = functionData2.getFunctionId();
						List<Inno72UserFunctionData> level3All = inno72UserFunctionDataMapper
								.selectFunctionDataByParentId(level2FunctionId);
						sonData.addAll(level3All);
					}
				}
				if (level3.size() > 0) {
					for (Inno72UserFunctionData functionData3 : level3) {
						if (!sonData.contains(functionData3)) {
							sonData.add(functionData3);
						}
					}
				}

				if (level1.size() > 0 || level2.size() > 0) {
					return Results.warn("选择有父级节点，子节点将全部添加", 0, sonData);
				}

			}

			if (null != sonData && sonData.size() > 0) {
				List<Inno72UserFunctionData> insertList = new ArrayList<>();
				sonData.forEach(functionData -> {
					functionData.setId(com.inno72.common.StringUtil.getUUID());
					functionData.setUserId(userId);
					functionData.setCreateId(mUserId);
					functionData.setCreateTime(LocalDateTime.now());
					insertList.add(functionData);
				});
				inno72UserFunctionDataMapper.insertUserFunctionDataList(insertList);
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.info(e.getMessage());
			return Results.failure("操作失败");
		}
		return Results.success();
	}

	@Override
	public Result<FunctionTreeResultVo> findAllTree(String userId) {
		FunctionTreeResultVo re = new FunctionTreeResultVo();
		if (!StringUtil.isEmpty(userId)) {
			Condition condition1 = new Condition(Inno72RoleFunction.class);
			condition1.createCriteria().andEqualTo("userId", userId);
			List<Inno72UserFunctionData> functionData = inno72UserFunctionDataMapper.selectByCondition(condition1);
			List<String> rr = new ArrayList<>();
			for (Inno72UserFunctionData f : functionData) {
				rr.add(f.getId());
			}
			re.setFunctions(rr);
		}
		DataAutherInterceptor.setUserId(null);
		FunctionTreeVo root = new FunctionTreeVo();
		root.setId("XX");
		root.setTitle("机器管理系统");
		Condition condition = new Condition(Inno72Function.class);
		condition.createCriteria().andEqualTo("functionLevel", 1).andNotEqualTo("functionDepict", "首页");
		List<Inno72Function> first = inno72FunctionMapper.selectByCondition(condition);
		List<FunctionTreeVo> firstVoList = new ArrayList<>();
		for (Inno72Function funFirst : first) {
			FunctionTreeVo funFirstVo = new FunctionTreeVo();
			funFirstVo.setId(funFirst.getId());
			funFirstVo.setTitle(funFirst.getFunctionDepict());
			funFirstVo.setLevel(1);

			funFirstVo.setSelectName(funFirst.getFunctionDepict());
			firstVoList.add(funFirstVo);
			condition = new Condition(Inno72Function.class);
			condition.createCriteria().andEqualTo("functionLevel", 2).andNotEqualTo("functionDepict", "活动排期")
					.andNotEqualTo("functionDepict", "机器排期").andEqualTo("parentId", funFirst.getId());
			List<Inno72Function> second = inno72FunctionMapper.selectByCondition(condition);
			List<FunctionTreeVo> secondVoList = new ArrayList<>();
			for (Inno72Function funSecond : second) {
				FunctionTreeVo funSecondVo = new FunctionTreeVo();
				funSecondVo.setId(funSecond.getId());
				funSecondVo.setTitle(funSecond.getFunctionDepict());
				funSecondVo.setLevel(2);

				funSecondVo.setSelectName(funFirst.getFunctionDepict() + "-" + funSecond.getFunctionDepict());
				secondVoList.add(funSecondVo);
				// 获取页面列表列字段
				if (StringUtil.isNotBlank(funSecond.getId())) {
					List<Inno72FunctionData> third = inno72FunctionDataMapper
							.findFunctionDataByParentId(funSecond.getId());

					List<FunctionTreeVo> thirdVoList = new ArrayList<>();
					for (Inno72FunctionData funThird : third) {
						FunctionTreeVo funThirdVo = new FunctionTreeVo();
						funThirdVo.setId(funThird.getId());
						funThirdVo.setTitle(funThird.getFunctionDepict());
						funThirdVo.setLevel(3);

						funThirdVo.setSelectName(funFirst.getFunctionDepict() + "-" + funSecond.getFunctionDepict()
								+ "-" + funThird.getFunctionDepict());
						funThirdVo.setVoName(funThird.getVoName());
						funThirdVo.setVoColumn(funThird.getVoColumn());
						thirdVoList.add(funThirdVo);
					}
					funSecondVo.setChildren(thirdVoList);
				}

			}
			funFirstVo.setChildren(secondVoList);
		}
		root.setChildren(firstVoList);
		re.setTree(root);
		return Results.success(re);
	}
}
