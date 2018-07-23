package com.inno72.check.controller;

import com.inno72.check.model.Inno72CheckFault;
import com.inno72.check.model.Inno72CheckFaultType;
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
import java.util.Map;

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
    public Result add(@RequestBody Inno72CheckFault checkFault){
        Result result = checkFaultService.addCheckFault(checkFault);
        return result;
    }

    /**
     * 解决故障
     * @param checkFault
     * @return
     */
    @RequestMapping(value="/finish",method = {RequestMethod.POST,RequestMethod.GET})
    public Result finish(@RequestBody Inno72CheckFault checkFault){
        Result result = checkFaultService.finish(checkFault);
        return result;
    }

    /**
     * 故障集合
     * @return
     */
    @RequestMapping(value="/list",method = {RequestMethod.POST,RequestMethod.GET})
    public ModelAndView list(@RequestBody Inno72CheckFault inno72CheckFault){
        List<Inno72CheckFault> list = checkFaultService.findForPage(inno72CheckFault.getStatus());
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
     * @return
     */
    @RequestMapping(value="/edit",method = {RequestMethod.POST,RequestMethod.GET})
    public Result<String> edit(@RequestBody Inno72CheckFault inno72CheckFault){
        checkFaultService.editRemark(inno72CheckFault.getId(),inno72CheckFault.getFinishRemark());
        return ResultGenerator.genSuccessResult();
    }

    /**
     * 查询详情
     * @return
     */
    @RequestMapping(value="/detail",method = {RequestMethod.POST,RequestMethod.GET})
    public Result<Inno72CheckFault> detail(@RequestBody Inno72CheckFault inno72CheckFault){
        Result<Inno72CheckFault> result = checkFaultService.getDetail(inno72CheckFault.getId());
        return result;
    }

    /**
     * 查询故障类型
     * @return
     */
    @RequestMapping(value = "/typeList", method = {RequestMethod.POST,RequestMethod.GET})
    public Result<List<Inno72CheckFaultType>> typeList(@RequestBody Inno72CheckFault inno72CheckFault){
        Result<List<Inno72CheckFaultType>> result = checkFaultService.getTypeList(inno72CheckFault.getType());
        return result;
    }
}
