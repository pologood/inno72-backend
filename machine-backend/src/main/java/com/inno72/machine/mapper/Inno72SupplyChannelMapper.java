package com.inno72.machine.mapper;

import java.util.List;

import com.inno72.common.Mapper;
import com.inno72.machine.model.Inno72SupplyChannel;
import com.inno72.machine.vo.ChannelListVo;

@org.apache.ibatis.annotations.Mapper
public interface Inno72SupplyChannelMapper extends Mapper<Inno72SupplyChannel> {

	List<ChannelListVo> selectChannelListByMachineId(String id);

}