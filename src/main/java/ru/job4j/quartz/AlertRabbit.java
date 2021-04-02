package ru.job4j.quartz;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.util.Date;

import static org.quartz.JobBuilder.*;
import static org.quartz.TriggerBuilder.*;
import static org.quartz.SimpleScheduleBuilder.*;

public class AlertRabbit {
   // Date downloadTime;

    public static void main(String[] args) {
        try {
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();
            JobDetail job = newJob(Rabbit.class).withIdentity("emailJob").build();
            SimpleScheduleBuilder times = simpleSchedule()
                    .withIntervalInSeconds(10)
                    .repeatForever();
            Trigger trigger = newTrigger()
                    .startNow()
                    .withSchedule(times)
                    .build();
            scheduler.scheduleJob(job, trigger);
        } catch (SchedulerException se) {
            se.printStackTrace();
            System.out.println("SchedulerException" + se.getMessage());
        }
    }

    public static class Rabbit implements Job {
        private Date downloadTime;

        @Override
        public void execute(JobExecutionContext context) throws JobExecutionException {
            downloadTime = new Date();
            System.out.println("Rabbit runs here ..." + downloadTime);
        }

        public Date getDownloadTime() {
            return downloadTime;
        }
    }
}
