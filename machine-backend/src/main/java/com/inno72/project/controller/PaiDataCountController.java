package com.inno72.project.controller;

import com.inno72.common.Result;
import com.inno72.project.model.Inno72ActivityDataCount;
import com.inno72.project.model.Inno72PaiDataCount;
import com.inno72.project.service.ActivityDataCountService;
import com.inno72.project.service.PaiDataCountService;
import com.inno72.project.vo.Inno72ActivityPaiDataVo;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/pai/data/count")
public class PaiDataCountController {

    @Resource
    private PaiDataCountService paiDataCountService;

    /**
     * 添加派样活动每天的数据
     */
    @RequestMapping(value = "add")
    public void add(){
        paiDataCountService.addData();
    }

    /**
     * 查询派样活动每天的数据
     * @param inno72PaiDataCount
     * @return
     */
    @RequestMapping(value="list")
    public Result<Map<String,Object>> list(@RequestBody Inno72PaiDataCount inno72PaiDataCount){
        return paiDataCountService.findList(inno72PaiDataCount);
    }

    /**
     * 添加派样活动总数据
     */
    @RequestMapping(value="addTotalData")
    public void addNowData(){
        paiDataCountService.addTotalData();
    }

    /**
     * 查询派样活动总数据
     * @return
     */
    @RequestMapping(value = "totalList")
    public Result<List<Inno72ActivityPaiDataVo>> totalList(){
        return paiDataCountService.findTotalDataList();
    }

}
