package com.inno72.project.controller;

import com.inno72.common.Result;
import com.inno72.project.model.Inno72ActivityDataCount;
import com.inno72.project.model.Inno72PaiDataCount;
import com.inno72.project.service.ActivityDataCountService;
import com.inno72.project.service.PaiDataCountService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/pai/data/count")
public class PaiDataCountController {

    @Resource
    private PaiDataCountService paiDataCountService;

    /**
     * 添加数据
     */
    @RequestMapping(value = "add")
    public void add(){
        paiDataCountService.addData();
    }


    @RequestMapping(value="list")
    public Result<Map<String,Object>> list(@RequestBody Inno72PaiDataCount inno72PaiDataCount){
        return paiDataCountService.findList(inno72PaiDataCount);
    }

    @RequestMapping(value="addNowData")
    public void addNowData(){
        paiDataCountService.addNowData();
    }

}
