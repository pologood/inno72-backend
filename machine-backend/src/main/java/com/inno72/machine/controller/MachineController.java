package com.inno72.machine.controller;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;
import com.inno72.common.ResultPages;
import com.inno72.common.Results;
import com.inno72.common.StringUtil;
import com.inno72.machine.model.Inno72AppLog;
import com.inno72.machine.model.Inno72Machine;
import com.inno72.machine.service.MachineService;
import com.inno72.machine.vo.ChannelListVo;
import com.inno72.machine.vo.MachineAppStatus;
import com.inno72.machine.vo.MachineExceptionVo;
import com.inno72.machine.vo.MachineListVo;
import com.inno72.machine.vo.MachineListVo1;
import com.inno72.machine.vo.MachineNetInfo;
import com.inno72.machine.vo.MachinePortalVo;
import com.inno72.machine.vo.MachineStatusVo;
import com.inno72.machine.vo.MachineStockOutInfo;
import com.inno72.machine.vo.PointLog;
import com.inno72.machine.vo.UpdateMachineChannelVo;
import com.inno72.machine.vo.UpdateMachineVo;

/**
 * Created by CodeGenerator on 2018/06/29.
 */
@RestController
@RequestMapping("/machine/machine")
@CrossOrigin
public class MachineController {
	@Resource
	private MachineService machineService;

