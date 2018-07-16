package com.inno72.machine.controller;

import java.io.UnsupportedEncodingException;

import org.springframework.stereotype.Service;

@Service
public class RedisReceiver {

	public void receiveMessage(String message) throws UnsupportedEncodingException {

		System.out.println("收到消息" + message);
	}
}
