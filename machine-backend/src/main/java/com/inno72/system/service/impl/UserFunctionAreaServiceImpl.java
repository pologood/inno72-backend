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
import com.inno72.share.mapper.Inno72AdminAreaMapper;
import com.inno72.share.model.Inno72AdminArea;
import com.inno72.system.mapper.Inno72UserFunctionAreaMapper;
import com.inno72.system.model.Inno72User;
import com.inno72.system.model.Inno72UserFunctionArea;
import com.inno72.system.service.UserFunctionAreaService;
import com.inno72.system.vo.AreaTreeResultVo;
import com.inno72.system.vo.AreaTreeVo;
import com.inno72.system.vo.UserAreaDataVo;

import tk.mybatis.mapper.entity.Condition;

/**
 * Created by CodeGenerator on 2018/09/03.
 */
@Service
@Transactional
public class UserFunctionAreaServiceImpl extends AbstractService<Inno72UserFunctionArea>
		implements UserFunctionAreaService {
	private static Logger logger = LoggerFactory.getLogger(UserFunctionAreaServiceImpl.class);
	@Resource
	private Inno72UserFunctionAreaMapper inno72UserFunctionAreaMapper;
	@Resource
	private Inno72AdminAreaMapper inno72AdminAreaMapper;

	@Override
	public List<Inno72UserFunctionArea> list(String userId) {
		Condition condition = new Condition(Inno72UserFunctionArea.class);
		condition.createCriteria().andEqualTo("userId", userId);
		List<Inno72UserFunctionArea> userFunctionArea = inno72UserFunctionAreaMapper.selectByCondition(condition);

		return userFunctionArea;
	}

	@Override
	public Result<Object> updateFunctionArea(UserAreaDataVo userArea) {

		try {
			SessionData session = SessionUtil.sessionData.get();
			Inno72User mUser = Optional.ofNullable(session).map(SessionData::getUser).orElse(null);
			if (mUser == null) {
				logger.info("登陆用户为空");
				return Results.failure("未找到用户登录信息");
			}
			String mUserId = Optional.ofNullable(mUser).map(Inno72User::getId).orElse(null);

			String userId = userArea.getUserId();
			if (StringUtil.isBlank(userId)) {
				logger.info("请选择人员");
				return Results.failure("请选择人员");
			}

			Condition condition = new Condition(Inno72UserFunctionArea.class);
			condition.createCriteria().andEqualTo("userId", userId);
			inno72UserFunctionAreaMapper.deleteByCondition(condition);

			List<Inno72AdminArea> areas = userArea.getAreaList();
			// 去除子节点
			List<Inno72AdminArea> sonArea = new ArrayList<Inno72AdminArea>();
			if (null != areas && areas.size() > 1) {
				List<Inno72AdminArea> level1 = new ArrayList<Inno72AdminArea>();
				List<Inno72AdminArea> level2 = new ArrayList<Inno72AdminArea>();
				List<Inno72AdminArea> level3 = new ArrayList<Inno72AdminArea>();

				for (Inno72AdminArea inno72AdminArea : areas) {
					if (inno72AdminArea.getLevel() == 1) {
						level1.add(inno72AdminArea);
					} else if (inno72AdminArea.getLevel() == 2) {
						level2.add(inno72AdminArea);
					} else if (inno72AdminArea.getLevel() == 3) {
						level3.add(inno72AdminArea);
					}
				}

				if (level1.size() > 0) {
					for (Inno72AdminArea inno72AdminArea : level1) {
						String areaCode = inno72AdminArea.getCode().substring(0, 2);
						if (level2.size() > 0) {
							for (Inno72AdminArea inno72AdminArea2 : level2) {
								String areaCode2 = inno72AdminArea2.getCode().substring(0, 2);
								if (areaCode.equals(areaCode2)) {
									sonArea.add(inno72AdminArea2);
								}
							}
						}
						if (level3.size() > 0) {
							for (Inno72AdminArea inno72AdminArea3 : level3) {
								String areaCode3 = inno72AdminArea3.getCode().substring(0, 2);
								if (areaCode.equals(areaCode3)) {
									sonArea.add(inno72AdminArea3);
								}
							}
						}
					}
				}

				if (level2.size() > 0) {
					for (Inno72AdminArea inno72AdminArea2 : level2) {
						String areaCode2 = inno72AdminArea2.getCode().substring(0, 4);
						for (Inno72AdminArea inno72AdminArea3 : level3) {
							String areaCode3 = inno72AdminArea3.getCode().substring(0, 4);
							if (areaCode2.equals(areaCode3) && !sonArea.contains(inno72AdminArea3)) {
								sonArea.add(inno72AdminArea3);
							}
						}
					}
				}

			}
			System.out.println(sonArea);
			if (sonArea.size() > 0) {
				areas.removeAll(sonArea);
				return Results.warn("有重复区域", 0, areas);
			}

			if (null != areas && areas.size() > 0) {
				List<Inno72UserFunctionArea> insertList = new ArrayList<>();
				areas.forEach(area -> {
					Inno72UserFunctionArea fa = new Inno72UserFunctionArea();
					fa.setId(com.inno72.common.StringUtil.getUUID());
					fa.setUserId(userId);
					fa.setCode(area.getCode());
					fa.setName(area.getName());
					fa.setProvince(area.getProvince());
					fa.setCity(area.getCity());
					fa.setDistrict(area.getDistrict());
					fa.setLevel(area.getLevel());
					fa.setCreateTime(LocalDateTime.now());
					fa.setCreateId(mUserId);
					insertList.add(fa);
				});
				inno72UserFunctionAreaMapper.insertUserFunctionAreaList(insertList);
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.info(e.getMessage());
			return Results.failure("操作失败");
		}
		return Results.success();
	}

	@Override
	public Result<AreaTreeResultVo> findAllAreaTree(String userId) {
		AreaTreeResultVo areaResult = new AreaTreeResultVo();
		if (!StringUtil.isEmpty(userId)) {
			Condition condition1 = new Condition(Inno72UserFunctionArea.class);
			condition1.createCriteria().andEqualTo("userId", userId);
			List<Inno72UserFunctionArea> userFunctionArea = inno72UserFunctionAreaMapper.selectByCondition(condition1);
			List<String> areaCodes = new ArrayList<>();
			for (Inno72UserFunctionArea area : userFunctionArea) {
				areaCodes.add(area.getCode());
			}
			areaResult.setAreaCodes(areaCodes);
		}
		AreaTreeVo areaTree = new AreaTreeVo();
		areaTree.setName("");
		areaTree.setCode("");
		Condition condition = new Condition(Inno72AdminArea.class);
		condition.createCriteria().andEqualTo("level", 1);
		List<Inno72AdminArea> first = inno72AdminAreaMapper.selectByCondition(condition);
		List<AreaTreeVo> firstVoList = new ArrayList<>();
		for (Inno72AdminArea areaFirst : first) {
			AreaTreeVo areaFirstVo = new AreaTreeVo();
			areaFirstVo.setCode(areaFirst.getCode());
			areaFirstVo.setName(areaFirst.getName());
			areaFirstVo.setProvince(areaFirst.getProvince());
			areaFirstVo.setCity(areaFirst.getCity());
			firstVoList.add(areaFirstVo);
			condition = new Condition(Inno72AdminArea.class);
			condition.createCriteria().andEqualTo("level", 2).andEqualTo("parentCode", areaFirst.getCode());
			List<Inno72AdminArea> second = inno72AdminAreaMapper.selectByCondition(condition);
			List<AreaTreeVo> secondVoList = new ArrayList<>();
			for (Inno72AdminArea areaSecond : second) {
				AreaTreeVo areaSecondVo = new AreaTreeVo();
				areaSecondVo.setCode(areaSecond.getCode());
				areaSecondVo.setName(areaSecond.getName());
				areaSecondVo.setProvince(areaSecond.getProvince());
				areaSecondVo.setCity(areaSecond.getCity());
				secondVoList.add(areaSecondVo);

				condition = new Condition(Inno72AdminArea.class);
				condition.createCriteria().andEqualTo("level", 3).andEqualTo("parentCode", areaSecond.getCode());
				List<Inno72AdminArea> third = inno72AdminAreaMapper.selectByCondition(condition);
				List<AreaTreeVo> thirdVo = new ArrayList<>();
				for (Inno72AdminArea areaThird : third) {
					AreaTreeVo areaThirdVo = new AreaTreeVo();
					areaThirdVo.setCode(areaThird.getCode());
					areaThirdVo.setName(areaThird.getName());
					areaThirdVo.setProvince(areaThird.getProvince());
					areaThirdVo.setCity(areaThird.getCity());
					thirdVo.add(areaThirdVo);
				}
				areaSecondVo.setChildren(thirdVo);
			}
			areaFirstVo.setChildren(secondVoList);
		}
		areaTree.setChildren(firstVoList);
		areaResult.setAreaTree(areaTree);
		return Results.success(areaResult);
	}

}
