package com.dmk78.countmyjob.Data;

import java.util.Objects;

public class Employer {
    private int id;
    private String name;
    private String desc;

    public Employer(int id, String name, String desc) {
        this.id = id;
        this.name = name;
        this.desc = desc;
    }

    public Employer(String name, String desc) {
        this.name = name;
        this.desc = desc;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employer employer = (Employer) o;
        return id == employer.id &&
                Objects.equals(name, employer.name) &&
                Objects.equals(desc, employer.desc);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, desc);
    }
}