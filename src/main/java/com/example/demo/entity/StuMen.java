package com.example.demo.entity;

import javax.persistence.*;
@Entity
@Table
public class StuMen {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = true)
    private long sid;
    @Column(nullable = true)
    private long mid;

    public long getId() {
        return id;
    }

    public long getMid() {
        return mid;
    }

    public long getSid() {
        return sid;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setMid(long mid) {
        this.mid = mid;
    }

    public void setSid(long sid) {
        this.sid = sid;
    }
}
