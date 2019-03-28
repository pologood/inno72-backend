package com.inno72.share.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inno72.share.mapper.Inno72AlipayAreaMapper;
import com.inno72.share.model.Inno72AlipayArea;

@Service
@Transactional
public class InitAlipayArea {

	@Resource
	private Inno72AlipayAreaMapper alipayAreaMapper;

	private List<Inno72AlipayArea> list = new ArrayList<>();

	public List<Inno72AlipayArea> init(String fileName) {
		// 设置默认编码

		File file = new File(fileName);
		if (file.isFile() && file.exists()) {
			try {
				FileInputStream fileInputStream = new FileInputStream(file);
				InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
				BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
				String text = null;
				while ((text = bufferedReader.readLine()) != null) {
					initArea(text);
				}
				int n = alipayAreaMapper.insertAlipayAreaList(list);
				return list;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public void initArea(String info) {
		String[] areaInfo = info.split(",");
		String name = areaInfo[0];
		String code = areaInfo[1];

		Inno72AlipayArea area = new Inno72AlipayArea();
		area.setCode(code);
		area.setName(name.trim());
		int level = getAreaCodeNum(code);
		area.setLevel(level);
		if (level == 1) {
			area.setProvince(area.getName());
		}
		if (level == 2) {
			area.setCity(area.getName());
			area.setParentCode(getParentCode(code, 1));
		}
		if (level == 3) {
			area.setDistrict(area.getName());
			area.setParentCode(getParentCode(code, 2));
		}
		System.out.println("名称：" + name + "编码：" + code + "级别：" + level);
		list.add(area);
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

	public static int getAreaCodeNum(String s) {
		for (int i = s.length() - 1; i >= 0; i--) {
			if (!"0".equals(String.valueOf(s.charAt(i)))) {
				if (i < 2) {
					return 1;
				} else if (i < 4) {
					return 2;
				} else {
					return 3;
				}
			}
		}
		return 0;
	}

	public static void main(String[] args) {
		/*
		 * list = readToString("/Users/72cy-0101-01-0014/aaaa.txt"); int n =
		 * alipayAreaMapper.insertAlipayAreaList(list); System.out.println(n);
		 */
	}

}
