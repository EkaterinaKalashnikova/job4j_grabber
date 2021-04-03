package ru.job4j.quartz;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

import static org.quartz.JobBuilder.*;
import static org.quartz.TriggerBuilder.*;
import static org.quartz.SimpleScheduleBuilder.*;

public class AlertRabbit {

    public static void main(String[] args) {
        try {
            // Время периода запуска в расписание
            read();
            // Конфигурирование
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();
            // Создание задачи
            JobDetail job = newJob(Rabbit.class).withIdentity("emailJob").build();
            // Создание расписания
            SimpleScheduleBuilder times = simpleSchedule()
                    .withIntervalInSeconds(10)
                    .repeatForever();
            // Запуск задачи
            Trigger trigger = newTrigger()
                    .startNow()
                    .withSchedule(times)
                    .build();
            //Загрузка задачи и триггера в планировщик
            scheduler.scheduleJob(job, trigger);
        } catch (SchedulerException se) {
            se.printStackTrace();
            System.out.println("SchedulerException" + se.getMessage());
        }
    }

    private static Properties read() {
        FileInputStream fis;
        //создаем объект Properties
        Properties property = new Properties();

        try {
            //загружаем в него данные из файла
            fis = new FileInputStream("src/main/resources/rabbit.properties");
            //загружаем свойства из файла, представленного объектом InputStream
            property.load(fis);
            //получаем значения свойств из объекта Properties
            String interval = property.getProperty("rabbit.interval");
            System.out.println("Interval: " + interval);

        } catch (IOException e) {
            System.err.println("ОШИБКА: Файл свойств отсуствует!");
        }
        return property;
    }

    public static class Rabbit implements Job {

        @Override
        public void execute(JobExecutionContext context) {
            Date downloadTime = new Date();
            System.out.println("Rabbit runs here ..." + downloadTime);
        }
    }
}
