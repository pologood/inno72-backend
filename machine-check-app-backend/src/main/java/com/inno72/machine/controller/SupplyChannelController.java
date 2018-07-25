package com.inno72.machine.controller;

import com.alibaba.fastjson.JSONArray;
import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;
import com.inno72.common.ResultPages;
import com.inno72.machine.model.Inno72Goods;
import com.inno72.machine.model.Inno72Machine;
import com.inno72.machine.model.Inno72SupplyChannel;
import com.inno72.machine.service.SupplyChannelService;
import com.inno72.machine.vo.SupplyChannelVo;
import com.inno72.machine.vo.SupplyRequestVo;
import com.inno72.machine.vo.WorkOrderVo;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by CodeGenerator on 2018/07/03.
 */
@RestController
@CrossOrigin
@RequestMapping("/machine/channel")
public class SupplyChannelController {
	@Resource
	private SupplyChannelService supplyChannelService;


	/**
	 * 货道列表
	 * 
	 * @return
	 */
	@RequestMapping(value = "/list", method = { RequestMethod.POST, RequestMethod.GET })
	public ModelAndView list(@RequestBody SupplyRequestVo vo) {
		List<Inno72SupplyChannel> list = supplyChannelService.getList(vo.getMachineId());
		return ResultPages.page(ResultGenerator.genSuccessResult(list));
	}

	/**
	 * 合并货道
	 * 
	 * @return
	 */
	@RequestMapping(value = "/merge", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> merge(@RequestBody Inno72SupplyChannel supplyChannel) {
		Result<String> result = supplyChannelService.merge(supplyChannel);
		return result;
	}

	/**
	 * 拆分货道
	 * 
	 * @param supplyChannel
	 * @return
	 */
	@RequestMapping(value = "/split", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> split(@RequestBody Inno72SupplyChannel supplyChannel) {
		Result<String> result = supplyChannelService.split(supplyChannel);
		return result;
	}

	/**
	 * 货道清零
	 * 
	 * @param supplyChannel
	 * @return
	 */
	@RequestMapping(value = "/clear", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> clear(@RequestBody Inno72SupplyChannel supplyChannel) {
		Result<String> result = supplyChannelService.clear(supplyChannel);
		return result;
	}

	/**
	 * 查询货道操作历史
	 * 
	 * @param supplyChannel
	 * @return
	 */
	@RequestMapping(value = "history", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<Map<String, Object>> history(@RequestBody Inno72SupplyChannel supplyChannel) {
		Result<Map<String, Object>> result = supplyChannelService.history(supplyChannel);
		return result;
	}

	/**
	 * 机器维度缺货
	 * @return
	 */
	@RequestMapping(value="machineLack",method = { RequestMethod.POST, RequestMethod.GET })
	public Result<List<Inno72Machine>> getMachineLack(){
		List<Inno72Machine> machineList = supplyChannelService.getMachineLackGoods();
		return ResultGenerator.genSuccessResult(machineList);
	}

	/**
	 * 商品维度缺货
	 * @return
	 */
	@RequestMapping(value="goodsLack",method = { RequestMethod.POST, RequestMethod.GET })
	public Result<List<Inno72Goods>> getGoodsLack(){
		List<Inno72Goods> goodsList = supplyChannelService.getGoodsLack();
		return ResultGenerator.genSuccessResult(goodsList);
	}

	/**
	 * 查询单个商品缺货的机器
	 * @return
	 */
	@RequestMapping(value="machineByLackGoods",method = { RequestMethod.POST, RequestMethod.GET })
	public Result<List<Inno72Machine>> getMachineByLackGoods(@RequestBody SupplyRequestVo vo){
		List<Inno72Machine> machineList = supplyChannelService.getMachineByLackGoods(vo.getGoodsId());
		return ResultGenerator.genSuccessResult(machineList);
	}

	/**
	 * 根据机器查询可用商品
	 * @return
	 */
	@RequestMapping(value="getGoodsByMachineId",method = { RequestMethod.POST, RequestMethod.GET })
	public Result<List<Inno72Goods>> getGoodsByMachineId(@RequestBody SupplyRequestVo vo){
		List<Inno72Goods> goodsList = supplyChannelService.getGoodsByMachineId(vo.getMachineId());
		return ResultGenerator.genSuccessResult(goodsList);
	}

	/**
	 * 一键清空
	 * @return
	 */
	@RequestMapping(value="clearAll", method = {RequestMethod.POST,RequestMethod.GET})
	public Result<String> clearAll(@RequestBody SupplyRequestVo vo){
		Result<String> result = supplyChannelService.clearAll(vo.getMachineId());
		return result;
	}

	/**
	 * 一键补货
	 * @return
	 */
	@RequestMapping(value="supplyAll", method = {RequestMethod.POST,RequestMethod.GET})
	public Result<String> supplyAll(@RequestBody SupplyRequestVo vo){
		Result<String> result = supplyChannelService.supplyAll(vo.getMachineId());
		return result;
	}

	/**
	 * 提交补货
	 * @param map
	 * @return
	 */
	@RequestMapping(value="submit",method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> submit(@RequestBody Map<String,Object> map){
		List<Map<String,Object>> mapList = (List<Map<String,Object>>) map.get("list");
		Result<String> result = supplyChannelService.submit(mapList);
		return result;
	}

	/**
	 * 工单列表
	 * @return
	 */
	@RequestMapping(value="workOrderList",method = {RequestMethod.POST, RequestMethod.GET })
	public ModelAndView workOrderList(@RequestBody SupplyRequestVo vo){
		List<WorkOrderVo> list = supplyChannelService.workOrderList(vo.getKeyword(),vo.getFindTime());
		return ResultPages.page(ResultGenerator.genSuccessResult(list));
	}

	/**
	 * 工单详情
	 * @return
	 */
	@RequestMapping(value="workOrderDetail",method = {RequestMethod.POST,RequestMethod.GET})
	public Result<WorkOrderVo> workOrderDetail(@RequestBody SupplyRequestVo vo){
		Result<WorkOrderVo> result = supplyChannelService.workOrderDetail(vo.getMachineId(),vo.getBatchNo());
		return result;
	}


	/**
	 * 查询缺货货道并发送push
	 * @param vo
	 */
	@RequestMapping(value = "findAndPushByTaskParam",method = {RequestMethod.POST,RequestMethod.GET})
	public void findAndPushByTaskParam(@RequestBody SupplyRequestVo vo){
		supplyChannelService.findAndPushByTaskParam(vo.getLackGoodsType());
	}


}
