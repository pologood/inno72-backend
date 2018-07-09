package com.inno72.machine.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;
import com.inno72.common.ResultPages;
import com.inno72.machine.model.Inno72SupplyChannel;
import com.inno72.machine.service.SupplyChannelService;

import tk.mybatis.mapper.entity.Condition;

/**
 * Created by CodeGenerator on 2018/07/03.
 */
@RestController
@CrossOrigin
@RequestMapping("/machine/channel")
public class SupplyChannelController {
	@Resource
	private SupplyChannelService supplyChannelService;

	@RequestMapping(value = "/add", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> add(Inno72SupplyChannel supplyChannel) {
		supplyChannelService.save(supplyChannel);
		return ResultGenerator.genSuccessResult();
	}

	@RequestMapping(value = "/delete", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> delete(@RequestParam String id) {
		supplyChannelService.deleteById(id);
		return ResultGenerator.genSuccessResult();
	}

	@RequestMapping(value = "/update", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> update(Inno72SupplyChannel supplyChannel) {
		supplyChannelService.update(supplyChannel);
		return ResultGenerator.genSuccessResult();
	}

	@RequestMapping(value = "/detail", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<Inno72SupplyChannel> detail(@RequestParam String id) {
		Inno72SupplyChannel supplyChannel = supplyChannelService.findById(id);
		return ResultGenerator.genSuccessResult(supplyChannel);
	}

    /**
     * 货道列表
     * @param supplyChannel
     * @return
     */
	@RequestMapping(value = "/list", method = { RequestMethod.POST, RequestMethod.GET })
	public ModelAndView list(Inno72SupplyChannel supplyChannel) {
		List<Inno72SupplyChannel> list = supplyChannelService.getListForPage(supplyChannel);
		return ResultPages.page(ResultGenerator.genSuccessResult(list));
	}

	/**
	 * 初始化数据
	 * @param machineId
	 * @return
	 */
	@RequestMapping(value = "/init" ,method={RequestMethod.POST,RequestMethod.GET})
	public Result<String> init(@RequestParam String machineId){
		Result result = supplyChannelService.init(machineId);
		return result;
	}

	/**
	 * 合并货道
	 * @param supplyChannel
	 * @return
	 */
	@RequestMapping(value="/merge",method = {RequestMethod.POST,RequestMethod.GET})
	public Result<String> merge(Inno72SupplyChannel supplyChannel){
		Result result = supplyChannelService.merge(supplyChannel);
		return result;
	}

	/**
	 * 拆分货道
	 * @param supplyChannel
	 * @return
	 */
	@RequestMapping(value="/split",method = {RequestMethod.POST,RequestMethod.GET})
	public Result<String> split(Inno72SupplyChannel supplyChannel){
		Result result = supplyChannelService.split(supplyChannel);
		return result;
	}

	/**
	 * 货道清零
	 * @param supplyChannel
	 * @return
	 */
	@RequestMapping(value="/clear",method = {RequestMethod.POST,RequestMethod.GET})
	public Result<String> clear(Inno72SupplyChannel supplyChannel){
		Result<String> result = supplyChannelService.clear(supplyChannel);
		return result;
	}

	/**
	 * 一键下架
	 * @param supplyChannel
	 * @return
	 */
	@RequestMapping(value="downAll",method = {RequestMethod.POST,RequestMethod.GET})
	public Result<String> downAll(Inno72SupplyChannel supplyChannel){
		Result<String> result = supplyChannelService.downAll(supplyChannel);
		return result;
	}

    /**
     * 查询货道操作历史
     * @param supplyChannel
     * @return
     */
	@RequestMapping(value="history",method = {RequestMethod.POST,RequestMethod.GET})
	public Result<Map<String,Object>> history(Inno72SupplyChannel supplyChannel){
	    Result<Map<String,Object>> result = supplyChannelService.history(supplyChannel);
	    return result;
    }

}
