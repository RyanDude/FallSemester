package com.example.demo.Repository;

import com.example.demo.entity.Mentor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MentorRepository extends JpaRepository<Mentor, Long> {
    List<Mentor> findByAid(long id);
    @Query(value = "select * from mentor m where m.name like %:name%", nativeQuery = true)
    Page<Mentor> getAll(Pageable pageable, String name);
    @Query(value = "select * from mentor m where m.gender=:gender or m.title=:pos", nativeQuery = true)
    Page<Mentor> recommend(Pageable pageable, @Param("gender") String gender, @Param("pos") String pos);
    @Query(value = "select * from mentor m where m.title=:pos", nativeQuery = true)
    Page<Mentor> recommendBy(Pageable pageable, @Param("pos") String pos);
}
