package com.inno72.gameUser.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inno72.common.AbstractService;
import com.inno72.common.StringUtil;
import com.inno72.gameUser.mapper.Inno72GameUserChannelMapper;
import com.inno72.gameUser.mapper.Inno72GameUserMapper;
import com.inno72.gameUser.model.Inno72GameUser;
import com.inno72.gameUser.service.GameUserService;

@Service
@Transactional
public class GameUserServiceImpl extends AbstractService<Inno72GameUser> implements GameUserService {

	@Resource
	private Inno72GameUserMapper inno72GameUserMapper;

	@Resource
	private Inno72GameUserChannelMapper inno72GameUserChannelMapper;

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
		params.put("sex", sex);

		List<Map<String, Object>> gameUserChannelList = inno72GameUserMapper.selectByPage(params);
		return gameUserChannelList;
	}

}
