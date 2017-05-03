package com.shengjing.ibd.scheduler.quartz;

import com.shengjing.ibd.scheduler.model.ScheduleJob;
import com.shengjing.ibd.scheduler.vo.ScheduleJobVo;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.web.client.RestTemplate;

/**
 * author : fengjing
 * createTime : 2016-08-04
 * description : 异步任务工厂
 * version : 1.0
 */
public class AsyncJobFactory extends QuartzJobBean {

    /* 日志对象 */
    private static final Logger LOG = LoggerFactory.getLogger(AsyncJobFactory.class);
    private RestTemplate restTemplate = new RestTemplate();
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        LOG.info("AsyncJobFactory execute");
        ScheduleJob scheduleJob = (ScheduleJob) context.getMergedJobDataMap().get(ScheduleJobVo.JOB_PARAM_KEY);
        if("1".equals(scheduleJob.getStatus())) {
            LOG.info("jobName:" + scheduleJob.getJobName() + "  " + scheduleJob);
            Object result = restTemplate.getForObject(scheduleJob.getUrl(), Object.class);
            LOG.info("result:" + result.toString());
        }
    }
}
