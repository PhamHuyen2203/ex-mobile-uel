package com.example.models;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;

public class Department implements Serializable {
    private String departmentId;
    private String departmentName;
    private ArrayList<Employee> listOfEmployee;

    public void addEmployee(Employee emp) {
        this.listOfEmployee.add(emp);
    }

    public void addListEmployee(ArrayList<Employee> list) {
        this.listOfEmployee.addAll(list);
    }

    public ArrayList<Employee> getListOfEmployee() {
        return listOfEmployee;
    }

    public Department() {
        this.listOfEmployee=new ArrayList<>();
    }

    public Department(String departmentId, String departmentName) {
        this();
        this.departmentId = departmentId;
        this.departmentName = departmentName;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    @Override
    public String toString() {
        return this.departmentName;
    }
}
