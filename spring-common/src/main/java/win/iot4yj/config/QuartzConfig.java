package win.iot4yj.config;

import java.io.IOException;
import java.util.Properties;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

/**
 * quartz定时任务配置类。quartz能做的事很多，如发邮件、短信等。还可以配合spring batch对数据定时批量处理。
 * 深入内容可以参考：龙果学院。
 * 一个Job可以绑定到多个Trigger上面，但是一个Trigger不能触发多个Job。
 * 这是一种固定配置的方式，如果我们做了持久化就只需要编写Job，然后可以动态注册任务。
 * @author yj
 */
@Configuration
public class QuartzConfig {

    private static final Logger log = LoggerFactory.getLogger(QuartzConfig.class);
    /**
     * 用于定义任务的一些属性信息
     */
    /*@Bean
    public JobDetail myJobDetail() {
        return JobBuilder.newJob(HiJob.class)
            //设置定时任务名字和分组，也可以不设置，这样则随机生成一个定时任务名字
            .withIdentity("myJob1", "myJobGroup1")
            //JobDataMap可以给任务execute传递参数，就是为Job设置一些属性
            .usingJobData("jobParam", "jobParam1")
            //是否持久化，默认为true：持久化
            .storeDurably()
            .build();
    }*/

    /**
     * 触发器：设置出发频率等。一般有两种触发器，一种是下面这种Simple Trigger，按固定频率触发定时任务。
     * 另一种是CronTrigger，这种触发器可以设置日历时间，在什么时间触发，什么时间截止
     */
    /*@Bean
    public Trigger myTrigger() {
        return TriggerBuilder.newTrigger()
            //一个触发器可以触发多个定时任务
            .forJob(myJobDetail())
            .withIdentity("myTrigger1", "myTriggerGroup1")
            .usingJobData("jobTriggerParam", "jobTriggerParam1")
            .startNow()
            //下面是两种配置定时任务执行频率的方式
            //.withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(5).repeatForever())
            .withSchedule(CronScheduleBuilder.cronSchedule("0/5 * * * * ?"))
            .build();
    }*/

    /**
     * 上面是固定定时任务的配置方法，下面是动态配置方式
     */
    @Autowired
    @Qualifier("defaultDatabaseSource")
    private DataSource dataSource;

    @Value("${spring.quartz.auto-startup}")
    private Boolean autoStartup;

    @Value("${spring.quartz.startup-delay}")
    private Integer delaySeconds;

    @Value("${spring.quartz.properties.org.quartz.scheduler.instanceName}")
    private String schedulerInstanceName;

    @Value("${spring.quartz.properties.org.quartz.scheduler.rmi.export}")
    private String schedulerRmiExport;

    @Value("${spring.quartz.properties.org.quartz.scheduler.rmi.proxy}")
    private String schedulerRmiProxy;

    @Value("${spring.quartz.properties.org.quartz.scheduler.wrapJobExecutionInUserTransaction}")
    private String schedulerWrapJobExecutionInUserTransaction;

    @Value("${spring.quartz.properties.org.quartz.threadPool.class}")
    private String threadPoolClass;

    @Value("${spring.quartz.properties.org.quartz.threadPool.threadCount}")
    private String threadPoolThreadCount;

    @Value("${spring.quartz.properties.org.quartz.threadPool.threadPriority}")
    private String threadPoolThreadPriority;

    @Value("${spring.quartz.properties.org.quartz.threadPool.threadsInheritContextClassLoaderOfInitializingThread}")
    private String threadPoolThreadsInheritContextClassLoaderOfInitializingThread;

    @Value("${spring.quartz.properties.org.quartz.jobStore.misfireThreshold}")
    private String jobStoreMisfireThreshold;

    @Value("${spring.quartz.properties.org.quartz.jobStore.driverDelegateClass}")
    private String jobStoreDriverDelegateClass;

