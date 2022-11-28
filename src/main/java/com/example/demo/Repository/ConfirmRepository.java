package com.example.demo.Repository;

import com.example.demo.entity.Confirm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ConfirmRepository extends JpaRepository<Confirm, Long> {
    @Query(value = "select * from confirm", nativeQuery = true)
    List<Confirm> all();
    Confirm findByMidAndSid(long mid, long sid);
    @Query
            (value =
            "select s.id as sid,s.name as sname,m.id as mid,m.name as mname from confirm as c, student as s, mentor as m where c.sid = s.id and c.mid=m.id",nativeQuery = true)
    List<?> admin();
}
