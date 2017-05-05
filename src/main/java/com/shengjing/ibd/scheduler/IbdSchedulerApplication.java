package com.shengjing.ibd.scheduler;

import com.shengjing.ibd.scheduler.common.AppSetting;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class IbdSchedulerApplication {

	public static void main(String[] args) {

		AppSetting.parseArgs(args);
		SpringApplication.run(IbdSchedulerApplication.class, args);
	}
}
