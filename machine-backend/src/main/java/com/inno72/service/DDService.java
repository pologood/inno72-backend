package com.inno72.service;

import com.inno72.common.Result;

public interface DDService {

	String process(String data, String signature, String timestamp, String nonce);

	Result<String> getToken();

	Result<String> registryCallback(String url);

	Result<String> updateRegistryCallback(String url);

	Result<String> initDData();

}