    @Value("${spring.quartz.properties.org.quartz.jobStore.class}")
    private String jobStoreClass;

    @Value("${spring.quartz.properties.org.quartz.jobStore.tablePrefix}")
    private String jobStoreTablePrefix;

//    @Value("${spring.quartz.properties.org.quartz.jobStore.dataSource}")
//    private String jobStoreDataSource;
//    @Value("${spring.quartz.properties.org.quartz.dataSource.qzDS.maxConnections}")
//    private String dataSourceMaxConnections;
//    @Value("${spring.datasource.driver-class-name}")
//    private String dataSourceDriver;
//    @Value("${spring.datasource.url}")
//    private String dataSourceURL;
//    @Value("${spring.datasource.username}")
//    private String dataSourceUser;
//    @Value("${spring.datasource.password}")
//    private String dataSourcePassword;

    @Bean
    public Properties quartzProperties() throws IOException {
        Properties quartzProperties = new Properties();
        quartzProperties.setProperty("org.quartz.scheduler.instanceName", schedulerInstanceName);
        quartzProperties.setProperty("org.quartz.scheduler.rmi.export", schedulerRmiExport);
        quartzProperties.setProperty("org.quartz.scheduler.rmi.proxy", schedulerRmiProxy);
        quartzProperties.setProperty("org.quartz.scheduler.wrapJobExecutionInUserTransaction", schedulerWrapJobExecutionInUserTransaction);
        quartzProperties.setProperty("org.quartz.threadPool.class", threadPoolClass);
        quartzProperties.setProperty("org.quartz.threadPool.threadCount", threadPoolThreadCount);
        quartzProperties.setProperty("org.quartz.threadPool.threadPriority", threadPoolThreadPriority);
        quartzProperties.setProperty("org.quartz.threadPool.threadsInheritContextClassLoaderOfInitializingThread", threadPoolThreadsInheritContextClassLoaderOfInitializingThread);
        quartzProperties.setProperty("org.quartz.jobStore.misfireThreshold", jobStoreMisfireThreshold);
        quartzProperties.setProperty("org.quartz.jobStore.class", jobStoreClass);
        quartzProperties.setProperty("org.quartz.jobStore.driverDelegateClass", jobStoreDriverDelegateClass);
        quartzProperties.setProperty("org.quartz.jobStore.tablePrefix", jobStoreTablePrefix);
//        quartzProperties.setProperty("org.quartz.jobStore.dataSource", jobStoreDataSource);
//        quartzProperties.setProperty("org.quartz.dataSource.qzDS.maxConnections", dataSourceMaxConnections);
//        quartzProperties.setProperty("org.quartz.dataSource.qzDS.driver", dataSourceDriver);
//        quartzProperties.setProperty("org.quartz.dataSource.qzDS.URL", dataSourceURL);
//        quartzProperties.setProperty("org.quartz.dataSource.qzDS.user", dataSourceUser);
//        quartzProperties.setProperty("org.quartz.dataSource.qzDS.password", dataSourcePassword);

        return quartzProperties;
    }

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean() throws IOException {
        log.info("开始配置quartz定时任务");
        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        // 加载quartz数据源配置
        factory.setQuartzProperties(quartzProperties());
        // 加载数据源
        factory.setDataSource(dataSource);
        // 将spring上下文放入quartz中, 用于在job执行时获取注入bean
        factory.setApplicationContextSchedulerContextKey("applicationContextKey");

        factory.setOverwriteExistingJobs(true);

        if(autoStartup) {
            // 延时启动
            if(delaySeconds > 0) {
                factory.setStartupDelay(delaySeconds);
            }
            log.info("quartz开启自动启动定时任务, 延时{}s启动", delaySeconds);
        } else {
            factory.setAutoStartup(false);
            log.warn("quartz未开启自动启动定时任务");
        }

        log.info("quartz定时任务配置成功");
        return factory;
    }

}
