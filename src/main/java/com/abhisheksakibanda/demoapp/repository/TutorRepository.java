package com.abhisheksakibanda.demoapp.repository;

import com.abhisheksakibanda.demoapp.model.Tutor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TutorRepository extends JpaRepository<Tutor, Long> {
    boolean existsByFirstName(String firstName);
}
