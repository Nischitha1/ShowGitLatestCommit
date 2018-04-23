package com.example.nisch.gitlatestcommit;

/**
 * Created by nisch on 4/22/2018.
 */

public class DataModel {

    String name;
    String commit_code;
    String commit_message;


    public DataModel(String name, String commit_code, String commit_message) {
        this.name=name;
        this.commit_code=commit_code;
        this.commit_message=commit_message;

    }

    public String getName() {
        return name;
    }

    public String getCommit_code() {
        return commit_code;
    }

    public String getCommit_message() {
        return commit_message;
    }



}