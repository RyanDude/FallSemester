package com.example.demo.Repository;

import com.example.demo.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findByAid(long id);
    Student findById(long id);
    @Query(value = "select * from student",nativeQuery = true)
    Page<Student> all(Pageable pageable);

    @Query(value = "select * from student where name like %:name%", nativeQuery = true)
    Page<Student> findByName(Pageable pageable, @Param("name")String name);


}
