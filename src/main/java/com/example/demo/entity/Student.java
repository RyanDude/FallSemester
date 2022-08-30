package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
@Table
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String pid;
    private String email;
    private String gender;
    private String likedGender;
    private String likedPos;
    @Column(nullable = false)
    private long account_id;

    public long getAccount_id() {
        return account_id;
    }

    public void setAccount_id(long account_id) {
        this.account_id = account_id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public String getGender() {
        return gender;
    }

    public String getLikedGender() {
        return likedGender;
    }

    public String getLikedPos() {
        return likedPos;
    }

    public String getPid() {
        return pid;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setLikedGender(String likedGender) {
        this.likedGender = likedGender;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public void setLikedPos(String likedPos) {
        this.likedPos = likedPos;
    }

}
