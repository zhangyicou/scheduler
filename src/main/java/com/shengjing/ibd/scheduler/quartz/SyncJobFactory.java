package com.shengjing.ibd.scheduler.quartz;

import com.shengjing.ibd.scheduler.model.ScheduleJob;
import com.shengjing.ibd.scheduler.vo.ScheduleJobVo;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.web.client.RestTemplate;

/**
 * author : fengjing
 * createTime : 2016-08-04
 * description : 同步任务工厂
 * version : 1.0
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class SyncJobFactory extends QuartzJobBean {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private RestTemplate restTemplate = new RestTemplate();

    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        logger.info("SyncJobFactory execute");
        JobDataMap mergedJobDataMap = context.getMergedJobDataMap();
        ScheduleJob scheduleJob = (ScheduleJob) mergedJobDataMap.get(ScheduleJobVo.JOB_PARAM_KEY);
        logger.info("jobName:" + scheduleJob.getJobName() + "  " + scheduleJob);
        Object result = restTemplate.getForObject(scheduleJob.getUrl(), Object.class);
        logger.info("result:"+result.toString());
    }
}
