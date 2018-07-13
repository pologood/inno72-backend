package com.inno72.gameUser.service.impl;

import com.inno72.common.AbstractService;
import com.inno72.gameUser.mapper.Inno72GameUserChannelMapper;
import com.inno72.gameUser.mapper.Inno72GameUserMapper;
import com.inno72.gameUser.model.Inno72GameUser;
import com.inno72.gameUser.model.Inno72GameUserChannel;
import com.inno72.gameUser.service.GameUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
@Transactional
public class GameUserServiceImpl extends AbstractService<Inno72GameUser> implements GameUserService {

    @Resource
    private Inno72GameUserMapper inno72GameUserMapper;

    @Resource
    private Inno72GameUserChannelMapper inno72GameUserChannelMapper;
    @Override
    public List<Inno72GameUserChannel> findForPage(Inno72GameUserChannel gameUserChannel) {
        List<Inno72GameUserChannel> gameUserChannelList = inno72GameUserChannelMapper.selectForPage(gameUserChannel);
        return gameUserChannelList;
    }

}
