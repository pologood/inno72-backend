package com.inno72.machine.service.impl;

import com.inno72.check.model.Inno72CheckUser;
import com.inno72.common.*;
import com.inno72.machine.mapper.Inno72SupplyChannelHistoryMapper;
import com.inno72.machine.mapper.Inno72SupplyChannelOrderMapper;
import com.inno72.machine.model.Inno72SupplyChannelHistory;
import com.inno72.machine.service.SupplyChannelHistoryService;
import com.inno72.machine.vo.SupplyOrderVo;
import com.inno72.machine.vo.SupplyRequestVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

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

    @Override
    public Result<SupplyOrderVo> detail(SupplyRequestVo vo) {
        Map<String,Object> map = new HashMap<>();
        map.put("batchNo",vo.getBatchNo());
        SupplyOrderVo supplyOrderVo = new SupplyOrderVo();
        List<Inno72SupplyChannelHistory> historyList = inno72SupplyChannelHistoryMapper.getSupplyOrderGoods(map);
        List<Inno72SupplyChannelHistory> resultLsit = new ArrayList<>();
        if(historyList != null && historyList.size()>0){
            supplyOrderVo.setBatchNo(vo.getBatchNo());
            supplyOrderVo.setMachineCode(historyList.get(0).getMachineCode());
            supplyOrderVo.setCreateTime(historyList.get(0).getCreateTime());
            supplyOrderVo.setLocaleStr(historyList.get(0).getLocaleStr());
            supplyOrderVo.setMachineId(historyList.get(0).getMachineId());
            Set<String> set = new HashSet<>();
            for(Inno72SupplyChannelHistory history:historyList){
                String goodsName = history.getGoodsName();
                if(!set.contains(goodsName)){
                    set.add(goodsName);
                    int count = 0;
                    for(Inno72SupplyChannelHistory his:historyList){
                        String name = history.getGoodsName();
                        if(name.equals(goodsName)){
                            count += his.getSubCount();
                        }
                    }
                    history.setSubCount(count);
                }
                resultLsit.add(history);
            }
            supplyOrderVo.setHistoryList(resultLsit);
        }
        return ResultGenerator.genSuccessResult(supplyOrderVo);
    }
}
