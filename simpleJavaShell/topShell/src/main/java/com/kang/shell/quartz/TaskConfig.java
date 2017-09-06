package com.kang.shell.quartz;

import com.kang.shell.constants.TaskConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SyncTaskExecutor;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

/**
 * @Title 类名
 * @Description 描述
 * @Date 2017/9/5.
 * @Author Healthy
 * @Version
 */
@Configuration
public class TaskConfig {

    @Autowired
    private TaskConstants taskConstants;

    @Bean(name="runReportDetail")
    public MethodInvokingJobDetailFactoryBean runReportDetail(){
        MethodInvokingJobDetailFactoryBean uploadTask = new MethodInvokingJobDetailFactoryBean();
        uploadTask.setTargetBeanName("quartzRun");
        uploadTask.setTargetMethod("runReport");
        uploadTask.setConcurrent(false);
        return uploadTask;
    }

    @Bean(name="runReportTrigger")
    public CronTriggerFactoryBean runReportTrigger(){
        CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();
        cronTriggerFactoryBean.setJobDetail(runReportDetail().getObject());
        cronTriggerFactoryBean.setCronExpression(taskConstants.getRunReport());
        return cronTriggerFactoryBean;
    }

    @Bean
    public SchedulerFactoryBean SchedulerFactoryBean(){
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
        SyncTaskExecutor syncTaskExecutor = new SyncTaskExecutor();//指定单线程执行器
        schedulerFactoryBean.setTaskExecutor(syncTaskExecutor);
        return schedulerFactoryBean;
    }
}
