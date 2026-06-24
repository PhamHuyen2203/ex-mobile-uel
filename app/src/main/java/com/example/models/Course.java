package com.example.models;

import java.io.Serializable;

public class Course implements Serializable {
    private String code;
    private String name;
    private String credits;
    private String semester;

    public Course(String code, String name, String credits, String semester) {
        this.code = code;
        this.name = name;
        this.credits = credits;
        this.semester = semester;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getCredits() {
        return credits;
    }

    public String getSemester() {
        return semester;
    }
}
