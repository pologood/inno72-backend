package com.inno72.test;

import java.net.URISyntaxException;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

public class TestSocket {
	private void socketConnect(String machineId) {
		String path = "http://192.168.33.248:1244" + "?channel=1&roomId=1&machineId=" + machineId;
		IO.Options options = new IO.Options();
		options.reconnection = false;
		options.forceNew = true;
		try {
			Socket mSocket = IO.socket(path, options);
			mSocket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
				@Override
				public void call(Object... arg0) {
					System.out.println("连上了");
				}
			});
			mSocket.on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
				@Override
				public void call(Object... arg0) {
					System.out.println("断开了");
				}
			});
			mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, new Emitter.Listener() {
				@Override
				public void call(Object... arg0) {
					System.out.println("error");
				}
			});

			mSocket.connect();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		TestSocket test = new TestSocket();
		for (int i = 0; i < 10000; i++) {
			test.socketConnect("123" + i);
		}
	}
}
