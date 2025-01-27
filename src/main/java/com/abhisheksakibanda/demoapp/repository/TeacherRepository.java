package com.abhisheksakibanda.demoapp.repository;

import com.abhisheksakibanda.demoapp.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    boolean existsByFirstName(String firstName);
}
