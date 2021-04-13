package ru.job4j.model;

import java.util.Objects;

public class Post {
    private int id;
    private String link;
    private String text;
    private String createData;
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
        return createData;
    }

    public void setCreateData(String createData) {
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
                count == post.count &&
                Objects.equals(link, post.link) &&
                Objects.equals(text, post.text) &&
                Objects.equals(createData, post.createData);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, link, text, createData, count);
    }

    @Override
    public String toString() {
        return String.format("Post(%s, %s, %s, %d)", link, text, createData, count);
    }
}


