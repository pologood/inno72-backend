package com.inno72.service.impl;

import com.inno72.mapper.Inno72GameMapper;
import com.inno72.model.Inno72Game;
import com.inno72.service.GameService;
import com.inno72.common.AbstractService;
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
