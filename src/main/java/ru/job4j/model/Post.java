package ru.job4j.model;

import java.util.Objects;

public class Post {
    private int id;
    private String link;
    private String text;
    private String created_data;
    private int count;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getCreated_data() {
        return created_data;
    }

    public void setCreated_data(String created_data) {
        this.created_data = created_data;
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
                count == post.count &&
                Objects.equals(link, post.link) &&
                Objects.equals(text, post.text) &&
                Objects.equals(created_data, post.created_data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, link, text, created_data, count);
    }

    @Override
    public String toString() {
        return String.format("Post(%s, %s, %s, %d)", link, text, created_data, count);
    }
}


