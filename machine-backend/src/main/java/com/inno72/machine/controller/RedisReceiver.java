package com.inno72.machine.controller;

import java.io.UnsupportedEncodingException;

import org.springframework.stereotype.Service;

@Service
public class RedisReceiver {

	public void receiveMessage(String message) throws UnsupportedEncodingException {

		System.out.println(new String(message.getBytes("GB2312"), "UTF-8") + "======================");
		System.out.println(new String(message.getBytes("iso8859-1"), "UTF-8") + "======================");
		System.out.println(new String(message.getBytes("GBK"), "UTF-8") + "======================");

	}
}
