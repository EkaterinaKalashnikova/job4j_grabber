package ru.job4j.grabber;

import org.junit.Test;
import ru.job4j.model.Post;
import ru.job4j.store.ConnectionRollback;
import ru.job4j.store.PsqlStore;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Arrays;
import java.util.Properties;

import static java.sql.Timestamp.valueOf;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class PsqlStoreTest {
    public Connection initPS() {
        try (InputStream in = PsqlStore.class.getClassLoader().getResourceAsStream("rabbit.properties")) {
            Properties config = new Properties();
            assert in != null;
            config.load(in);
            Class.forName(config.getProperty("jdbc.driver"));
            return DriverManager.getConnection(
                    config.getProperty("jdbc.url"),
                    config.getProperty("jdbc.username"),
                    config.getProperty("jdbc.password")
            );
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    @Test
    public void getAll() {
        try (PsqlStore ps = new PsqlStore(ConnectionRollback.create(this.initPS()))) {
            Post[] posts = new Post[2];
            Post[] rsl = ps.getAll().toArray(new Post[0]);
            for (int i = 0; i < posts.length; i++) {
                System.out.println(Arrays.toString(rsl));
                System.out.println();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}