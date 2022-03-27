package ru.job4j.grabber;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import ru.job4j.html.SqlRuParse;
import ru.job4j.html.Parse;
import ru.job4j.model.Post;
import ru.job4j.store.PsqlStore;
import ru.job4j.store.Store;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
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

    private Store store() {
        return new PsqlStore(cfg);
       /*return null;*/
    }

    private Scheduler scheduler() throws SchedulerException {
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
        scheduler.start();
        return scheduler;
    }

    private void cfg() throws IOException {
        /*try (InputStream in = new FileInputStream(new File("app.properties"))) {*/
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

    public void web(Store store) {
        new Thread(() -> {
            try (ServerSocket server = new ServerSocket(Integer.parseInt(cfg.getProperty("port")))) {
                while (!server.isClosed()) {
                    Socket socket = server.accept();
                    try (OutputStream out = socket.getOutputStream()) {
                        out.write("HTTP/1.1 200 OK\r\n\r\n".getBytes());
                        List<Post> posts = store.getAll();
                        for (Post post : posts) {
                            out.write(post.toString().getBytes("windows-1251"));
                            out.write(System.lineSeparator().getBytes());
                            out.flush();
                        }
                    } catch (IOException io) {
                        io.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static class GrabJob implements Job {
       /* private Timestamp timestamp;

        public GrabJob(Timestamp timestamp) {
            this.timestamp = timestamp;
        }*/

        @Override
        public void execute(JobExecutionContext context) throws JobExecutionException {
            JobDataMap map = context.getJobDetail().getJobDataMap();
            Store store = (Store) map.get("store");
            Parse parse = (Parse) map.get("parse");
            /* TODO impl logic */
           /* Создаем список для вставки обновлений*/
            try {
                List<Post> posts = parse.list("https://www.sql.ru/forum/job-offers");
                /*все полученные ссылки добавляем в спиок
                и сохраняем поочереди*/
                for (Post post : posts) {
                    store.save(post);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public static void main(String[] args) throws Exception {
            Grabber grab = new Grabber();
            grab.cfg();
            Scheduler scheduler = grab.scheduler();
            Store store = grab.store();
            grab.init(new SqlRuParse(), store, scheduler);
            grab.web(store);
        }
    }
}
