package com.example.demo.entity;

import lombok.Data;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

/**
 * @Author: Jianjun Guo
 * @Date: Sep 1st
 * */

@Component
@Data
public class ResEntity<T> {
    private int StatusCode;
    private T inner;
    private String message;
    public ResEntity(){}
    public ResEntity(@Nullable T t, int status){
        this.inner = t;
        this.StatusCode = status;
    }
    public ResEntity(@Nullable T t, String message){
        this.inner = t;
        this.message = message;
    }
    public ResEntity(@Nullable T t, String message, int status){
        this.inner = t;
        this.message = message;
        this.StatusCode = status;
    }

    public ResEntity(int status, String message){
        this.message = message;
        this.StatusCode = status;
    }
}
