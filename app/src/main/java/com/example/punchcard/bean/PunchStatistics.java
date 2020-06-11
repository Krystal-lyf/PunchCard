package com.example.punchcard.bean;

public class PunchStatistics {
    private int id;
    private int projectId;
    private int year;
    private int month;
    private int day;

    public PunchStatistics(){

    }
    public PunchStatistics(int projectId,int year,int month,int day){
        this.projectId =projectId;
        this.year = year;
        this.month = month;
        this.day = day;
    }
    public void setId(int id) {
        this.id = id;
    }

    public void setProjectName(int projectId) {
        this.projectId = projectId;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getId() {
        return id;
    }

    public int getProjectName() {
        return projectId;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }
}