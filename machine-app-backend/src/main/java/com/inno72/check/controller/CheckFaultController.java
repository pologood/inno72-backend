package com.inno72.check.controller;

import com.inno72.check.model.Inno72CheckFault;
import com.inno72.check.service.CheckFaultService;
import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;
import com.inno72.common.ResultPages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.util.List;

@RequestMapping("/check/fault")
@CrossOrigin
@RestController
public class CheckFaultController {
    public Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private CheckFaultService checkFaultService;

    /**
     * 添加报障
     * @param checkFault
     * @return
     */
    @RequestMapping(value="/add" , method = {RequestMethod.POST,RequestMethod.GET})
    public Result add(Inno72CheckFault checkFault){
        Result result = checkFaultService.addCheckFault(checkFault);
        return result;
    }

    /**
     * 解决故障
     * @param checkFault
     * @return
     */
    @RequestMapping(value="/finish",method = {RequestMethod.POST,RequestMethod.GET})
    public Result finish(Inno72CheckFault checkFault){
        Result result = checkFaultService.finish(checkFault);
        return result;
    }

    @RequestMapping(value="/list",method = {RequestMethod.POST,RequestMethod.GET})
    public ModelAndView list(){
        Condition condition = new Condition(Inno72CheckFault.class);
        List<Inno72CheckFault> list = checkFaultService.findForPage(condition);
        return ResultPages.page(ResultGenerator.genSuccessResult(list));
    }

    /**
     * 上传图片
     * @param file
     * @return
     */
    @RequestMapping(value="/upload",method = {RequestMethod.POST})
    public Result<String> upload(MultipartFile file){
        Result<String> result = checkFaultService.upload(file);
        return result;
    }

    /**
     * 编辑故障
     * @param checkFault
     * @return
     */
    @RequestMapping(value="/edit",method = {RequestMethod.POST,RequestMethod.GET})
    public Result<String> edit(Inno72CheckFault checkFault){
        checkFaultService.update(checkFault);
        return ResultGenerator.genSuccessResult();
    }
}
