package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
@Table
public class Mentor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String email;
    private String name;
    private String current_employer;
    private String title;
    private String gender;
    private String race;
    @Column(nullable = false)
    private long account_id;

    public long getAccount_id() {
        return account_id;
    }

    public void setAccount_id(long account_id) {
        this.account_id = account_id;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public String getEmail() {
        return email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public String getCurrent_employer() {
        return current_employer;
    }

    public String getRace() {
        return race;
    }

    public String getTitle() {
        return title;
    }

    public void setCurrent_employer(String current_employer) {
        this.current_employer = current_employer;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}