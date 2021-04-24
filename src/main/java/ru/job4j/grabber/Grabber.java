package ru.job4j.grabber;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import ru.job4j.html.SqlRuParse;
import ru.job4j.html.Parse;
import ru.job4j.model.Post;
import ru.job4j.store.Store;

import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;


public class Grabber implements Grab {
    private final Properties cfg = new Properties();

    private Store store() {
        return null;
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

        @Override
        public void execute(JobExecutionContext context) throws JobExecutionException {
            JobDataMap map = context.getJobDetail().getJobDataMap();
            Store store = (Store) map.get("store");
            Parse parse = (Parse) map.get("parse");
            /* TODO impl logic */
            //Получаем спиок всех вакансий
            List<Post> all = store.getAll();
            //Создаем список для вставки обновлений
            List<Post> lp = new LinkedList<>();
            //перебираем полученные
            for (Post i : all) {
                try {
                    List<Post> urlPost = parse.list("link");
                    //проверяем, что ссылки не пустые, если пустая, выходим
                    if (urlPost.isEmpty()) {
                        break;
                    }
                    //все полученные ссылки добавляем в спиок
                    lp.addAll(urlPost);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            // сохраняем поочереди
            lp.forEach(f -> f.setLink("link"));
            store.save(new Post(0));
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
