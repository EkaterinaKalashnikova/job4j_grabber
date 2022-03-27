package ru.job4j.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class Post  {
    private int id;
    private String name; /*Message header*/
    private String link; /*msgBody*/
    private String text; /*msgBody*/
    private LocalDateTime createData;


    public Post(int id, String name, String link, String text, LocalDateTime createData) {
        this.id = id;
        this.name = name;
        this.link = link;
        this.text = text;
        this.createData = createData;
    }

    public Post() {

    }

    public Post(String vacancy, String link, String text, LocalDateTime date) {
        this.name = vacancy;
        this.link = link;
        this.text = text;
        this.createData = date;
    }

    public Post(int i) {

    }

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
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Post post = (Post) o;
        return id == post.id && Objects.equals(name, post.name) && Objects.equals(link, post.link) && Objects.equals(text, post.text) && Objects.equals(createData, post.createData);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, link, text, createData);
    }

    @Override
    public String toString() {
        return String.format("Post(%s,\n %s,\n \"|%-20s|\",\n %s)", name, link, text, createData);
    }
}


