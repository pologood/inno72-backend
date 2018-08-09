package com.inno72.machine.service.impl;

import com.inno72.common.AbstractService;
import com.inno72.common.StringUtil;
import com.inno72.machine.mapper.Inno72SupplyChannelHistoryMapper;
import com.inno72.machine.mapper.Inno72SupplyChannelOrderMapper;
import com.inno72.machine.model.Inno72SupplyChannelHistory;
import com.inno72.machine.service.SupplyChannelHistoryService;
import com.inno72.machine.vo.SupplyOrderVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class SupplyChannelHistoryServiceImpl extends AbstractService<Inno72SupplyChannelHistory> implements SupplyChannelHistoryService {
    @Resource
    private Inno72SupplyChannelHistoryMapper inno72SupplyChannelHistoryMapper;

    @Resource
    private Inno72SupplyChannelOrderMapper inno72SupplyChannelOrderMapper;
    @Override
    public List<SupplyOrderVo> list(SupplyOrderVo supplyOrderVo) {
        Map<String,Object> map = new HashMap<>();
        String areaCode = supplyOrderVo.getAreaCode();
        String beginTime = supplyOrderVo.getBeginTime();
        String endTime = supplyOrderVo.getEndTime();
        String keyword = supplyOrderVo.getKeyword();
        if(StringUtil.isNotEmpty(areaCode) && StringUtil.isNotEmpty(areaCode.trim())){
            map.put("areaCode",areaCode.trim());
        }
        if(StringUtil.isNotEmpty(beginTime) && StringUtil.isNotEmpty(beginTime.trim())){
            map.put("beginTime",beginTime.trim()+" 00:00:00");
        }
        if(StringUtil.isNotEmpty(endTime) && StringUtil.isNotEmpty(endTime.trim())){
            map.put("endTime",endTime.trim()+" 23:59:59");
        }
        if(StringUtil.isNotEmpty(keyword) && StringUtil.isNotEmpty(keyword.trim())){
            map.put("keyword",keyword.trim());
        }
        List<SupplyOrderVo> list = inno72SupplyChannelOrderMapper.getOrderList(map);
        return list;
    }
}
