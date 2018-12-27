package com;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

import com.inno72.common.MachineBackendProperties;
import com.inno72.config.client.QyhProperties;
import com.inno72.springboot.web.SpringApplicationBuilder;
import com.inno72.springboot.web.SpringBootServletInitializer;

@SpringBootApplication(scanBasePackages = { "com.inno72" })
@EnableFeignClients
@EnableEurekaClient
@EnableCircuitBreaker // 开启熔断
@EnableConfigurationProperties({ MachineBackendProperties.class, QyhProperties.class })
public class MachineStoreBackendApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		new SpringApplicationBuilder(MachineStoreBackendApplication.class, "machine-store-backend", args);
	}

	@Override
	public String setAppNameForLog() {
		return "machine-store-backend";
	}
}
