package com.inno72.game.service.impl;

import com.inno72.common.AbstractService;
import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;
import com.inno72.common.StringUtil;
import com.inno72.game.mapper.Inno72GameMapper;
import com.inno72.game.model.Inno72Game;
import com.inno72.game.service.GameService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Resource;


/**
 * Created by CodeGenerator on 2018/06/29.
 */
@Service
@Transactional
public class GameServiceImpl extends AbstractService<Inno72Game> implements GameService {
	
	private static Logger logger = LoggerFactory.getLogger(GameServiceImpl.class);
    @Resource
    private Inno72GameMapper inno72GameMapper;

	@Override
	public void save(Inno72Game model) {
		logger.info("----------------游戏添加--------------");
		
		model.setId(StringUtil.getUUID());
		model.setCreateId("");
		model.setUpdateId("");
		
		super.save(model);
	}

	@Override
	public Result<String> delById(String id) {
		logger.info("----------------游戏删除--------------");
		
		int nmu=inno72GameMapper.selectIsUseing(id);
		if (nmu > 0) {
			return ResultGenerator.genFailResult("游戏使用中，不能删除！");
		}
		
		Inno72Game model = inno72GameMapper.selectByPrimaryKey(id);
		model.setIsDelete(1);
		model.setUpdateId("");
		super.update(model);
		return ResultGenerator.genSuccessResult();
	}

	@Override
	public void update(Inno72Game model) {
		logger.info("----------------游戏更新--------------");
		model.setUpdateId("");
		super.update(model);
	}

	@Override
	public List<Inno72Game> findByPage(String code,String keyword) {
		logger.info("----------------游戏分页列表--------------");
		keyword=Optional.ofNullable(keyword).map(a->a.replace("'", "")).orElse(keyword);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("keyword", keyword);
		params.put("code", code);
		
		return inno72GameMapper.selectByPage(params);
	}
	
	@Override
	public Result<String> matchMachine(String gameId,String machineIds) {
		logger.info("----------------游戏绑定机器--------------");
		try {
			List<String> mIds = Arrays.asList(machineIds.split(","));
			//删除所选机器对应的游戏
			inno72GameMapper.deleteMachineGameByMachineId(mIds);
			
			//添加所选机器与改游戏对应关系
			List<Map<String, Object>> machineGames =new ArrayList<>();
			for (String mId : mIds) {
				Map<String, Object> mg = new HashMap<String, Object>();
				mg.put("gameId", gameId);
				mg.put("mId", mId);
				mg.put("id", StringUtil.getUUID());
				machineGames.add(mg);
			}
			inno72GameMapper.addMachineGame(machineGames);
			
		} catch (Exception e) {
			return ResultGenerator.genFailResult("操作失败！");
		}
		
		return ResultGenerator.genSuccessResult();
	}
    
    
    
    

}
