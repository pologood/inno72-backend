package com.inno72.zuul.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.inno72.zuul.filter.LogFilter;

/**
 * 过滤器配置
 * 
 * @author Houkm
 *
 *         2017年5月26日
 */
@Configuration
public class FilterConfig {

	/**
	 * 日志过滤器
	 * 
	 * @return
	 * @author Houkm 2017年5月26日
	 */
	@Bean
	public LogFilter logFilter() {
		return new LogFilter();
	}

}
