package com.inno72.gameUser.service;

import com.inno72.common.Service;
import com.inno72.gameUser.model.Inno72GameUser;
import com.inno72.gameUser.model.Inno72GameUserChannel;
import tk.mybatis.mapper.entity.Condition;

import java.util.List;

public interface GameUserService extends Service<Inno72GameUser> {
    List<Inno72GameUserChannel> findForPage(Inno72GameUserChannel gameUserChannel);
}
