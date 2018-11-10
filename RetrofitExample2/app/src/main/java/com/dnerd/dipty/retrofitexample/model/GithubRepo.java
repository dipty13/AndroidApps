package com.dnerd.dipty.retrofitexample.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class GithubRepo extends RealmObject {
    @PrimaryKey
    private  String name;
    public String getName() {
        return name;
    }

    public  void setName(String name) {

        this.name = name;
    }

    public GithubRepo() {

    }
}
