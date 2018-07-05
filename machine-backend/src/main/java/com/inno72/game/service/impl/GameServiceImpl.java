package com.inno72.game.service.impl;

import com.inno72.common.AbstractService;
import com.inno72.game.mapper.Inno72GameMapper;
import com.inno72.game.model.Inno72Game;
import com.inno72.game.service.GameService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by CodeGenerator on 2018/06/29.
 */
@Service
@Transactional
public class GameServiceImpl extends AbstractService<Inno72Game> implements GameService {
    @Resource
    private Inno72GameMapper inno72GameMapper;

}
