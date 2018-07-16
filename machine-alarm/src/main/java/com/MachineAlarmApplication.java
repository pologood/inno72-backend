package com;

import com.inno72.common.MachineAlarmProperties;
import com.inno72.springboot.web.SpringApplicationBuilder;
import com.inno72.springboot.web.SpringBootServletInitializer;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = { "com.inno72" })
@EnableFeignClients
@EnableEurekaClient
@EnableCircuitBreaker // 开启熔断
@EnableConfigurationProperties({MachineAlarmProperties.class })
public class MachineAlarmApplication extends SpringBootServletInitializer {
	public static void main(String[] args) {
		new SpringApplicationBuilder(MachineAlarmApplication.class, "machine-alarm", args);
	}

	@Override
	public String setAppNameForLog() {
		return "machine-alarm";
	}
}
