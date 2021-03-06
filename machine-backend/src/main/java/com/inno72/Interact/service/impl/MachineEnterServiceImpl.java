package com.inno72.Interact.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AntMerchantExpandAutomatApplyUploadRequest;
import com.alipay.api.response.AntMerchantExpandAutomatApplyUploadResponse;
import com.inno72.Interact.mapper.Inno72MachineEnterMapper;
import com.inno72.Interact.service.MachineEnterService;
import com.inno72.Interact.vo.AlipayAutomat;
import com.inno72.common.DateUtil;
import com.inno72.common.Result;
import com.inno72.common.Results;
import com.inno72.common.json.JsonUtil;
import com.inno72.machine.mapper.Inno72LocaleMapper;
import com.inno72.machine.mapper.Inno72MachineMapper;
import com.inno72.share.mapper.Inno72AlipayAreaMapper;
import com.inno72.share.model.Inno72AlipayArea;

import tk.mybatis.mapper.entity.Condition;

@Service
@Transactional
public class MachineEnterServiceImpl implements MachineEnterService {
	private static Logger logger = LoggerFactory.getLogger(MachineEnterServiceImpl.class);

	private String alipayPrivateKey = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQC8o5HZOvFNmUlMNyrUC4GMC47Wqp8MyrokYbk9EsgL1g/mHsuxGi3W1P1LJg8NDknCNKPFuo8Uqs0hT7x32V7lG3nstynh0RT72gUxLlnFG7KecRRj6pSnAs/HF4YfaBgC7ftsTnpNyJnW8CBySpbJ8TY4XkwBXaByFyr9D8ID8i8iUpd/qstGB4YzQfeY52ufEi3VfN68Gcco0lVAZDNcglHs9YsWf//sjej7b5LVvR4hqKSp4CyD2dlNLF/i/mSD3zaH3DsID+xbSV37Ij3vkATjuj0LTOVi4OORPVj91V/2Ogixgk2Uq2BPBSoK/gHsydxrn0CLJjIgYH30t61PAgMBAAECggEBALPGCsQEeVzjncgFt0o34PEPPsRz/HnbZgQwIbIDiSRGkAZWCPcyJMddWjtY/PULTd3H/t/8iElA8Zcyf0GUpewgAFVIxaBQevf98f7J1oGTgOmgiLckIyD6+/sX/xlFQcTg+kBglgw1Be+iDrn9PbEcsPiNXU/b44F3dN+ROew5KhuSel7R+xEJazHUfEOaKIa4dOAK47MzwoCiiXf1kLfOIyFdr/X4LqS5QKiN0NT95xvZgGJ3u7S0szpl1B0IXneC7ZbAytHUeLdPg2yaCbUpDb9ZOI7kQGbkTcRdA1ex+1cmXd/HpOOFJBL0Qno4NGtQ+BSL2x54P22xdE1m91kCgYEA4O1ReydWQWrM/ynJsdKS/1tMswvJL8XWd+6IZv0r1hetiWTZh+nUYZj5/kQUiV/wC98J8pqPQDixt8ANiQh579gXCuqbPpwRnI3k6+K8zwwoET9DRnkCT+XAtRDVN437elj22wXyYYQLKD8joSxQd+NOGAkHcE42p6OfYRCVm7sCgYEA1rLmux+4UQjLEC/EMt7gOZVUbXobnTrna1PcdYFVY6+klvRSt8+W9kVppwEoMpz0Pkt8vGF7Bn/oK1OX4i7sJDx8K21SGKPBtE90hsTvJOAGiAa50LfoMMjGVTMtfXCywuFfq1CWNgkiEj71a5xJWFQZ1AtraYt75sCDLgEaOX0CgYB4r1000xp45zEvB+DsxKuS0A4LU5uTQnecyiPt/pFywimeurntLw2BgG9Ceoz6WLuX4wiXX07VipXwnd+lUyL6CdvzQ4YgxeS9N3VJC9N61G77MhKK0YroC8KmZG3C7S+tqeZqBnrSz+KcqaQYaoqSiSfxxYZ0P8Mbl7OAhUGA2QKBgQC6Py/RHE80XMBrJkS5LIau7U/0OH1EBBlFDdatSKjedTv+h6xKMBbxRH3GlkZcwbNPZwSqFpY2qTaqfzW+zJ2lQaMluQwCu+wJkvOvBZ+/CWghEFSZLzCJQWJr/p1zuBQa4o+veZUVAw8/bMZRt54YtbxCKjXftVSidFZXzjUFsQKBgF+Thohiylk9NLqRhbcrDMzGFcfg8VKP5CMwdnhQpmGgqFuwrQpbxoGPJ0Lgishkp02/LB2sJMzZMPEPQqHtWz3cAfePWm3S3CwwQH/NDnPbtu6IAvJ2MCIW/XSwSP0VvGU8imFIWyy5hFcG+WyHSiKtWapVv+YaP8z87+MAK3rl";
	private String alipayPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAnT1ojMWntF1yBfqwJRdPs1VNnn99XJQ9qy0lrtozs2aAIASMEOq+IqJ362BlB+pDug7YZTVe3+H6BBCHnMXByXpeTYkpMU+hFN8XF01m+mXN0wNQO5VCTwzs6Sb7YIFM5gETPERmpWdNDUKugZnwR11kyRByBtbsiWEIzs7E+wWBgOuaQeDe3QnCZnqvC7z16bx9GbU88mYPwa91StIKsh7mqgBQ3csbAShcTAL22fs0Vh+TPCklEQHmrfRweSSVt4l6ieisKOpKcCJ1mF9HxbUBbrVkcmLp9SqSA6JV5PHJm3HfwjfxL26xG5r18I72Rl0cFTR12ZLNSbE6lxAzLQIDAQAB";

