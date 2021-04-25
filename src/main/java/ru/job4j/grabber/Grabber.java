package ru.job4j.grabber;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import ru.job4j.html.SqlRuParse;
import ru.job4j.html.Parse;
import ru.job4j.model.Post;
import ru.job4j.store.PsqlStore;
import ru.job4j.store.Store;

import java.io.*;
import java.sql.Connection;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;


public class Grabber implements Grab {
    private final Properties cfg = new Properties();

    private Store store() throws IOException {
        return new PsqlStore();
    }

    private Scheduler scheduler() throws SchedulerException {
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
        scheduler.start();
        return scheduler;
    }

    private void cfg() throws IOException {
        //  try (InputStream in = new FileInputStream(new File("app.properties"))) {
        try (InputStream in = Grabber.class.getClassLoader().getResourceAsStream("app.properties")) {
            cfg.load(in);
        }
    }

    @Override
    public void init(Parse parse, Store store, Scheduler scheduler) throws SchedulerException {
        JobDataMap data = new JobDataMap();
        data.put("store", store);
        data.put("parse", parse);
        JobDetail job = newJob(GrabJob.class)
                .usingJobData(data)
                .build();
        SimpleScheduleBuilder times = simpleSchedule()
                .withIntervalInSeconds(Integer.parseInt(cfg.getProperty("time")))
                .repeatForever();
        Trigger trigger = newTrigger()
                .startNow()
                .withSchedule(times)
                .build();
        scheduler.scheduleJob(job, trigger);
    }

    public static class GrabJob implements Job {

        private Timestamp timestamp;

        public GrabJob(Timestamp timestamp) {
            this.timestamp = timestamp;
        }

        @Override
        public void execute(JobExecutionContext context) throws JobExecutionException {
            JobDataMap map = context.getJobDetail().getJobDataMap();
            Store store = (Store) map.get("store");
            Parse parse = (Parse) map.get("parse");
            /* TODO impl logic */
            //Создаем список для вставки обновлений
            List<Post> lp = new LinkedList<>();
            //получаем ссылки
            try {
                List<Post> urlPost = parse.list(" https://www.sql.ru/forum/job");
                //все полученные ссылки добавляем в спиок
                lp.addAll(urlPost);
            } catch (IOException e) {
                e.printStackTrace();
            }
            // сохраняем поочереди
            for (Post p : lp) {
                p.setName("name");
                p.setLink("link");
                p.setText("text");
                p.setCreateData(timestamp.toLocalDateTime());
            }
            store.getAll();
            store.save(new Post());
        }
    }

    public static void main(String[] args) throws Exception {
        Grabber grab = new Grabber();
        grab.cfg();
        Scheduler scheduler = grab.scheduler();
        Store store = grab.store();
        grab.init(new SqlRuParse(), store, scheduler);
    }
}
