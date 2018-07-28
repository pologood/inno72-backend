package com.inno72.service;

import com.inno72.common.Service;
import com.inno72.model.GoodsChannelBean;
import com.inno72.model.Inno72SupplyChannelStatus;

import java.util.List;


/**
 * Created by CodeGenerator on 2018/07/18.
 */
public interface SupplyChannelStatusService extends Service<Inno72SupplyChannelStatus> {

    /**
     * @param goodsChannelStatus
     * @return
     * @author wxt
     */
    List<GoodsChannelBean> getChannelErrorDetail(List<GoodsChannelBean> goodsChannelStatus);

}
