package com.echo.config.quarz;

import com.echo.service.ProductSellDailyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

@Configuration
public class QuarzConfiguration {

    @Autowired
    private ProductSellDailyService productSellDailyService;

    @Autowired
    private MethodInvokingJobDetailFactoryBean jobDetailFactoryBean;

    @Autowired
    private CronTriggerFactoryBean productSellDailyTriggerFactory;


    //创建jobDetail并返回
    @Bean("jobDetailFactoryBean")
    public MethodInvokingJobDetailFactoryBean createJobDetail() {

        //此工厂主要用来制作一个jobDetail
        MethodInvokingJobDetailFactoryBean jobDetailFactoryBean = new MethodInvokingJobDetailFactoryBean();

        //设置jobDetail名字
        jobDetailFactoryBean.setName("product_sell_daily_job");

        //设置jobDetail的组名
        jobDetailFactoryBean.setGroup("job_product_sell_daily_group");

        //对于相同的JobDetail，当指定多个Trigger时，第一个job完成之前，第二个job就开始了。
        //指定concurrent设为false，多个job不会并发运行。

        jobDetailFactoryBean.setConcurrent(false);

        //指定运行任务的类
        jobDetailFactoryBean.setTargetObject(productSellDailyService);

        //指定运行任务的方法
        jobDetailFactoryBean.setTargetMethod("dailyCalculate");

        return jobDetailFactoryBean;
    }

    //创建cronTrigger并返回
    @Bean("productSellDailyTriggerFactory")

    public CronTriggerFactoryBean createProductSellDailyTrigger() {
        //创建trigger
        CronTriggerFactoryBean triggerFactory = new CronTriggerFactoryBean();

        //设置名字
        triggerFactory.setName("product_sell_daily_trigger");

        //组名
        triggerFactory.setGroup("job_product_sell_daily_group");

        //绑定jobDetail
        triggerFactory.setJobDetail(jobDetailFactoryBean.getObject());

        //设定cron表达式,生成器生成~  每天的0点执行一次
        triggerFactory.setCronExpression("00 00 10 * * ? *");
        return triggerFactory;
    }

    //创建调度工厂并返回
    @Bean("schedulerFactory")
    public SchedulerFactoryBean createSchedulerFactory() {
        SchedulerFactoryBean schedulerFactory = new SchedulerFactoryBean();
        schedulerFactory.setTriggers(productSellDailyTriggerFactory.getObject());
        return schedulerFactory;
    }


}

