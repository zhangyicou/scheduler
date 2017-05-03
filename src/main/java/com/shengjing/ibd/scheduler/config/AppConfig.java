package com.shengjing.ibd.scheduler.config;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.web.client.RestTemplate;

import javax.inject.Inject;

@Configuration
@Import(DataSourceConfig.class)
public class AppConfig {
    private static Logger log = Logger.getLogger(AppConfig.class);

    @Bean
    @Scope("singleton")
    public RestTemplate getRestTemplate(){
        RestTemplate restTemplate = new RestTemplate();

        return restTemplate;
    }

    @Bean
    @Scope("singleton")
    public HttpHeaders getHttpHeaders(){
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON_UTF8_VALUE);
        return headers;
    }
}
