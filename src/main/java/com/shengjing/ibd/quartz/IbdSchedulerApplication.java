package com.shengjing.ibd.quartz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@EnableAutoConfiguration
@SpringBootApplication
@Configuration
@ImportResource(locations = {"classpath:/spring-scheduler.xml"})
public class IbdSchedulerApplication {

	public static void main(String[] args) {
		SpringApplication.run(IbdSchedulerApplication.class, args);
	}
}
