package com.inno72.Interact.controller;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.inno72.Interact.vo.MachineGoods;
import com.inno72.common.Result;

/**
 * Created by CodeGenerator on 2018/09/19.
 */
@FeignClient(value = "INNO72GAMESERVICE")
public interface GameServiceFeignClient {

	@RequestMapping(value = "/newretail/saveMachine", method = RequestMethod.POST)
	Result<String> saveMachine(List<MachineGoods> machineGoodsList);

}
