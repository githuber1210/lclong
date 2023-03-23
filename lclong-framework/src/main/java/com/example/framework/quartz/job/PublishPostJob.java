package com.example.framework.quartz.job;

import com.example.framework.quartz.service.IScheduleService;
import com.example.framework.service.IPostsService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PublishPostJob extends QuartzJobBean {
    @Autowired
    private IScheduleService scheduleService;
    @Autowired
    private IPostsService postsService;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        Trigger trigger = jobExecutionContext.getTrigger();
        JobDetail jobDetail = jobExecutionContext.getJobDetail();
        JobDataMap jobDataMap = jobDetail.getJobDataMap();
        Long data = jobDataMap.getLong("data");
        log.debug("定时发布文章操作：{}",data);

        // 获取文章的 ID后获取文章，更新文章为发布的状态，还有发布的时间
        boolean success = postsService.updatePostByScheduler(data);

        //完成后删除触发器和任务
        if (success) {
            log.debug("定时任务执行成功，开始清除定时任务");
            scheduleService.cancelScheduleJob(trigger.getKey().getName());
        }
    }
}