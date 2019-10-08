package com.dmk78.countmyjob.Data;

public class Job {
    private int id;
    private Employer employer;
    private int hour;
    private int money;
    private int completed = 0;
    private String desc;

    public Job(int id, Employer employer, int hour, int money, int completed, String desc) {
        this.id = id;
        this.employer = employer;
        this.hour = hour;
        this.money = money;
        this.completed = completed;
        this.desc = desc;
    }

    public Job(Employer employer, int hour, int money, int completed, String desc) {
        this.employer = employer;
        this.hour = hour;
        this.money = money;
        this.completed = completed;
        this.desc = desc;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Employer getEmployer() {
        return employer;
    }

    public void setEmployer(Employer employer) {
        this.employer = employer;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public int getCompleted() {
        return completed;
    }

    public void setCompleted(int completed) {
        this.completed = completed;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
