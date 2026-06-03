package com.example.models;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Order implements Serializable {
    private String orderID;
    private String cusID;
    private String empID;
    private Date orderDate;
    private OrderStatus orderStatus;
    private SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");

    public Order() {
    }

    public Order(String orderID, String cusID, String empID, Date orderDate) {
        this.orderID = orderID;
        this.cusID = cusID;
        this.empID = empID;
        this.orderDate = orderDate;
    }

    public Order(String orderID, String cusID, String empID, Date orderDate, OrderStatus orderStatus, SimpleDateFormat sdf) {
        this(orderID,cusID,empID,orderDate);
        this.orderStatus = orderStatus;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getCusID() {
        return cusID;
    }

    public void setCusID(String cusID) {
        this.cusID = cusID;
    }

    public String getEmpID() {
        return empID;
    }

    public void setEmpID(String empID) {
        this.empID = empID;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    @Override
    public String toString() {
        return orderID + " [" + orderStatus + "] - " + sdf.format(orderDate) + " - Total: " + DataWarehouse.sumOfMoneyForOrder(this);
    }
}
