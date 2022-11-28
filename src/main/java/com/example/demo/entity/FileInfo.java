package com.example.demo.entity;

import javax.persistence.*;

@Entity
@Table
public class FileInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long owner;
    private String name;
    private long shared;
    public FileInfo(){}

    public FileInfo(String name, long owner) {
        this.name = name;
        this.owner = owner;
    }

    public long getOwner() {
        return owner;
    }

    public long getShared() {
        return shared;
    }

    public void setOwner(long owner) {
        this.owner = owner;
    }

    public void setShared(long shared) {
        this.shared = shared;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
