package com.inno72.machine.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inno72.common.AbstractService;
import com.inno72.common.ExportExcel;
import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;
import com.inno72.common.Results;
import com.inno72.common.StringUtil;
import com.inno72.machine.mapper.Inno72SupplyChannelHistoryMapper;
import com.inno72.machine.mapper.Inno72SupplyChannelOrderMapper;
import com.inno72.machine.model.Inno72SupplyChannelHistory;
import com.inno72.machine.service.SupplyChannelHistoryService;
import com.inno72.machine.vo.MachineGoodsCount;
import com.inno72.machine.vo.SupplyOrderVo;
import com.inno72.machine.vo.SupplyRequestVo;

@Service
@Transactional
public class SupplyChannelHistoryServiceImpl extends AbstractService<Inno72SupplyChannelHistory>
		implements SupplyChannelHistoryService {
	@Resource
	private Inno72SupplyChannelHistoryMapper inno72SupplyChannelHistoryMapper;

	@Resource
	private Inno72SupplyChannelOrderMapper inno72SupplyChannelOrderMapper;

	// 表格
	public static final String[] USERCHARGE = { "商品名称", "机器编号", "机器点位", "补货前数量", "补货数量", "时间", "剩余数量" };
	public static final String[] USERCOLUMN = { "goodsName", "machineCode", "localDesc", "beforeNum", "afterNum",
			"date", "num" };

	@Override
	public List<SupplyOrderVo> findListByPage(SupplyOrderVo supplyOrderVo) {
		Map<String, Object> map = new HashMap<>();
		String areaCode = supplyOrderVo.getAreaCode();
		String beginTime = supplyOrderVo.getBeginTime();
		String endTime = supplyOrderVo.getEndTime();
		String keyword = supplyOrderVo.getKeyword();

		if (StringUtil.isNotEmpty(areaCode.trim())) {
			int num = StringUtil.getAreaCodeNum(areaCode);
			String likeCode = areaCode.substring(0, num);
			map.put("code", likeCode);
			map.put("num", num);
		}

		if (StringUtil.isNotEmpty(beginTime) && StringUtil.isNotEmpty(beginTime.trim())) {
			map.put("beginTime", beginTime.trim() + " 00:00:00");
		}
		if (StringUtil.isNotEmpty(endTime) && StringUtil.isNotEmpty(endTime.trim())) {
			map.put("endTime", endTime.trim() + " 23:59:59");
		}
		if (StringUtil.isNotEmpty(keyword) && StringUtil.isNotEmpty(keyword.trim())) {
			map.put("keyword", keyword.trim());
		}
		List<SupplyOrderVo> list = inno72SupplyChannelOrderMapper.getOrderListByPage(map);
		return list;
	}

	@Override
	public Result<List<Inno72SupplyChannelHistory>> detail(SupplyRequestVo vo) {
		Map<String, Object> map = new HashMap<>();
		map.put("batchNo", vo.getBatchNo());
		List<Inno72SupplyChannelHistory> historyList = inno72SupplyChannelHistoryMapper.getSupplyOrderGoods(map);
		List<Inno72SupplyChannelHistory> resultList = new ArrayList<>();
		if (historyList != null && historyList.size() > 0) {
			Set<String> set = new HashSet<>();
			for (Inno72SupplyChannelHistory history : historyList) {
				String goodsName = history.getGoodsName();
				if (!set.contains(goodsName)) {
					set.add(goodsName);
					int count = 0;
					for (Inno72SupplyChannelHistory his : historyList) {
						String name = his.getGoodsName();
						if (name.equals(goodsName)) {
							count += his.getSubCount();
						}
					}
					if (count > 0) {
						history.setSubCount(count);
						resultList.add(history);
					}

				}

			}
		}
		return ResultGenerator.genSuccessResult(resultList);
	}

	@Override
	public List<Map<String, Object>> dayGoodsCount(SupplyOrderVo supplyOrderVo) {
		Map<String, Object> map = new HashMap<>();
		String areaCode = supplyOrderVo.getAreaCode();
		String beginTime = supplyOrderVo.getBeginTime();
		String endTime = supplyOrderVo.getEndTime();
		String keyword = supplyOrderVo.getKeyword();

		if (StringUtil.isNotEmpty(areaCode)) {
			int num = StringUtil.getAreaCodeNum(areaCode);
			String likeCode = areaCode.substring(0, num);
			map.put("code", likeCode);
			map.put("num", num);
		}

		if (StringUtil.isNotEmpty(beginTime) && StringUtil.isNotEmpty(beginTime.trim())) {
			map.put("beginTime", beginTime.trim() + " 00:00:00");
		}
		if (StringUtil.isNotEmpty(endTime) && StringUtil.isNotEmpty(endTime.trim())) {
			map.put("endTime", endTime.trim() + " 23:59:59");
		}
		if (StringUtil.isNotEmpty(keyword) && StringUtil.isNotEmpty(keyword.trim())) {
			map.put("keyword", keyword.trim());
		}
		List<Map<String, Object>> list = inno72SupplyChannelHistoryMapper.selectDayGoodsCountByPage(map);
		return list;
	}

	@Override
	public Result<String> dayGoodsCountExcel(SupplyOrderVo supplyOrderVo, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<>();
		String areaCode = supplyOrderVo.getAreaCode();
		String beginTime = supplyOrderVo.getBeginTime();
		String endTime = supplyOrderVo.getEndTime();
		String keyword = supplyOrderVo.getKeyword();

		if (StringUtil.isNotEmpty(areaCode)) {
			int num = StringUtil.getAreaCodeNum(areaCode);
			String likeCode = areaCode.substring(0, num);
			map.put("code", likeCode);
			map.put("num", num);
		}

		if (StringUtil.isNotEmpty(beginTime) && StringUtil.isNotEmpty(beginTime.trim())) {
			map.put("beginTime", beginTime.trim() + " 00:00:00");
		}
		if (StringUtil.isNotEmpty(endTime) && StringUtil.isNotEmpty(endTime.trim())) {
			map.put("endTime", endTime.trim() + " 23:59:59");
		}
		if (StringUtil.isNotEmpty(keyword) && StringUtil.isNotEmpty(keyword.trim())) {
			map.put("keyword", keyword.trim());
		}
		List<MachineGoodsCount> list = inno72SupplyChannelHistoryMapper.selectDayGoodsCountExcel(map);
		if (list.size() > 20000) {
			return Results.failure("数据两大于20000条");
		}
		// 导出excel
		ExportExcel<MachineGoodsCount> ee = new ExportExcel<MachineGoodsCount>();
		ee.setResponseHeader(USERCHARGE, USERCOLUMN, list, response, "商品统计");
		return Results.success();
	}

	@Override
	public List<Map<String, Object>> dayGoodsList(SupplyOrderVo supplyOrderVo) {
		Map<String, Object> map = new HashMap<>();
		String areaCode = supplyOrderVo.getAreaCode();
		String beginTime = supplyOrderVo.getBeginTime();
		String endTime = supplyOrderVo.getEndTime();
		String keyword = supplyOrderVo.getKeyword();

		if (StringUtil.isNotBlank(areaCode)) {
			int num = StringUtil.getAreaCodeNum(areaCode);
			String likeCode = areaCode.substring(0, num);
			map.put("code", likeCode);
			map.put("num", num);
		}

		if (StringUtil.isNotEmpty(beginTime) && StringUtil.isNotEmpty(beginTime.trim())) {
			map.put("beginTime", beginTime.trim() + " 00:00:00");
		}
		if (StringUtil.isNotEmpty(endTime) && StringUtil.isNotEmpty(endTime.trim())) {
			map.put("endTime", endTime.trim() + " 23:59:59");
		}
		if (StringUtil.isNotEmpty(keyword) && StringUtil.isNotEmpty(keyword.trim())) {
			map.put("keyword", keyword.trim());
		}
		List<Map<String, Object>> list = inno72SupplyChannelHistoryMapper.selectDayGoodsListByPage(map);
		return list;
	}

	@Override
	public List<Map<String, Object>> dayGoodsDetail(String machineId, String dateTime) {
		Map<String, Object> map = new HashMap<>();
		map.put("machineId", machineId);
		if (StringUtil.isNotEmpty(dateTime) && StringUtil.isNotEmpty(dateTime.trim())) {
			map.put("beginTime", dateTime.trim() + " 00:00:00");
			map.put("endTime", dateTime.trim() + " 23:59:59");
		}

		return inno72SupplyChannelHistoryMapper.selectDayGoodsDetail(map);
	}

}
