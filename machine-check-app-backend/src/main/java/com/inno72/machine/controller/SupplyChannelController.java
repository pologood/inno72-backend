package com.inno72.machine.controller;

import com.alibaba.fastjson.JSON;
import com.inno72.common.AesUtils;
import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;
import com.inno72.common.ResultPages;
import com.inno72.machine.model.Inno72Goods;
import com.inno72.machine.model.Inno72Machine;
import com.inno72.machine.model.Inno72SupplyChannel;
import com.inno72.machine.service.SupplyChannelService;
import com.inno72.machine.vo.CommonVo;
import com.inno72.machine.vo.SupplyRequestVo;
import com.inno72.machine.vo.WorkOrderVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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


	private Logger logger = LoggerFactory.getLogger(this.getClass());
	/**
	 * 货道列表
	 */
	@RequestMapping(value = "/list", method = {RequestMethod.POST })
	public Result<List<Inno72SupplyChannel>> list(@RequestBody SupplyRequestVo vo) {
		logger.info("货道列表接口参数：{}", JSON.toJSON(vo));
		return supplyChannelService.getList(vo.getMachineId());
	}

	/**
	 * 合并货道
	 * 
	 */
	@RequestMapping(value = "/merge", method = { RequestMethod.POST})
	public Result<String> merge(@RequestBody Inno72SupplyChannel supplyChannel) {
		logger.info("合并货道接口参数：{}", JSON.toJSON(supplyChannel));
        return supplyChannelService.merge(supplyChannel);
	}

	/**
	 * 拆分货道
	 */
	@RequestMapping(value = "/split", method = { RequestMethod.POST})
	public Result<String> split(@RequestBody Inno72SupplyChannel supplyChannel) {
		logger.info("拆分货道接口参数：{}", JSON.toJSON(supplyChannel));
        return supplyChannelService.split(supplyChannel);
	}

	/**
	 * 货道清零
	 */
	@RequestMapping(value = "/clear", method = { RequestMethod.POST})
	public Result<String> clear(@RequestBody Inno72SupplyChannel supplyChannel) {
		logger.info("货道清零接口参数：{}", JSON.toJSON(supplyChannel));
        return supplyChannelService.clear(supplyChannel);
	}

	/**
	 * 查询货道操作历史
	 */
	@RequestMapping(value = "history", method = { RequestMethod.POST})
	public Result<Map<String, Object>> history(@RequestBody Inno72SupplyChannel supplyChannel) {
		logger.info("货道操作历史接口参数：{}", JSON.toJSON(supplyChannel));
        return supplyChannelService.history(supplyChannel);
	}

	/**
	 * 机器维度缺货
	 */
	@RequestMapping(value="machineLack",method = {RequestMethod.POST })
	public Result<List<Inno72Machine>> getMachineLack(@RequestBody CommonVo commonVo){
		logger.info("机器维度缺货接口");
		return supplyChannelService.getMachineLackGoods(commonVo);
	}

	/**
	 * 商品维度缺货
	 */
	@RequestMapping(value="goodsLack",method = {RequestMethod.POST })
	public Result<List<Inno72Goods>> getGoodsLack(@RequestBody CommonVo commonVo){
		logger.info("商品维度缺货接口");
		return supplyChannelService.getGoodsLack(commonVo);
	}

	/**
	 * 查询单个商品缺货的机器
	 */
	@RequestMapping(value="machineByLackGoods",method = {RequestMethod.POST })
	public Result<List<Inno72Machine>> getMachineByLackGoods(@RequestBody SupplyRequestVo vo){
		logger.info("查询单个商品缺货的机器接口参数：{}",JSON.toJSON(vo));
		return supplyChannelService.getMachineByLackGoods(vo.getGoodsId());
	}

	/**
	 * 根据机器查询可用商品
	 */
	@RequestMapping(value="getGoodsByMachineId",method = {RequestMethod.POST })
	public Result<List<Inno72Goods>> getGoodsByMachineId(@RequestBody SupplyRequestVo vo){
		logger.info("根据机器查询可用商品接口参数：{}",JSON.toJSON(vo));
		return supplyChannelService.getGoodsByMachineId(vo.getMachineId());
	}

	/**
	 * 一键清空
	 */
	@RequestMapping(value="clearAll", method = {RequestMethod.POST})
	public Result<String> clearAll(@RequestBody SupplyRequestVo vo){
		logger.info("一键清空接口参数：{}",JSON.toJSON(vo));
        return supplyChannelService.clearAll(vo.getMachineId());
	}

	/**
	 * 一键补货
	 */
	@RequestMapping(value="supplyAll", method = {RequestMethod.POST})
	public Result<String> supplyAll(@RequestBody SupplyRequestVo vo){
		logger.info("一键补货接口参数：{}",JSON.toJSON(vo));
        return supplyChannelService.supplyAll(vo.getMachineId());
	}

	/**
	 * 提交补货
	 */
	@RequestMapping(value="submit",method = { RequestMethod.POST})
	public Result<String> submit(@RequestBody Map<String,Object> map){
		List<Map<String,Object>> mapList = (List<Map<String,Object>>) map.get("list");
		logger.info("提交补货接口参数：{}",JSON.toJSON(mapList));
        return supplyChannelService.submit(mapList);
	}

	/**
	 * 补货前校验
	 * @param map
	 * @return
	 */
	@RequestMapping(value="supplyCheck",method = { RequestMethod.POST})
	public Result<Boolean> supplyCheck(@RequestBody Map<String,Object> map){
		List<Map<String,Object>> mapList = (List<Map<String,Object>>) map.get("list");
		logger.info("补货校验接口参数：{}",JSON.toJSON(mapList));
		return supplyChannelService.supplyCheck(mapList);
	}

	/**
	 * 工单列表
	 */
	@RequestMapping(value="workOrderList",method = {RequestMethod.POST})
	public ModelAndView workOrderListByPage(@RequestBody SupplyRequestVo vo){
		logger.info("查询工单列表接口参数：{}",JSON.toJSON(vo));
		List<WorkOrderVo> list = supplyChannelService.findByPage(vo.getKeyword(),vo.getFindTime());
		logger.info("查询工单列表返回结果：{}",JSON.toJSON(list));
		return ResultPages.page(ResultGenerator.genSuccessResult(list));
	}

	/**
	 * 工单详情
	 */
	@RequestMapping(value="workOrderDetail",method = {RequestMethod.POST})
	public Result<List<WorkOrderVo>> workOrderDetail(@RequestBody SupplyRequestVo vo){
		logger.info("查询工单详情接口参数：{}",JSON.toJSON(vo));
        return supplyChannelService.workOrderDetail(vo);
	}


	/**
	 * 按月查询补货记录
	 * @param vo
	 * @return
	 */
	@RequestMapping(value="findByMonth",method = {RequestMethod.POST})
	public Result<List<WorkOrderVo>> findByMonth(@RequestBody SupplyRequestVo vo){
		logger.info("按日历查询补货记录接口参数：{}",JSON.toJSON(vo));
		return supplyChannelService.findOrderByMonth(vo);
	}


	/**
	 * 查询缺货货道并发送push
	 */
	@RequestMapping(value = "findAndPushByTaskParam",method = {RequestMethod.POST})
	public void findAndPushByTaskParam(){
		logger.info("查询缺货货道并发送push接口");
		supplyChannelService.findAndPushByTaskParam();
	}

	/**
	 * 查询异常货道
	 * @param vo
	 * @return
	 */
	@RequestMapping(value = "exceptionList" ,method = {RequestMethod.POST})
	public Result<List<Inno72SupplyChannel>> exceptionList(@RequestBody SupplyRequestVo vo){
		logger.info("查询异常货道接收参数：{}",JSON.toJSONString(vo));
		Result<List<Inno72SupplyChannel>> result = supplyChannelService.exceptionList(vo);
		logger.info("查询异常货道返回结果：{}",JSON.toJSONString(result));
		return result;
	}

	/**
	 * 启用货道
	 * @param vo
	 * @return
	 */
	@RequestMapping(value = "openSupplyChannel" ,method = {RequestMethod.POST})
	public Result<String> openSupplyChannel(@RequestBody SupplyRequestVo vo){
		logger.info("启用货道接收参数：{}",JSON.toJSONString(vo));
		Result<String> result = supplyChannelService.openSupplyChannel(vo);
		logger.info("启用货道返回结果：{}",JSON.toJSONString(result));
		return result;
	}


	/**
	 * 同步货道
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "updateSupplyChannel")
	public Result<String> updateSupplyChannel(@RequestBody Map<String,Object> map){
		logger.info("APP同步货道参数："+JSON.toJSONString(map));
		Result<String> result = supplyChannelService.updateSupplyChannel(map);
		logger.info("APP同步货道返回："+JSON.toJSONString(result));
		return result;
	}


	/**
	 * 同步货道后端
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "updateSupplyBackend")
	public Result<String> updateSupplyBackend(@RequestBody Map<String,Object> map){
		logger.info("后端同步货道参数："+JSON.toJSONString(map));
		Result<String> result = supplyChannelService.updateSupplyChannel(map);
		logger.info("后端同步货道返回："+JSON.toJSONString(result));
		return result;
	}


}
