package com.inno72.machine.service;

import java.util.List;

import com.inno72.machine.vo.SystemStatus;

/**
 * Created by CodeGenerator on 2018/06/29.
 */
public interface TrafficService {

	List<SystemStatus> list(String machineCode, Integer allTraffic);

}