	/**
	 * 更新机器网络状态
	 *
	 * @param list
	 * @return
	 */
	@RequestMapping(value = "/updateMachineListNetStatus", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<List<MachineNetInfo>> updateMachineListNetStatus(@RequestBody List<MachineNetInfo> list) {
		return machineService.updateMachineListNetStatus(list);

	}

	/**
	 * 机器日志
	 *
	 * @param machineCode
	 *            machineCode
	 * @param startTime
	 *            startTime
	 * @param endTime
	 *            endTime
	 * @return 日志列表
	 */
	@RequestMapping(value = "/machinePointLog", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<List<PointLog>> machinePointLog(String machineCode, String startTime, String endTime) {
		return machineService.machinePointLog(machineCode, startTime, endTime);
	}

	/**
	 * 导出机器日志
	 *
	 * @param machineCode
	 *            machineCode
	 * @param startTime
	 *            startTime
	 * @param endTime
	 *            endTime
	 * @return 日志列表
	 */
	@RequestMapping(value = "/exportMachinePointLog", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> exportMachinePointLog(String machineCode, String startTime, String endTime,
			HttpServletResponse response) throws IOException {
		if (StringUtil.isEmpty(startTime) || StringUtil.isEmpty(endTime)) {
			return Results.failure("导出请传入时间区间!");
		}
		Result<List<PointLog>> listResult = machineService.machinePointLog(machineCode, startTime, endTime);
		if (listResult.getCode() == Result.FAILURE) {
			return Results.failure(listResult.getMsg());
		}
		List<PointLog> logs = listResult.getData();

		String fileName = "export" + ".xls";
		int rowNum = 1;
		String[] headers = { "时间", "事件" };

		@SuppressWarnings("resource")
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("机器事件");
		HSSFRow row = sheet.createRow(0);

		for (int i = 0; i < headers.length; i++) {
			HSSFCell cell = row.createCell(i);
			HSSFRichTextString text = new HSSFRichTextString(headers[i]);
			cell.setCellValue(text);
		}
		// 在表中存放查询到的数据放入对应的列
		for (PointLog log : logs) {
			HSSFRow row1 = sheet.createRow(rowNum);
			row1.createCell(0).setCellValue(log.getPointTime());
			row1.createCell(1).setCellValue(log.getDetail());
			rowNum++;
		}

		response.setContentType("application/octet-stream");
		response.setHeader("Content-disposition", "attachment;filename=" + fileName);
		response.flushBuffer();
		workbook.write(response.getOutputStream());

		return Results.success();
	}

	/**
	 * 查询机器状态是正常的机器列表
	 *
	 * @param machineStatus
	 * @return
	 */
	@RequestMapping(value = "/findMachineByMachineStatus", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<List<String>> findMachineByMachineStatus(@RequestParam int machineStatus) {
		return machineService.findMachineByMachineStatus(machineStatus);

	}

	/**
	 * 查看机器列表
	 *
	 * @param machineCode
	 * @param localCode
	 * @return
	 */
	@RequestMapping(value = "/list", method = { RequestMethod.POST, RequestMethod.GET })
	public ModelAndView list(String machineCode, String localCode, String startTime, String endTime, String machineType,
			String machineStatus, String localType) {
		Result<List<MachineListVo>> list = machineService.findMachines(machineCode, localCode, startTime, endTime,
				machineType, machineStatus, localType);
		return ResultPages.page(list);
	}

	/**
	 * 查看机器排期计划
	 *
	 * @param machineCode
	 * @param localCode
	 * @return
	 */
	@RequestMapping(value = "/planList", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<List<MachineListVo1>> planList(@RequestParam(required = false) String machineCode,
			@RequestParam(required = false) String status, @RequestParam(required = false) String localCode,
			@RequestParam(required = false) String startTime, @RequestParam(required = false) String endTime) {
		List<MachineListVo1> list = machineService.findMachinePlan(machineCode, status, localCode, startTime, endTime);
		return ResultGenerator.genSuccessResult(list);
	}

	/**
	 * 更新点位
	 *
	 * @param localeId
	 * @param address
	 * @return
	 */
	@RequestMapping(value = "/updateLocale", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> updateLocale(@RequestParam String id, @RequestParam String localeId,
			@RequestParam(defaultValue = "") String address) {
		return machineService.updateLocale(id, localeId, address);

	}

	/**
	 * 更新机器
	 * 
	 * @param vo
	 * @return
	 */
	@RequestMapping(value = "/updateMachine", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> updateMachine(UpdateMachineVo vo) {
		return machineService.updateMachine(vo);

	}

	/**
	 * 查询机器信息
	 * 
	 * @param machineId
	 * @return
	 */
	@RequestMapping(value = "/findMachineInfoById", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<Inno72Machine> findMachineInfoById(@RequestParam String machineId) {
		return machineService.findMachineInfoById(machineId);

	}

	/**
	 * 货道详情
	 *
	 * @param machineId
	 * @return
	 */
	@RequestMapping(value = "/channelInfo", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<List<ChannelListVo>> channelInfo(@RequestParam String machineId) {
		return machineService.channelList(machineId);

	}

	/**
	 * 启用、停用货道（1停用，0请用）
	 *
	 * @param channels
	 * @return
	 */
	@RequestMapping(value = "/deleteChannel", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> deleteChannel(@RequestBody List<UpdateMachineChannelVo> channels) {
		return machineService.deleteChannel(channels);

	}

	/**
	 * 更新商品余量
	 *
	 * @param channels
	 * @return
	 */
	@RequestMapping(value = "/updateGoodsCount", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> updateGoodsCount(@RequestBody List<UpdateMachineChannelVo> channels) {
		return machineService.updateGoodsCount(channels);

	}

	/**
	 * 查询机器状态
	 *
	 * @param machineId
	 * @return
	 */
	@RequestMapping(value = "/machineStatus", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<MachineStatusVo> machineStatus(@RequestParam String machineId) {
		return machineService.machineStatus(machineId);

	}

	/**
	 * 查询app状态
	 *
	 * @param machineId
	 * @return
	 */
	@RequestMapping(value = "/appStatus", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<MachineAppStatus> appStatus(@RequestParam String machineId) {
		return machineService.appStatus(machineId);

	}

	/**
	 * 更新数据
	 *
	 * @param machineId
	 * @param updateStatus
	 * @return
	 */
	@RequestMapping(value = "/updateInfo", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> updateInfo(@RequestParam String machineId, @RequestParam Integer updateStatus) {
		return machineService.updateInfo(machineId, updateStatus);

	}

	/**
	 * 切换app
	 * 
	 * @param machineId
	 * @param appPackageName
	 * @return
	 */
	@RequestMapping(value = "/cutApp", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> cutApp(@RequestParam String machineId, @RequestParam String appPackageName) {
		return machineService.cutApp(machineId, appPackageName);

	}

	/**
	 * 安装app
	 * 
	 * @param machineId
	 * @param appPackageName
	 * @param url
	 * @param versionCode
	 * @return
	 */
	@RequestMapping(value = "/installApp", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> installApp(@RequestParam String machineId, @RequestParam String appPackageName,
			@RequestParam String url, @RequestParam Integer versionCode) {
		return machineService.installApp(machineId, appPackageName, url, versionCode);

	}

	/**
	 * 获取机器首页数据
	 * 
	 * @return
	 */
	@RequestMapping(value = "/findMachinePortalData", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<MachinePortalVo> findMachinePortalData() {
		return machineService.findMachinePortalData();
	}

	/**
	 * 查询异常机器列表
	 * 
	 * @param type
	 * @return
	 */
	@RequestMapping(value = "/findExceptionMachine", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<List<MachineExceptionVo>> findExceptionMachine(@RequestParam Integer type) {
		return machineService.findExceptionMachine(type);
	}

	/**
	 * 查询机器缺货详情
	 * 
	 * @param machineId
	 * @return
	 */
	@RequestMapping(value = "/findMachineStockoutInfo", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<List<MachineStockOutInfo>> findMachineStockoutInfo(@RequestParam String machineId) {
		return machineService.findMachineStockoutInfo(machineId);
	}

	/**
	 * 修改机器编号
	 * 
	 * @param machineId
	 * @param machineCode
	 * @return
	 */
	@RequestMapping(value = "/updateMachineCode", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> updateMachineCode(@RequestParam String machineId, @RequestParam String machineCode) {
		return machineService.updateMachineCode(machineId, machineCode);

	}

	/**
	 * 切换桌面
	 * 
	 * @param machineId
	 * @param status
	 * @return
	 */
	@RequestMapping(value = "/cutDesktop", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> cutDesktop(@RequestParam String machineId, @RequestParam Integer status) {
		return machineService.cutDesktop(machineId, status);

	}

	/**
	 * 更改温度
	 * 
	 * @param machineId
	 * @param temperature
	 * @return
	 */
	@RequestMapping(value = "/updateTemperature", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> updateTemperature(@RequestParam String machineId, @RequestParam Integer temperature) {
		return machineService.updateTemperature(machineId, temperature);

	}

	/**
	 * 查询温度
	 * 
	 * @param machineId
	 * @return
	 */
	@RequestMapping(value = "/findTemperature", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> findTemperature(@RequestParam String machineId) {
		return machineService.findTemperature(machineId);

	}

	/**
	 * 抓取日志
	 * 
	 * @param machineId
	 * @param logType
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	@RequestMapping(value = "/grabLog", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> grabLog(@RequestParam String machineId, @RequestParam Integer logType,
			@RequestParam String startTime, @RequestParam String endTime) {
		return machineService.grabLog(machineId, logType, startTime, endTime);

	}

	/**
	 * 查询日志列表
	 * 
	 * @param machineId
	 * @return
	 */
	@RequestMapping(value = "/getLogs", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<List<Inno72AppLog>> getLogs(@RequestParam String machineId) {
		return machineService.getLogs(machineId);

	}

	/**
	 * 更新机器类型
	 * 
	 * @param machineId
	 * @param machineType
	 * @return
	 */
	@RequestMapping(value = "/updateMachineType", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> updateMachineType(@RequestParam String machineId, @RequestParam Integer machineType) {
		return machineService.updateMachineType(machineId, machineType);

	}

}
