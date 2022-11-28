package com.example.demo.Repository;

import com.example.demo.entity.FileInfo;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileInfoRepository extends JpaRepository<FileInfo, Long> {
    FileInfo findById(long id);
    List<FileInfo> findByName(String name);
    List<FileInfo> findByOwner(long owner);
    List<FileInfo> findByShared(long shared);
}
