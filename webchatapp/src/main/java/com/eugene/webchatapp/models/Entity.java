package com.eugene.webchatapp.models;

import java.io.Serializable;

/**
 * Created by eugene on 26.05.16.
 */
public abstract class Entity implements Serializable {
    private String id;

    Entity(){

    }

    Entity(String id){
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
