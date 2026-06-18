package com.example.models;

public class ForecastItem {
    private String day;
    private String date;
    private int maxTemp;
    private int minTemp;
    private String status;

    public ForecastItem(String day, String date, int maxTemp, int minTemp, String status) {
        this.day = day;
        this.date = date;
        this.maxTemp = maxTemp;
        this.minTemp = minTemp;
        this.status = status;
    }

    public String getDay() {
        return day;
    }

    public String getDate() {
        return date;
    }

    public int getMaxTemp() {
        return maxTemp;
    }

    public int getMinTemp() {
        return minTemp;
    }

    public String getStatus() {
        return status;
    }
}
