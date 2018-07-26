package com.inno72.testContains;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.alibaba.fastjson.JSON;

public class Test {

	public static void main(String[] args) {
		List<Bean> list = new ArrayList<>();
		for (int i = 0; i < 2000; i++) {
			Bean b = new Bean();
			b.setCode(i + "");
			int _random = new Random().nextInt(5);
			b.setStatus(_random);
			list.add(b);
			b.setId(new Random().nextInt(100) + "");
		}
		System.out.println(JSON.toJSONString(list));
		for (int i = 0; i < 2000; i++) {
			Bean b = new Bean();
			b.setCode(i + "");
			b.setStatus(1);
			System.out.println(i + "" + list.contains(b));
		}
	}
}
