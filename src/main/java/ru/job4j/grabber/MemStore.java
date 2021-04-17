package ru.job4j.grabber;

import ru.job4j.model.Post;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MemStore implements Store, AutoCloseable {
    private Connection connection;

    public MemStore() throws Exception {
        initConnection();
    }

    public void initConnection() throws Exception {
        Class.forName("org.postgresql.Driver");
        String url = "jdbc:postgresql://localhost:5432/agregator";
        String login = "postgres";
        String password = "kindness";
        connection = DriverManager.getConnection(url, login, password);
    }


    @Override
    public void save(Post posts) {
        try (PreparedStatement statement =
                     connection.prepareStatement(
                             "insert into posts(name, link, text, createdata) values (?, ?, ?, ?)",
                             Statement.RETURN_GENERATED_KEYS
                     )) {
            statement.setString(1, posts.getName());
            statement.setString(2, posts.getLink());
            statement.setString(3, posts.getText());
            //statement.setString(4, posts.getCreateData().format(DateTimeFormatter.ofPattern("dd-MMM-yy")));
            statement.execute();
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    posts.setId(generatedKeys.getInt(1));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Post> getAll() {
        List<Post> posts = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement("select * from posts")) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    posts.add(new Post(resultSet.getInt("id"),
                            resultSet.getString("name"),
                            resultSet.getString("link"),
                            resultSet.getString("text")
                           // resultSet.getTimestamp("createData")
                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return posts;
    }

    @Override
    public Post findById(String id) {
        Post post = new Post();
        try (PreparedStatement statement = connection.prepareStatement("select * from posts where  id = ?")) {
            statement.setInt(1, Integer.parseInt(id));
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    post.setId(Integer.parseInt("id"));
                    post.setName("name");
                    post.setLink("link");
                    post.setText("text");
                    //post.setCreateData("createData");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return post;
    }

    @Override
    public void close() throws Exception {
        if (connection != null) {
            connection.close();
        }
    }
}
