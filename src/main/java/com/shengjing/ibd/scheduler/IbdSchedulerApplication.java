package com.shengjing.ibd.scheduler;

import com.shengjing.ibd.scheduler.common.AppSetting;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@EnableAutoConfiguration
@SpringBootApplication
//@Configuration
//@ImportResource(locations = {"classpath:/spring-scheduler.xml"})
public class IbdSchedulerApplication {

	public static void main(String[] args) {

		AppSetting.parseArgs(args);
		SpringApplication.run(IbdSchedulerApplication.class, args);
	}
}
