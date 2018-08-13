package com.inno72.project.controller;

import com.inno72.common.Result;
import com.inno72.project.model.Inno72ActivityDataCount;
import com.inno72.project.service.ActivityDataCountService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/activity/data/count")
public class ActivityDataCountController {

    @Resource
    private ActivityDataCountService activityDataCountService;

    /**
     * 添加数据
     */
    @RequestMapping(value = "add")
    public void add(){
        activityDataCountService.addData();
    }


    @RequestMapping(value="list")
    public Result<List<Inno72ActivityDataCount>> list(@RequestBody Inno72ActivityDataCount inno72ActivityDataCount){
        return activityDataCountService.findList(inno72ActivityDataCount);
    }
}
