package com;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

import com.inno72.springboot.web.SpringApplicationBuilder;
import com.inno72.springboot.web.SpringBootServletInitializer;

@SpringBootApplication(scanBasePackages = { "com.inno72" })
@EnableFeignClients
@EnableEurekaClient
@EnableCircuitBreaker // 开启熔断
public class MachineRemoteBackendApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		new SpringApplicationBuilder(MachineRemoteBackendApplication.class, "machine-remote-backend", args);
	}

	@Override
	public String setAppNameForLog() {
		return "machine-remote-backend";
	}
}
