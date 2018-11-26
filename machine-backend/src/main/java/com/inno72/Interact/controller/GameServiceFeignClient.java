package com.inno72.Interact.controller;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by CodeGenerator on 2018/09/19.
 */
@FeignClient(value = "inno72GameService")
public interface GameServiceFeignClient {

	@RequestMapping(value = "/api/standard/setLogged", method = RequestMethod.POST)
	String setLogged(@RequestParam(value = "sessionUuid") String sessionUuid,
			@RequestParam(value = "traceId") String traceId);

}
