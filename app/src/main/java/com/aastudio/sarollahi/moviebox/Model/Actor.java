package com.aastudio.sarollahi.moviebox.Model;

import java.io.Serializable;

public class Actor implements Serializable {
    private static final long id = 1L;


    private String actorId;
    private String name;
    private String poster;

    public Actor() {
    }

    public Actor(String actorId, String name, String poster) {
        this.actorId = actorId;
        this.name = name;
        this.poster = poster;
    }

    public String getActorId() {
        return actorId;
    }

    public void setActorId(String actorId) {
        this.actorId = actorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

}
