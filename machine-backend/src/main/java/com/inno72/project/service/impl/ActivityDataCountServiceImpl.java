package com.inno72.project.service.impl;

import com.inno72.common.AbstractService;
import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;
import com.inno72.common.StringUtil;
import com.inno72.project.mapper.Inno72ActivityDataCountMapper;
import com.inno72.project.model.Inno72ActivityDataCount;
import com.inno72.project.service.ActivityDataCountService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
@Transactional
public class ActivityDataCountServiceImpl extends AbstractService<Inno72ActivityDataCount> implements ActivityDataCountService {

    @Resource
    private Inno72ActivityDataCountMapper inno72ActivityDataCountMapper;

    @Override
    public void addData() {
        List<Inno72ActivityDataCount> list = inno72ActivityDataCountMapper.selectList();
        if(list != null && list.size()>0){
            for (Inno72ActivityDataCount dataCount:list) {
                dataCount.setId(StringUtil.getUUID());
                inno72ActivityDataCountMapper.insertSelective(dataCount);
            }
        }
    }

    @Override
    public Result<Map<String,Object>> findList(Inno72ActivityDataCount inno72ActivityDataCount) {
        Map<String,Object> map = new HashMap<>();
        String activityId = inno72ActivityDataCount.getActivityId();
        if(StringUtil.isNotEmpty(activityId)){
            map.put("activityId",activityId);
        }
        String activityPlanId = inno72ActivityDataCount.getActivityPlanId();
        if(StringUtil.isNotEmpty(activityPlanId)){
            map.put("activityPlanId",activityPlanId);
        }
        List<Inno72ActivityDataCount> list = inno72ActivityDataCountMapper.selectByParam(map);
        int totalOrderCount = 0;
        int totalPayCount = 0;
        int totalGoodsCount = 0;
        int totalCouponCount = 0;
        int totalUserCount = 0;
        if(list != null && list.size()>0){
            for(Inno72ActivityDataCount count:list){
                totalOrderCount+=count.getOrderCount();
                totalPayCount+=count.getPayCount();
                totalGoodsCount+=count.getGoodsCount();
                totalCouponCount+=count.getCouponCount();
                totalUserCount+=count.getUserCount();
            }
        }
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("list",list);
        resultMap.put("totalOrderCount",totalOrderCount);
        resultMap.put("totalPayCount",totalPayCount);
        resultMap.put("totalGoodsCount",totalGoodsCount);
        resultMap.put("totalCouponCount",totalCouponCount);
        resultMap.put("totalUserCount",totalUserCount);
        return ResultGenerator.genSuccessResult(resultMap);
    }

    @Override
    public Result<String> setHistory() {
        List<Inno72ActivityDataCount> historyList = inno72ActivityDataCountMapper.selectHistoryList();
        if(historyList != null && historyList.size()>0){
            for(Inno72ActivityDataCount count:historyList){
                count.setId(StringUtil.getUUID());
                inno72ActivityDataCountMapper.insertSelective(count);
            }
        }
        return ResultGenerator.genSuccessResult();
    }
}
