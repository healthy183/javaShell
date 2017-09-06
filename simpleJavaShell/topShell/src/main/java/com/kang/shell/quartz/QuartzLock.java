package com.kang.shell.quartz;

import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Date;

/**
 * @Title 类名
 * @Description 描述
 * @Date 2017/9/5.
 * @Author Healthy
 * @Version
 */
@Slf4j
@Component
public class QuartzLock {

    @Autowired
    private Scheduler scheduler;
    @Autowired
    @Qualifier("runReportDetail")
    private MethodInvokingJobDetailFactoryBean runReportDetail;
    @Autowired
    @Qualifier("runReportTrigger")
    private CronTriggerFactoryBean runReportTrigger;

    @PostConstruct
    public void watchLock(){
        try {
            scheduler.clear();
            CronTriggerImpl cronTrigger = (CronTriggerImpl) runReportTrigger.getObject();
            cronTrigger.setStartTime(new Date());
            scheduler.scheduleJob(runReportDetail.getObject(),cronTrigger);
            scheduler.start();
        } catch (SchedulerException e) {
            log.info(Throwables.getStackTraceAsString(e));
        }catch (Exception e){
            log.info(Throwables.getStackTraceAsString(e));
        }
    }
}
