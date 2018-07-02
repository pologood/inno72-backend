package com.inno72.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.inno72.model.Inno72AdminArea;

public class InitAdminArea {

	public static String readToString(String fileName) {
		String encoding = "UTF-8";
		File file = new File(fileName);
		Long filelength = file.length();
		byte[] filecontent = new byte[filelength.intValue()];
		try {
			FileInputStream in = new FileInputStream(file);
			in.read(filecontent);
			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			return new String(filecontent, encoding);
		} catch (UnsupportedEncodingException e) {
			System.err.println("The OS does not support " + encoding);
			e.printStackTrace();
			return null;
		}
	}

	public static List<Inno72AdminArea> main() {
		String a = readToString("/Users/lzh/admin-area.txt");
		List<AdminArea> admin = JSON.parseArray(a, AdminArea.class);
		List<Inno72AdminArea> list = new ArrayList<>();
		for (int i = 0; i < admin.size(); i++) {
			List<City> citys = admin.get(i).getCities();
			int provinceCodeInt = i + 10;
			Inno72AdminArea provice = new Inno72AdminArea();
			provice.setName(admin.get(i).getName());
			provice.setLevel(1);
			String provinceCode = String.valueOf(provinceCodeInt) + "0000000";
			provice.setCode(provinceCode);
			provice.setProvince(admin.get(i).getName());
			list.add(provice);
			for (int j = 0; j < citys.size(); j++) {
				List<Country> countrys = citys.get(j).getCounties();
				int cityCodeInt = j + 1;
				String cs = String.valueOf(cityCodeInt);
				if (cs.length() == 1) {
					cs = "0" + cs;
				}
				Inno72AdminArea city = new Inno72AdminArea();
				city.setName(citys.get(j).getName());
				city.setLevel(2);
				city.setProvince(provice.getName());
				city.setCity(citys.get(j).getName());
				String cityCode = String.valueOf(provinceCodeInt) + cs + "00000";
				city.setCode(cityCode);
				city.setParentCode(provice.getCode());
				list.add(city);
				for (int k = 0; k < countrys.size(); k++) {
					List<Circles> circles = countrys.get(k).getCircles();
					int countryCodeInt = k + 1;
					String ccs = String.valueOf(countryCodeInt);
					if (ccs.length() == 1) {
						ccs = "0" + ccs;
					}
					Inno72AdminArea country = new Inno72AdminArea();
					country.setName(countrys.get(k).getName());
					country.setLevel(3);
					String countryCode = String.valueOf(provinceCodeInt) + cs + ccs + "000";
					country.setCode(countryCode);
					country.setProvince(provice.getName());
					country.setCity(city.getName());
					country.setDistrict(countrys.get(k).getName());
					country.setParentCode(city.getCode());
					list.add(country);
					for (int h = 0; h < circles.size(); h++) {
						int circlesCodeInt = h + 1;
						String cccs = String.valueOf(circlesCodeInt);
						if (cccs.length() == 1) {
							cccs = "00" + cccs;
						} else if (cccs.length() == 2) {
							cccs = "0" + cccs;
						}
						Inno72AdminArea circle = new Inno72AdminArea();
						circle.setName(circles.get(h).getName());
						circle.setLevel(4);
						String circleCode = String.valueOf(provinceCodeInt) + cs + ccs + cccs;
						circle.setCode(circleCode);
						circle.setProvince(provice.getName());
						circle.setCity(city.getName());
						circle.setDistrict(country.getName());
						circle.setCircle(circles.get(h).getName());
						circle.setParentCode(country.getCode());
						list.add(circle);
					}
				}
			}
		}
		return list;
	}
}
