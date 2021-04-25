package ru.job4j.store;

import ru.job4j.model.Post;
import ru.job4j.utils.SqlRuDateTimeParser;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class PsqlStore implements Store, AutoCloseable {
    //private static final Logger LOG =  LoggerFactory.getLogger(PsqlStore.class.getName());

    private Connection cnn;
    private Properties cfg;

    private PsqlStore(Properties cfg) throws SQLException {
        this.cfg = cfg;
        // this.cnn = ConnectionRollback.create(this.init());
        this.cnn = init();
    }

    public PsqlStore(Connection cnn, Properties cfg) {
        this.cnn = cnn;
        this.cfg = cfg;
    }

    public PsqlStore(Connection cnn) {
        this.cnn = cnn;
    }

    public PsqlStore() {

    }

    private Connection init() {
        try (InputStream in = PsqlStore.class.getClassLoader().getResourceAsStream("rabbit.properties")) {
            assert in != null;
            cfg.load(in);
            Class.forName(cfg.getProperty("jdbc.driver"));
            cnn = DriverManager.getConnection(
                    cfg.getProperty("jdbc.url"),
                    cfg.getProperty("jdbc.username"),
                    cfg.getProperty("jdbc.password"));

        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        return cnn;
    }

    @Override
    public void save(Post post) {
        //LOG.info("info message");
        try (PreparedStatement statement =
                     cnn.prepareStatement(
                             "insert into posts(name, link, text, createdata) values (?, ?, ?, ?)",
                             Statement.RETURN_GENERATED_KEYS
                     )) {
            statement.setString(1, post.getName());
            statement.setString(2, post.getLink());
            statement.setString(3, post.getText());
            statement.setTimestamp(4, Timestamp.valueOf(post.getCreateData()));
            statement.execute();
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    post.setId(generatedKeys.getInt(1));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Post> getAll() {
       // LOG.info("info message");
        List<Post> posts = new ArrayList<>();
        try (PreparedStatement statement = cnn.prepareStatement("select * from posts")) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Post post = new Post(resultSet.getInt("id"),
                            resultSet.getString("name"),
                            resultSet.getString("link"),
                            resultSet.getString("text"),
                            resultSet.getTimestamp("createdata").toLocalDateTime()
                    );
                    posts.add(post);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return posts;
    }

    @Override
    public Post findById(String id) {
      //  LOG.info("info message");
        Post post = new Post();
        try (PreparedStatement statement = cnn.prepareStatement("select * from posts where  id = ?")) {
            statement.setInt(1, Integer.parseInt(id));
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    // resultSet.getInt("id"); //название столбца
                    post.setId(resultSet.getInt("id"));
                    post.setName(resultSet.getString("name"));
                    post.setLink(resultSet.getString("link"));
                    post.setText(resultSet.getString("text"));
                    post.setCreateData(resultSet.getTimestamp("createdata").toLocalDateTime());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return post;
    }

    @Override
    public void close() throws Exception {
        if (cnn != null) {
            cnn.close();
        }
    }

    public static void main(String[] args) throws SQLException, IOException {
        Properties cfg = new Properties();
        PsqlStore psqlStore = new PsqlStore(cfg);
        SqlRuDateTimeParser sdt = new SqlRuDateTimeParser();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime ldt = sdt.parse("12 мар 20, 15:30");
        LocalDateTime ldt1 = sdt.parse("вчера, 15:30");
        Timestamp timestamp = Timestamp.valueOf(now);
        Timestamp timestamp1 = Timestamp.valueOf(ldt);
        Timestamp timestamp2 = Timestamp.valueOf(ldt1);
        Post post = new Post("job", "link", "text", timestamp.toLocalDateTime());
        Post post1 = new Post("job1", "link1", "text1", timestamp1.toLocalDateTime());
        Post post2 = new Post("job2", "link2", "text2", timestamp2.toLocalDateTime());
        psqlStore.save(post);
        psqlStore.save(post1);
        psqlStore.save(post2);
        psqlStore.getAll().forEach(System.out::println);
        Post byId = psqlStore.findById("0");
        System.out.println(byId);
    }
}
