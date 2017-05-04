package com.shengjing.ibd.scheduler;

import com.shengjing.ibd.scheduler.common.AppSetting;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableAutoConfiguration
@SpringBootApplication
@EnableWebMvc
public class IbdSchedulerApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {

		AppSetting.parseArgs(args);
		SpringApplication.run(IbdSchedulerApplication.class, args);
	}
}
