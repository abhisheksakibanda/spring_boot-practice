package com.abhisheksakibanda.demoapp.repository;

import com.abhisheksakibanda.demoapp.model.School;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SchoolRepository extends JpaRepository<School, Long> {
    boolean existsByName(String name);
}
