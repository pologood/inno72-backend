package com.inno72.gameUser.mapper;

import java.util.List;

import com.inno72.common.Mapper;
import com.inno72.gameUser.model.Inno72GameUserChannel;

@org.apache.ibatis.annotations.Mapper
public interface Inno72GameUserChannelMapper extends Mapper<Inno72GameUserChannel> {
	List<Inno72GameUserChannel> selectForPage(Inno72GameUserChannel gameUserChannel);
}
