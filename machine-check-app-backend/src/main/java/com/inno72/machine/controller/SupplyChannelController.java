package com.inno72.machine.controller;

import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;
import com.inno72.common.ResultPages;
import com.inno72.machine.model.Inno72Goods;
import com.inno72.machine.model.Inno72Machine;
import com.inno72.machine.model.Inno72SupplyChannel;
import com.inno72.machine.service.SupplyChannelService;
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
	 * @param machineId
	 * @return
	 */
	@RequestMapping(value = "/list", method = { RequestMethod.POST, RequestMethod.GET })
	public ModelAndView list(String machineId) {
		List<Inno72SupplyChannel> list = supplyChannelService.getList(machineId);
		return ResultPages.page(ResultGenerator.genSuccessResult(list));
	}

	/**
	 * 合并货道
	 * 
	 * @param supplyChannel
	 * @return
	 */
	@RequestMapping(value = "/merge", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> merge(Inno72SupplyChannel supplyChannel) {
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
	public Result<String> split(Inno72SupplyChannel supplyChannel) {
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
	public Result<String> clear(Inno72SupplyChannel supplyChannel) {
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
	public Result<Map<String, Object>> history(Inno72SupplyChannel supplyChannel) {
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
	 * @param goodsId
	 * @return
	 */
	@RequestMapping(value="machineByLackGoods",method = { RequestMethod.POST, RequestMethod.GET })
	public Result<List<Inno72Machine>> getMachineByLackGoods(String goodsId){
		List<Inno72Machine> machineList = supplyChannelService.getMachineByLackGoods(goodsId);
		return ResultGenerator.genSuccessResult(machineList);
	}

	/**
	 * 根据机器查询可用商品
	 * @param machineId
	 * @return
	 */
	@RequestMapping(value="getGoodsByMachineId",method = { RequestMethod.POST, RequestMethod.GET })
	public Result<List<Inno72Goods>> getGoodsByMachineId(String machineId){
		List<Inno72Goods> goodsList = supplyChannelService.getGoodsByMachineId(machineId);
		return ResultGenerator.genSuccessResult(goodsList);
	}

	/**
	 * 一键清空
	 * @param machineId
	 * @return
	 */
	@RequestMapping(value="clearAll")
	public Result<String> clearAll(String machineId){
		Result<String> result = supplyChannelService.clearAll(machineId);
		return result;
	}

	/**
	 * 一键补货
	 * @param machineId
	 * @return
	 */
	@RequestMapping(value="supplyAll")
	public Result<String> supplyAll(String machineId){
		Result<String> result = supplyChannelService.supplyAll(machineId);
		return result;
	}

	/**
	 * 提交补货
	 * @param supplyChannelList
	 * @return
	 */
	@RequestMapping(value="submit",method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> submit(List<Inno72SupplyChannel> supplyChannelList){
		Result<String> result = supplyChannelService.submit(supplyChannelList);
		return result;
	}

	/**
	 * 工单列表
	 * @param keyword
	 * @param findTime
	 * @return
	 */
	@RequestMapping(value="workOrderList",method = {RequestMethod.POST, RequestMethod.GET })
	public ModelAndView workOrderList(String keyword,String findTime){
		List<WorkOrderVo> list = supplyChannelService.workOrderList(keyword,findTime);
		return ResultPages.page(ResultGenerator.genSuccessResult(list));
	}

	/**
	 * 工单详情
	 * @param machineId
	 * @param batchNo
	 * @return
	 */
	@RequestMapping(value="workOrderDetail",method = {RequestMethod.POST,RequestMethod.GET})
	public Result<WorkOrderVo> workOrderDetail(String machineId,String batchNo){
		Result<WorkOrderVo> result = supplyChannelService.workOrderDetail(machineId,batchNo);
		return result;
	}


}
