package com.inno72.machine.service.impl;

import com.inno72.common.AbstractService;
import com.inno72.machine.mapper.Inno72SupplyChannelStatusMapper;
import com.inno72.machine.model.Inno72SupplyChannelStatus;
import com.inno72.machine.service.SupplyChannelStatusService;
import com.inno72.project.model.GoodsChannelBean;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by CodeGenerator on 2018/07/18.
 */
@Service
@Transactional
public class SupplyChannelStatusServiceImpl extends AbstractService<Inno72SupplyChannelStatus> implements SupplyChannelStatusService {
    @Resource
    private Inno72SupplyChannelStatusMapper inno72SupplyChannelStatusMapper;

    @Override
    public List<GoodsChannelBean> getChannelErrorDetail(List<GoodsChannelBean> goodsChannelStatus) {
        //字典表
        List<Inno72SupplyChannelStatus> list = inno72SupplyChannelStatusMapper.getChannelErrorDetail();
        //转map
        Map<Integer, String> dictMap = new HashMap<>();
        for (Inno72SupplyChannelStatus inno72SupplyChannelStatus : list) {
            Integer errorCode = Integer.valueOf(inno72SupplyChannelStatus.getCode());
            String result = inno72SupplyChannelStatus.getResult();
            dictMap.put(errorCode, result);
        }
        //定义返回值
        List<GoodsChannelBean> returnList = new ArrayList<>();
        for (GoodsChannelBean goodsChannelBean : goodsChannelStatus) {
            String errorDesc = dictMap.get(Integer.valueOf(goodsChannelBean.getGoodsChannelStatus()));
            goodsChannelBean.setDescription(errorDesc);
            returnList.add(goodsChannelBean);
        }

        return returnList;
    }

}
