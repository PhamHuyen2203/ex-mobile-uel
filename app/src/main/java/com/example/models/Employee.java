package com.example.models;

import java.io.Serializable;

public class Employee implements Serializable {
    private String id;
    private String name;
    private String phone;
    private String birthPlace;

    public void setId(String id) {
        this.id=id;
    }
    public String getId() {
        return this.id;
    }

    public void setName(String name) {
        this.name=name;
    }
    public String getName() {
        return this.name;
    }

    public void setPhone(String phone) {
        this.phone=phone;
    }
    public String getPhone() {
        return this.phone;
    }

    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }
    public String getBirthPlace() {
        return this.birthPlace;
    }

    public Employee() {
    }

    public Employee(String id, String name, String phone) {
        this.id = id;
        this.name = name;
        this.phone = phone;
    }

    public Employee(String id, String name, String phone, String birthPlace) {
        this(id,name,phone);
        this.birthPlace=birthPlace;
    }

    @Override
    public String toString() {
        return id + " - " + name + " - " + phone;
    }
}
