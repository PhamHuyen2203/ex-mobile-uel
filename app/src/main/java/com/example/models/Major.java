package com.example.models;

import androidx.annotation.NonNull;

public class Major {
    private String id;
    private String name;

    public Major(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }
}
