package com.inno72.check.controller;

import com.alibaba.fastjson.JSON;
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

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RequestMapping("/check/fault/h5")
@CrossOrigin
@RestController
public class CheckFaultH5Controller {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 故障service
     */
    @Resource
    private CheckFaultService checkFaultService;

    /**
     * 添加报障
     */
    @RequestMapping(value="/add" , method = {RequestMethod.POST})
    public Result<String> add(@RequestBody Inno72CheckFault checkFault){
        logger.info("上报故障H5接口参数：{}", JSON.toJSON(checkFault));
        Result result = checkFaultService.addCheckFault(checkFault);
        logger.info("上报故障H5接口结果：{}",JSON.toJSON(result));
        return result;
    }

    /**
     * 解决故障
     */
    @RequestMapping(value="/finish",method = {RequestMethod.POST})
    public Result<String> finish(@RequestBody Inno72CheckFault checkFault){
        logger.info("解决故障H5接口参数：{}", JSON.toJSON(checkFault));
        Result<String> result = checkFaultService.finish(checkFault);
        logger.info("解决故障H5接口结果：{}", JSON.toJSON(result));
        return result;
    }

    /**
     * 故障集合
     */
    @RequestMapping(value="/list",method = {RequestMethod.GET})
    public ModelAndView list(Integer status,Integer workType){
        Inno72CheckFault checkFault = new Inno72CheckFault();
        checkFault.setStatus(status);
        checkFault.setWorkType(workType);
        logger.info("查询故障分页列表H5接口参数：{}", JSON.toJSON(checkFault));
        List<Inno72CheckFault> list = checkFaultService.findForPage(checkFault);
        logger.info("查询故障分页列表H5接口结果：{}", JSON.toJSON(list));
        return ResultPages.page(ResultGenerator.genSuccessResult(list));
    }

    /**
     * 上传图片
     */
    @RequestMapping(value="/upload",method = {RequestMethod.POST})
    public Result<String> upload(MultipartFile file){
        logger.info("添加故障上传头像H5接口开始上传");
        Result<String> result = checkFaultService.upload(file);
        logger.info("添加故障上传头像H5接口返回图片路径：{}",JSON.toJSON(result));
        return result;
    }

    /**
     * 编辑故障
     */
    @RequestMapping(value="/edit",method = {RequestMethod.POST})
    public Result<String> edit(@RequestBody Inno72CheckFault inno72CheckFault){
        logger.info("编辑回复故障备注H5接口参数：{}",JSON.toJSON(inno72CheckFault));
        Result<String> result = checkFaultService.editRemark(inno72CheckFault);
        logger.info("编辑回复故障备注H5接口结果：{}",JSON.toJSON(result));
        return result;
    }

    /**
     * 查询详情
     */
    @RequestMapping(value="/detail",method = {RequestMethod.GET})
    public Result<Inno72CheckFault> detail(Inno72CheckFault inno72CheckFault){
        logger.info("查询故障详情H5接口参数：{}",JSON.toJSON(inno72CheckFault));
        Result<Inno72CheckFault> result = checkFaultService.getDetail(inno72CheckFault.getId());
        logger.info("查询故障详情H5接口结果：{}",JSON.toJSON(result));
        return result;
    }

    /**
     * 查询故障类型
     */
    @RequestMapping(value = "/typeList", method = {RequestMethod.GET})
    public Result<List<Inno72CheckFaultType>> typeList(Inno72CheckFaultType inno72CheckFaultType){
        logger.info("查询故障类型H5接口参数：{}",JSON.toJSON(inno72CheckFaultType));
        Result<List<Inno72CheckFaultType>> result = checkFaultService.getTypeList(inno72CheckFaultType);
        logger.info("查询故障类型H5接口结果：{}",JSON.toJSON(result));
        return result;
    }

    /**
     * 接单
     * @param inno72CheckFault
     * @return
     */
    @RequestMapping(value = "receive", method = {RequestMethod.POST})
    public Result<String> receive(@RequestBody Inno72CheckFault inno72CheckFault){
        logger.info("工单接单接口参数：{}",JSON.toJSON(inno72CheckFault));
        Result<String> result = checkFaultService.receive(inno72CheckFault);
        return result;
    }
}
