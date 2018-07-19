package com.inno72.machine.service;

import com.inno72.goods.model.GoodsChannelBean;
import com.inno72.machine.model.Inno72SupplyChannelStatus;
import com.inno72.common.Service;

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
