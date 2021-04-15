package ru.job4j.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Post {
    private int id;
    private String name;
    private String link;
    private String text;
    private LocalDateTime createData;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDateTime getCreateData() {
        return createData;
    }

    public void setCreateData(LocalDateTime createData) {
        this.createData = createData;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null|| getClass() != o.getClass()) {
            return false;
        }
        Post post = (Post) o;
        return id == post.id &&
                Objects.equals(name, post.name) &&
                Objects.equals(link, post.link) &&
                Objects.equals(text, post.text) &&
                Objects.equals(createData, post.createData);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, link, text, createData);
    }

    @Override
    public String toString() {
        return String.format("Post(%s, %s, %s, %s)", name, link, text, createData);
    }
}


