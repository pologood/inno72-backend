package com.inno72.service.impl;

import com.inno72.common.AbstractService;
import com.inno72.mapper.Inno72SupplyChannelStatusMapper;
import com.inno72.model.GoodsChannelBean;
import com.inno72.model.Inno72SupplyChannelStatus;
import com.inno72.service.SupplyChannelStatusService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by CodeGenerator on 2018/07/18.
 * @author
 */
@Service
@Transactional
public class SupplyChannelStatusServiceImpl extends AbstractService<Inno72SupplyChannelStatus> implements SupplyChannelStatusService {
    @Resource
    private Inno72SupplyChannelStatusMapper inno72SupplyChannelStatusMapper;

    @Override
    public List<GoodsChannelBean> getChannelErrorDetail(List<GoodsChannelBean> goodsChannelStatus) {
        List<Inno72SupplyChannelStatus> list = inno72SupplyChannelStatusMapper.getChannelErrorDetail();
        Map<Integer, String> dictMap = new HashMap<>();
        for (Inno72SupplyChannelStatus inno72SupplyChannelStatus : list) {
            Integer errorCode = Integer.valueOf(inno72SupplyChannelStatus.getCode());
            String result = inno72SupplyChannelStatus.getResult();
            dictMap.put(errorCode, result);
        }
        List<GoodsChannelBean> returnList = new ArrayList<>();
        for (GoodsChannelBean goodsChannelBean : goodsChannelStatus) {
            String errorDesc = dictMap.get(Integer.valueOf(goodsChannelBean.getGoodsChannelStatus()));
            goodsChannelBean.setDescription(errorDesc);
            returnList.add(goodsChannelBean);
        }

        return returnList;
    }

    @Override
    public List<Inno72SupplyChannelStatus> findByPage(Object condition) {
        return null;
    }
}