	@Resource
	private Inno72MachineMapper inno72MachineMapper;
	@Resource
	private Inno72LocaleMapper inno72LocaleMapper;
	@Resource
	private Inno72MachineEnterMapper inno72MachineEnterMapper;
	@Resource
	private Inno72AlipayAreaMapper inno72AlipayAreaMapper;

	@Override
	public Result<Object> alipayAutomatUpload(String machineId) {
		String appId = "2018112362328253";
		String alipayUserId = "2088131479613438";
		String alipayUploadUrl = "https://openapi.alipay.com/gateway.do";

		try {
			AlipayClient alipayClient = new DefaultAlipayClient(alipayUploadUrl, appId, alipayPrivateKey, "json",
					"utf-8", alipayPublicKey, "RSA2");
			AntMerchantExpandAutomatApplyUploadRequest request = new AntMerchantExpandAutomatApplyUploadRequest();

			Map<String, String> machineAddress = inno72MachineEnterMapper.selectMachineAddress(machineId);
			String address = machineAddress.get("address");
			String district = machineAddress.get("district");

			Inno72AlipayArea inno72AlipayArea = new Inno72AlipayArea();
			inno72AlipayArea.setDistrict(district);

			Condition condition = new Condition(Inno72AlipayArea.class);
			condition.createCriteria().andEqualTo("name", district);
			inno72AlipayArea = inno72AlipayAreaMapper.selectByCondition(condition).get(0);

			String areaCode = inno72AlipayArea.getCode();
			String cityCode = inno72AlipayArea.getParentCode();
			String provinceCode = getParentCode(areaCode, 1);

			AlipayAutomat p = new AlipayAutomat();

			p.setTerminal_id(machineAddress.get("machineCode"));
			p.setProduct_user_id(alipayUserId);
			p.setMerchant_user_id(alipayUserId);
			p.setMachine_type("AUTOMAT");
			p.setMachine_cooperation_type("COOPERATION_EXCLUSIVE");
			p.setMachine_delivery_date(DateUtil.nowStr());
			p.setMachine_name("点72");

			Map<String, String> associate = new HashMap<String, String>();
			associate.put("associate_type ", "DISTRIBUTORS");
			associate.put("associate_user_id", alipayUserId);

			Map<String, String> deliveryAddress = new HashMap<String, String>();
			deliveryAddress.put("province_code", provinceCode);
			deliveryAddress.put("city_code", cityCode);
			deliveryAddress.put("area_code", areaCode);
			deliveryAddress.put("machine_address", address);

			/*
			 * deliveryAddress.put("province_code", "110000");
			 * deliveryAddress.put("city_code", "110100");
			 * deliveryAddress.put("area_code", "110105");
			 * deliveryAddress.put("machine_address", "骏豪朝阳公园广场");
			 */

			Map<String, String> pointPosition = new HashMap<String, String>();
			pointPosition.put("province_code", provinceCode);
			pointPosition.put("city_code", cityCode);
			pointPosition.put("area_code", areaCode);
			pointPosition.put("machine_address", address);

			p.setDelivery_address(deliveryAddress);
			p.setPoint_position(deliveryAddress);
			p.setMerchant_user_type("ALIPAY_MERCHANT");
			// p.setAssociate(associate);
			/*
			 * pointPosition.put("province_code", "110000");
			 * pointPosition.put("city_code", "110100");
			 * pointPosition.put("area_code", "110105");
			 * pointPosition.put("machine_address", "骏豪朝阳公园广场");
			 */

			Map<String, String> scene = new HashMap<String, String>();
			scene.put("level_1", "MALL");
			scene.put("level_2", "001");
			p.setScene(scene);

			request.setBizContent(JsonUtil.toJson(p));
			logger.info("入驻参数：" +  request.getBizContent());

			AntMerchantExpandAutomatApplyUploadResponse response = alipayClient.execute(request);
			if (response.isSuccess()) {
				logger.info("入驻成功：" + response.getAlipayTerminalId());
				return Results.warn("入驻成功", 0, response.getAlipayTerminalId());
			} else {
				logger.info("入驻失败：" + response.getCode() + response.getSubMsg());
				return Results.failure(response.getSubMsg());
			}
		} catch (Exception e) {
			e.printStackTrace();
			return Results.failure("操作失败");
		}
	}

	@Override
	public Result<Object> jingdongAutomatUpload(String machineCode) {
		return Results.warn("入驻成功", 0, "success");
	}

	public static String getParentCode(String areaCode, int level) {
		String coed = "";
		if (level == 1) {
			coed = areaCode.substring(0, 2) + "0000";
		} else if (level == 2) {
			coed = areaCode.substring(0, 4) + "00";
		}
		return coed;
	}

}
