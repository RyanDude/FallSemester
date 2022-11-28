package com.example.demo;

import com.example.demo.Service.impl.FilesStorageService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.Resource;

@SpringBootApplication
public class SystemApplication {
	public static void main(String[] args) {
		SpringApplication.run(SystemApplication.class, args);
	}
}
