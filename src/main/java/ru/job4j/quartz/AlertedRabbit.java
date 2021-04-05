package ru.job4j.quartz;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;

import static org.quartz.JobBuilder.*;
import static org.quartz.TriggerBuilder.*;
import static org.quartz.SimpleScheduleBuilder.*;

public class AlertedRabbit {

    public static void main(String[] args) {
        try {
            // Время периода запуска в расписание
            Properties properties = read();
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
        //
        Properties properties = new Properties();
        //
        try (InputStream input = AlertedRabbit.class.getClassLoader().getResourceAsStream("rabbit1.properties")) {
            if (input == null) {
                System.out.println("ОШИБКА: Файл свойств отсуствует!" + properties);
            }
            assert input != null;
            properties.load(input);
            // String interval = properties.getProperty("rabbit.interval");
            SimpleScheduleBuilder interval = simpleSchedule().withRepeatCount(1)
                    .withIntervalInSeconds(Integer.parseInt(properties.getProperty("rabbit.interval")));

            //System.out.println("Interval: " + interval);
            properties.forEach((key, value) -> System.out.println("Key : " + key + ", Value : " + value));

       /*  //создаем объект Properties
        Properties properties = new Properties();
        // реализуем свой собственный загрузчик классов
        ClassLoader loader = AlertedRabbit.class.getClassLoader();
        try (InputStream io = loader.getResourceAsStream("rabbit.properties")) {
            //загружаем свойства из файла, представленного объектом InputStream
            properties.load(io);
            //получаем значения свойств из объекта Properties
            String interval = properties.getProperty("rabbit.interval");
            System.out.println("Interval: " + interval);*/

        } catch (IOException e) {
           // System.err.println("ОШИБКА: Файл свойств отсуствует!");
        }
        return properties;
    }

    public static class Rabbit implements Job {

        @Override
        public void execute(JobExecutionContext context) {
            Date downloadTime = new Date();
            System.out.println("Rabbit runs here ..." + downloadTime);
        }
    }
}
