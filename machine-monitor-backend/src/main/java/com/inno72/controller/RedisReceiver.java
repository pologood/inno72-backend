package com.inno72.controller;

import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
public class RedisReceiver {

	public void receiveMessage(String message) throws UnsupportedEncodingException {

		System.out.println("收到消息" + message);
	}
}
