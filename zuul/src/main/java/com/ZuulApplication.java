package com;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.netflix.turbine.EnableTurbine;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

import com.inno72.springboot.web.SpringApplicationBuilder;
import com.inno72.springboot.web.SpringBootServletInitializer;

@SpringBootApplication(scanBasePackages = { "com.inno72" })
@EnableZuulProxy
@EnableEurekaClient
@EnableHystrixDashboard
@EnableTurbine
public class ZuulApplication extends SpringBootServletInitializer {
	public static void main(String[] args) {
		new SpringApplicationBuilder(ZuulApplication.class, "zuul", args);
	}

	@Override
	public String setAppNameForLog() {
		return "zuul";
	}
}
