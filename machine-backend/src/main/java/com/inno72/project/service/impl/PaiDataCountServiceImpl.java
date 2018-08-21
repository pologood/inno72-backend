package com.inno72.project.service.impl;

import com.inno72.common.AbstractService;
import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;
import com.inno72.common.StringUtil;
import com.inno72.project.mapper.Inno72PaiDataCountMapper;
import com.inno72.project.mapper.Inno72PaiNowDataMapper;
import com.inno72.project.model.Inno72PaiDataCount;
import com.inno72.project.model.Inno72PaiNowData;
import com.inno72.project.service.PaiDataCountService;
import com.inno72.project.vo.Inno72ActivityPaiDataVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class PaiDataCountServiceImpl extends AbstractService<Inno72PaiDataCount> implements PaiDataCountService {

    @Resource
    private Inno72PaiDataCountMapper inno72PaiDataCountMapper;

    @Resource
    private Inno72PaiNowDataMapper inno72PaiNowDataMapper;

    @Override
    public void addData() {
        List<Inno72PaiDataCount> list = inno72PaiDataCountMapper.selectList();
        if(list != null && list.size()>0){
            for (Inno72PaiDataCount dataCount:list) {
                dataCount.setId(StringUtil.getUUID());
                inno72PaiDataCountMapper.insertSelective(dataCount);
            }
        }
    }

    @Override
    public Result<Map<String,Object>> findList(Inno72PaiDataCount inno72PaiDataCount) {
        Map<String,Object> map = new HashMap<>();
        String activityId = inno72PaiDataCount.getActivityId();
        if(StringUtil.isNotEmpty(activityId)){
            map.put("activityId",activityId);
        }
        String activityPlanId = inno72PaiDataCount.getActivityPlanId();
        if(StringUtil.isNotEmpty(activityPlanId)){
            map.put("activityPlanId",activityPlanId);
        }
        List<Inno72PaiDataCount> list = inno72PaiDataCountMapper.selectByParam(map);
        int totalOrderCount = 0;
        int totalPayCount = 0;
        int totalGoodsCount = 0;
        int totalUserCount = 0;
        if(list != null && list.size()>0){
            for(Inno72PaiDataCount count:list){
                totalOrderCount+=count.getOrderCount();
                totalPayCount+=count.getPayCount();
                totalGoodsCount+=count.getGoodsCount();
                totalUserCount+=count.getUserCount();
            }
        }
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("list",list);
        resultMap.put("totalOrderCount",totalOrderCount);
        resultMap.put("totalPayCount",totalPayCount);
        resultMap.put("totalGoodsCount",totalGoodsCount);
        resultMap.put("totalUserCount",totalUserCount);
        return ResultGenerator.genSuccessResult(resultMap);
    }

    @Override
    public void addTotalData() {
        Inno72PaiNowData obj = new Inno72PaiNowData();
        inno72PaiNowDataMapper.delete(obj);
        List<Inno72PaiNowData> dataList = inno72PaiNowDataMapper.selectList();
        if(dataList != null && dataList.size()>0){
            for(Inno72PaiNowData nowData:dataList){
                nowData.setId(StringUtil.getUUID());
                inno72PaiNowDataMapper.insert(nowData);
            }
        }

    }

    @Override
    public Result<List<Inno72ActivityPaiDataVo>> findTotalDataList() {
        List<Inno72ActivityPaiDataVo> list = inno72PaiNowDataMapper.selectPaiNowList();
        return ResultGenerator.genSuccessResult(list);
    }

}
