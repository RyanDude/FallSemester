package com.example.demo.Repository;

import com.example.demo.entity.StuMen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StuMenRepository extends JpaRepository<StuMen, Long> {
    List<StuMen> findBySid(long sid);
    List<StuMen> findByMid(long mid);
    @Query(value = "select * from stu_men where sid=:sid and mid=:mid", nativeQuery = true)
    StuMen find(@Param("sid")long sid, @Param("mid")long mid);
}
