package ru.job4j.quartz;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

import static org.quartz.JobBuilder.*;
import static org.quartz.TriggerBuilder.*;
import static org.quartz.SimpleScheduleBuilder.*;

public class AlertRabbit {


    public static void main(String[] args) {
        try {
            /*Создаем соединение с базой данных*/
            Connection cn = AlertRabbit.connection();
            /*Создаем конфигурацию*/
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();
            /*Передаем общий ресурс*/
            JobDataMap data = new JobDataMap();
            data.put("store", cn);
            /*Создаем задачу*/
            JobDetail job = newJob(Rabbit.class)
                    .usingJobData(data)
                    .build();
            /*Создание расписания*/
            SimpleScheduleBuilder times = simpleSchedule()
                    .withIntervalInSeconds(10)
                    .repeatForever();
            /*Запуск задачи*/
            Trigger trigger = newTrigger()
                    .startNow()
                    .withSchedule(times)
                    .build();
            /*Выполнение задачи*/
            scheduler.scheduleJob(job, trigger);
            Thread.sleep(10000);
            /*выключить планировщик*/
            scheduler.shutdown();
            System.out.println(cn);
        } catch (Exception se) {
            se.printStackTrace();
        }
    }

    public static class Rabbit implements Job {

        public Rabbit() {
            System.out.println(hashCode());
        }

        @Override
        public void execute(JobExecutionContext context) {
            System.out.println("Rabbit runs here ...");
            /* List<Long> store = (List<Long>) context.getJobDetail().getJobDataMap().get("store");*/
            Connection cn = (Connection) context.getJobDetail().getJobDataMap().get("store");
            /*cn.add(System.currentTimeMillis());*/
            try (PreparedStatement ps = cn.prepareStatement("insert into rabbit(created_data) values(?)")) {
                ps.setString(1, String.valueOf(System.currentTimeMillis()));
                ps.execute();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


        public static Connection connection() throws ClassNotFoundException, SQLException {
            Properties properties = new Properties();
            try (InputStream is = AlertRabbit.class.getClassLoader().getResourceAsStream("rabbit.properties")) {
                properties.load(is);
            } catch (IOException e) {
                e.printStackTrace();
            }

            Class.forName(properties.getProperty("jdbc.driver"));
            String url = properties.getProperty("jdbc.url");
            String login = properties.getProperty("jdbc.username");
            String password = properties.getProperty("jdbc.password");
            return DriverManager.getConnection(url, login, password);
        }
    }

