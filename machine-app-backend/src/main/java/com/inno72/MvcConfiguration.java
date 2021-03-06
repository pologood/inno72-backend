package com.inno72;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.MultipartConfigElement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.http.converter.json.Jackson2ObjectMapperFactoryBean;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inno72.common.filter.ProcessDataFilter;
import com.inno72.common.interceptor.LogInterceptor;
import com.inno72.common.interceptor.PageListAttrHandlerInterceptor;
import com.inno72.common.spring.JsonView;
import com.inno72.redis.IRedisUtil;

@Configuration
@EnableWebMvc
public class MvcConfiguration extends WebMvcConfigurerAdapter {

	private Logger logger = LoggerFactory.getLogger(MvcConfiguration.class);

	@Resource
	private IRedisUtil redisUtil;

	public View jsonView() {
		logger.info("***********************init JsonView**************************");
		JsonView view = new JsonView();
		view.setExtractValueFromSingleKeyModel(true);
		Jackson2ObjectMapperFactoryBean objectMapperFactoryBean = new Jackson2ObjectMapperFactoryBean();
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapperFactoryBean.setObjectMapper(objectMapper);
		objectMapperFactoryBean.setSerializationInclusion(Include.NON_NULL);
		objectMapperFactoryBean.afterPropertiesSet();
		view.setObjectMapper(objectMapperFactoryBean.getObject());
		return view;
	}

	/**
	 * 配置解析器
	 *
	 * @return
	 * @Date 2017年5月4日
	 * @Author Houkm
	 */
	@Bean
	public ViewResolver viewResolver() {
		logger.info("***********************init ViewResolver**************************");
		ContentNegotiatingViewResolver resolver = new ContentNegotiatingViewResolver();
		List<View> views = new ArrayList<>();
		views.add(jsonView());
		resolver.setDefaultViews(views);
		return resolver;
	}

	/**
	 * 国际化的消息资源文件
	 *
	 * @return
	 * @Date 2017年5月3日
	 * @Author Houkm
	 */
	@Bean
	public ReloadableResourceBundleMessageSource messageSource() {
		logger.info("***********************init ReloadableResourceBundleMessageSource**************************");
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasenames("classpath:conf/messages");
		messageSource.setCacheSeconds(60);
		messageSource.setDefaultEncoding("UTF-8");
		return messageSource;
	}

	/**
	 * 拦截器
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new PageListAttrHandlerInterceptor()).addPathPatterns("/**");
		LogInterceptor logInterceptor = new LogInterceptor();
		registry.addInterceptor(logInterceptor).addPathPatterns("/**");
	}

	@Bean
	public FilterRegistrationBean someFilterRegistration() {
		FilterRegistrationBean registration = new FilterRegistrationBean();
		registration.setFilter(new ProcessDataFilter());
		registration.addUrlPatterns("/*");
		return registration;
	}

	@Bean
	public MultipartConfigElement multipartConfigElement() {
		MultipartConfigFactory factory = new MultipartConfigFactory();
		// 单个数据大小
		factory.setMaxFileSize("10240KB"); // KB,MB
		/// 总上传数据大小
		factory.setMaxRequestSize("102400KB");
		return factory.createMultipartConfig();
	}

	// @Override
	// public void configureMessageConverters(List<HttpMessageConverter<?>>
	// converters) {
	// converters.add(mappingJackson2HttpMessageConverter());
	// }
	//
	// @Bean
	// public MappingJackson2HttpMessageConverter
	// mappingJackson2HttpMessageConverter() {
	// return new MappingJackson2HttpMessageConverter() {
	// // 重写 writeInternal 方法，在返回内容前首先进行加密
	// @Override
	// protected void writeInternal(Object object, HttpOutputMessage
	// outputMessage)
	// throws IOException, HttpMessageNotWritableException {
	// // 使用 Jackson 的 ObjectMapper 将 Java 对象转换成 Json String
	// ObjectMapper mapper = new ObjectMapper();
	// String json = mapper.writeValueAsString(object);
	// // 加密
	// String result = json + "加密了！";
	// // 输出
	// outputMessage.getBody().write(result.getBytes());
	// }
	// };
	// }

}