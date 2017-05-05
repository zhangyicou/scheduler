package com.shengjing.ibd.scheduler.config;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Scope;
import org.springframework.core.Ordered;
import org.springframework.core.env.Environment;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.velocity.VelocityConfig;
import org.springframework.web.servlet.view.velocity.VelocityConfigurer;
import org.springframework.web.servlet.view.velocity.VelocityToolboxView;
import org.springframework.web.servlet.view.velocity.VelocityViewResolver;

import javax.inject.Inject;

@Configuration
public class AppConfig {
    private static Logger log = Logger.getLogger(AppConfig.class);

//    @Bean
//    @Scope("singleton")
//    public RestTemplate getRestTemplate(){
//        RestTemplate restTemplate = new RestTemplate();
//
//        return restTemplate;
//    }
//
//    @Bean
//    @Scope("singleton")
//    public HttpHeaders getHttpHeaders(){
//        HttpHeaders headers = new HttpHeaders();
//        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
//        headers.setContentType(type);
//        headers.add("Accept", MediaType.APPLICATION_JSON_UTF8_VALUE);
//        return headers;
//    }

//    @Autowired
//    private final ResourceLoader resourceLoader = new DefaultResourceLoader();
//
//    @Bean
//    public VelocityConfig velocityConfig() {
//        VelocityConfigurer cfg = new VelocityConfigurer();
//        cfg.setResourceLoader(resourceLoader);
//        cfg.setResourceLoaderPath("/templates/");
//        return cfg;
//    }
//
//    @Bean
//    public ViewResolver viewResolver() {
//        VelocityViewResolver resolver = new VelocityViewResolver();
//        resolver.setViewClass(VelocityToolboxView.class);
//        //resolver.setToolboxConfigLocation("classpath:/WEB-INF/toolbox.xml");
//        resolver.setPrefix("/");
//        resolver.setSuffix(".vm");
//        resolver.setCache(false);
//        resolver.setOrder(Ordered.LOWEST_PRECEDENCE - 20);
//        return resolver;
//    }
}
