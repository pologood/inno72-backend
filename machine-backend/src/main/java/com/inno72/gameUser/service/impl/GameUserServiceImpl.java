package com.inno72.gameUser.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inno72.common.AbstractService;
import com.inno72.common.StringUtil;
import com.inno72.gameUser.mapper.Inno72GameUserChannelMapper;
import com.inno72.gameUser.mapper.Inno72GameUserMapper;
import com.inno72.gameUser.model.Inno72GameUser;
import com.inno72.gameUser.service.GameUserService;
import com.inno72.job.task.vo.Inno72UserProfile;

@Service
@Transactional
public class GameUserServiceImpl extends AbstractService<Inno72GameUser> implements GameUserService {

	@Resource
	private Inno72GameUserMapper inno72GameUserMapper;

	@Resource
	private Inno72GameUserChannelMapper inno72GameUserChannelMapper;

	@Autowired
	private MongoOperations mongoTpl;

	@Override
	public List<Map<String, Object>> findForPage(String code, String sex, String time, String keyword) {

		Map<String, Object> params = new HashMap<String, Object>();
		keyword = Optional.ofNullable(keyword).map(a -> a.replace("'", "")).orElse(keyword);
		params.put("keyword", keyword);
		if (StringUtil.isNotEmpty(code)) {
			int num = StringUtil.getAreaCodeNum(code);
			String likeCode = code.substring(0, num);
			params.put("code", likeCode);
			params.put("num", num);
		}
		if (StringUtil.isNotBlank(time)) {
			params.put("beginTime", time.trim() + " 00:00:00");
			params.put("endTime", time.trim() + " 23:59:59");
		}
		// params.put("sex", sex);

		List<Map<String, Object>> gameUserChannelList = inno72GameUserMapper.selectByPage(params);
		for (Map<String, Object> map : gameUserChannelList) {
			Query query = new Query();
			query.addCriteria(Criteria.where("userId").is(map.get("id")));
			List<Inno72UserProfile> ts = mongoTpl.find(query, Inno72UserProfile.class, "inno72UserProfile");
			StringBuffer tagName = new StringBuffer();
			if (ts != null && !ts.isEmpty()) {
				Inno72UserProfile userProfile = ts.get(0);
				if (StringUtil.isNotEmpty(userProfile.getGender())) {
					tagName.append(userProfile.getGender()).append(" ");
					map.put("sex", userProfile.getGender());
				}
				if (StringUtil.isNotEmpty(userProfile.getAge())) {
					tagName.append(userProfile.getAge()).append(" ");
					map.put("age", userProfile.getAge());
				}
				if (StringUtil.isNotEmpty(userProfile.getShopping())) {
					tagName.append(userProfile.getShopping()).append(" ");
				}
				if (StringUtil.isNotEmpty(userProfile.getInteraction())) {
					tagName.append(userProfile.getInteraction()).append(" ");
				}
				if (StringUtil.isNotEmpty(userProfile.getAttempt())) {
					tagName.append(userProfile.getAttempt()).append(" ");

				}

				if (StringUtil.isNotEmpty(userProfile.getCity())) {
					tagName.append(userProfile.getCity()).append(" ");
				}
				if (StringUtil.isNotEmpty(userProfile.getPos())) {
					tagName.append(userProfile.getPos()).append(" ");
				}
				if (StringUtil.isNotEmpty(userProfile.getGameTalent())) {
					tagName.append(userProfile.getGameTalent()).append(" ");
				}
				if (StringUtil.isNotEmpty(userProfile.getGameNovice())) {
					tagName.append(userProfile.getGameNovice()).append(" ");
				}

				if (StringUtil.isNotEmpty(userProfile.getBuypower())) {
					tagName.append(userProfile.getBuypower()).append(" ");
				}

			}
			map.put("tagName", tagName.toString());
		}

		return gameUserChannelList;
	}

	@Override
	public List<Map<String, Object>> areaList(String userId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userId);
		List<Map<String, Object>> areaList = inno72GameUserMapper.selectAreaList(params);
		return areaList;
	}

}
