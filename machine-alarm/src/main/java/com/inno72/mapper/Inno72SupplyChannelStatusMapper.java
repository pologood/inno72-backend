package com.inno72.mapper;

import com.inno72.common.Mapper;
import com.inno72.model.Inno72SupplyChannelStatus;

import java.util.List;

/**
 * @author
 */
@org.apache.ibatis.annotations.Mapper
public interface Inno72SupplyChannelStatusMapper extends Mapper<Inno72SupplyChannelStatus> {

    /**
     * find channel errorInfo
     *
     * @param
     * @return : List<Inno72SupplyChannelStatus>
     */
    List<Inno72SupplyChannelStatus> getChannelErrorDetail();
}