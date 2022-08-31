package com.example.demo.Repository;

import com.example.demo.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findByName(String name);
    @Query(value = "select a.id from account a where a.name=:name", nativeQuery = true)
    List<Long> findIdByName(@Param("name") String name);
}
