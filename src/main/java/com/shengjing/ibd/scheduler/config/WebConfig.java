package com.shengjing.ibd.scheduler.config;

import org.apache.log4j.Logger;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.boot.context.web.ErrorPageFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.velocity.VelocityViewResolver;

import javax.servlet.Filter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * WebMvcConfigurationSupport 使用时要加上@Configuration, 不需要加上@EnableWebMvc。(直接用WebMvcConfigurationSupport就好)
 * 与WebMvcConfigurerAdapter的区别：比WebMvcConfigurerAdapter暴露的方法和可配置项目多
 * adapter中找不到的配置项时，可以考虑用继承WebMvcConfigurationSupport的类来实现。
 */
@Configuration
public class WebConfig extends WebMvcConfigurationSupport {

	private static Logger log = Logger.getLogger(WebMvcConfigurationSupport.class);

	private static final String[] CLASSPATH_RESOURCE_LOCATIONS = {
        "classpath:/META-INF/resources/", "classpath:/resources/",
        "classpath:/static/", "classpath:/templates/" };

	// 用于处理编码问题
	@Bean
	public Filter characterEncodingFilter() {
		CharacterEncodingFilter filter = new CharacterEncodingFilter();
		filter.setEncoding("UTF-8");
		filter.setForceEncoding(true);
		return filter;
	}

//	@Bean
//	public AuthorizationFilter authorizationFilter() {
//		AuthorizationFilter filter = new AuthorizationFilter();
//		return filter;
//	}

	@Bean
	public FilterRegistrationBean characterFilterRegistrationBean(){
		FilterRegistrationBean bean = new FilterRegistrationBean();
		bean.setFilter(characterEncodingFilter());
		bean.setEnabled(true);
		bean.addUrlPatterns("/*");
		bean.setOrder(10);
		return bean;
	}
//
//	@Bean
//	public FilterRegistrationBean authorizationFilterRegistrationBean(){
//		FilterRegistrationBean bean = new FilterRegistrationBean();
//		bean.setFilter(authorizationFilter());
//		bean.setEnabled(true);
//		bean.addUrlPatterns("/*");
//		//bean.setOrder(20);
//		return bean;
//	}

	@Bean
	public ErrorPageFilter errorPageFilter() {
		return new ErrorPageFilter();
	}

	@Bean
	public FilterRegistrationBean disableSpringBootErrorFilter(ErrorPageFilter filter) {
		FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
		filterRegistrationBean.setFilter(filter);
		filterRegistrationBean.setEnabled(false);
		return filterRegistrationBean;
	}

	@Override
	public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
		// Simple strategy: only path extension is taken into account
		// 想测试defaultContentType时，要注意ignoreAcceptHeader的设置，用浏览器测试的时候会自动添加Accept，如下时google添加的。
		// Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8
		configurer.favorPathExtension(true).
			ignoreAcceptHeader(true).
			useJaf(true).
			defaultContentType(MediaType.APPLICATION_JSON).
			mediaType("html", MediaType.TEXT_HTML).
			mediaType("xml", MediaType.APPLICATION_XML).
			mediaType("json", MediaType.APPLICATION_JSON);
	}


//    @Override
//    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
//        configurer.enable();
//    }

	protected void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/**").addResourceLocations(CLASSPATH_RESOURCE_LOCATIONS);
	}

    @Bean
    public InternalResourceViewResolver getJspViewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/");
        resolver.setSuffix(".jsp");
        resolver.setOrder(1);
        return resolver;
    }

    @Bean
    public VelocityViewResolver getVelocityViewResolver() {
		VelocityViewResolver resolver = new VelocityViewResolver();
//        resolver.setViewClass(VelocityToolboxView.class);
        //resolver.setToolboxConfigLocation("classpath:/WEB-INF/toolbox.xml");
        resolver.setSuffix(".vm");
		resolver.setCache(true);
		resolver.setContentType("text/html; charset=UTF-8");
		resolver.setOrder(0);
        return resolver;
    }

    @Bean
    public ViewResolver contentNegotiatingViewResolver(
              ContentNegotiationManager manager) {
         List<ViewResolver> resolvers = new ArrayList<ViewResolver>();
         resolvers.add(getJspViewResolver());
         resolvers.add(getVelocityViewResolver());

         ContentNegotiatingViewResolver resolver = new ContentNegotiatingViewResolver();
         resolver.setViewResolvers(resolvers);
         resolver.setContentNegotiationManager(manager);
         return resolver;
    }

	@Bean
	public SimpleMappingExceptionResolver simpleMappingExceptionResolver() {
		SimpleMappingExceptionResolver resolver = new SimpleMappingExceptionResolver();

		// 异常视图映射
		String viewError404 = "error_404";
		String viewError500 = "error_500";

		Properties mappings = new Properties();
		mappings.put("java.lang.Throwable", viewError500);
		mappings.put("java.lang.Exception", viewError500);
		mappings.put("org.springframework.web.servlet.NoHandlerFoundException", viewError404);

		// 异常HTTP状态码
		Properties statusCodes = new Properties();
		mappings.put(viewError500, 500);
		mappings.put(viewError404, 404);

		// 设置 异常视图映射
		resolver.setExceptionMappings(mappings);

		// 设置 异常HTTP状态码
		resolver.setStatusCodes(statusCodes);

		// 默认错误页面，当找不到上面mappings中指定的异常对应视图时，使用本默认配置
		resolver.setDefaultErrorView(viewError500);

		// 默认HTTP状态码
		resolver.setDefaultStatusCode(500);

		// 设置日志输出级别，不定义则默认不输出警告等错误日志信息
		resolver.setWarnLogCategory("WARN");

		return resolver;
	}

}
