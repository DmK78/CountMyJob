package com.dmk78.countmyjob.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class WorkDay implements Comparable {
    private int id;
    private String day;
    private List<Job> jobs;
    private int hours;
    private int money;

    public WorkDay(int id, String day, List<Job> jobs, int hours, int money) {
        this.id = id;
        this.day = day;
        this.jobs = jobs;
        this.hours = hours;
        this.money = money;
    }

    public WorkDay(String day, List<Job> jobs, int hours, int money) {
        this.day = day;
        this.jobs = jobs;
        this.hours = hours;
        this.money = money;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public List<Job> getJobs() {
        return jobs;
    }

    public void setJobs(List<Job> jobs) {
        this.jobs = jobs;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public void recountJobs() {
        hours = 0;
        money = 0;
        for (Job job : jobs) {
            hours += job.getHour();
            money += job.getMoney();
        }
    }

    public WorkDay filterByEmployer(int eployerId) {
        List<Job> jobList = new ArrayList<>();
        for (Job job : jobs) {
            if (job.getEmployer().getId() == eployerId) {
                jobList.add(job);
            }
        }
        if (jobList.size() > 0) {
            setJobs(jobList);
            recountJobs();
        }
        return this;
    }

    public boolean isAllJobsDone(){
        boolean result = true;
        for(Job job:jobs){
            if(job.getCompleted()==0){
                result=false;
                break;
            }
        }
        return result;
    }

        @Override
        public boolean equals (Object o){
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            WorkDay workDay = (WorkDay) o;
            return id == workDay.id &&
                    hours == workDay.hours &&
                    money == workDay.money &&
                    Objects.equals(day, workDay.day) &&
                    Objects.equals(jobs, workDay.jobs);
        }

        @Override
        public int hashCode () {
            return Objects.hash(id, day, jobs, hours, money);
        }

        @Override
        public int compareTo (Object o){
            return 0;
        }
    }
