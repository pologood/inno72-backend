package com.inno72.gameUser.service.impl;

import com.inno72.common.AbstractService;
import com.inno72.gameUser.mapper.Inno72GameUserMapper;
import com.inno72.gameUser.model.Inno72GameUser;
import com.inno72.gameUser.service.GameUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
@Transactional
public class GameUserServiceImpl extends AbstractService<Inno72GameUser> implements GameUserService {

    @Resource
    private Inno72GameUserMapper inno72GameUserMapper;
}
