package com.abhisheksakibanda.demoapp.service;

import com.abhisheksakibanda.demoapp.exception.ConflictException;
import com.abhisheksakibanda.demoapp.exception.ResourceNotFoundException;
import com.abhisheksakibanda.demoapp.model.School;
import com.abhisheksakibanda.demoapp.model.Student;
import com.abhisheksakibanda.demoapp.repository.SchoolRepository;
import com.abhisheksakibanda.demoapp.repository.StudentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SchoolService {

    private final SchoolRepository schoolRepository;
    private final StudentRepository studentRepository;

    public SchoolService(SchoolRepository schoolRepository, StudentRepository studentRepository) {
        this.schoolRepository = schoolRepository;
        this.studentRepository = studentRepository;
    }

    public ResponseEntity<List<School>> getSchools() {
        List<School> schools = schoolRepository.findAll();

        if (schools.isEmpty()) {
            throw new ResourceNotFoundException("No schools found");
        }
        return ResponseEntity.ok(schools);
    }

    public ResponseEntity<String> addSchool(School school) {
        if (schoolRepository.existsByName(school.getName())) {
            throw new ConflictException("School with name " + school.getName() + " already exists");
        }
        schoolRepository.save(school);
        return ResponseEntity.status(HttpStatus.CREATED).body("School added successfully");
    }

    public ResponseEntity<School> addStudentToSchool(Long schoolId, Long studentId) {
        School foundSchool = schoolRepository.findById(schoolId).orElseThrow(() -> new ResourceNotFoundException("School with ID " + schoolId + " does not exist"));
        Student foundStudent = studentRepository.findById(studentId).orElseThrow(() -> new ResourceNotFoundException("Student with ID " + studentId + " does not exist"));

        foundSchool.addStudent(foundStudent);
        schoolRepository.save(foundSchool);

        return ResponseEntity.ok(foundSchool);
    }

    public ResponseEntity<School> updateSchool(Long schoolId, School school) {
        School foundSchool = schoolRepository.findById(schoolId).orElseThrow(() -> new ResourceNotFoundException("School with ID " + schoolId + " does not exist"));

        if (school.getName() != null && !school.getName().isBlank() && !school.getName().equalsIgnoreCase(foundSchool.getName())) {
            foundSchool.setName(school.getName());
        }

        if (school.getCity() != null && !school.getCity().isBlank() && !school.getCity().equalsIgnoreCase(foundSchool.getCity())) {
            foundSchool.setCity(school.getCity());
        }
        schoolRepository.save(foundSchool);
        return ResponseEntity.ok(foundSchool);
    }

    public ResponseEntity<String> deleteSchool(Long schoolId) {
        if (!schoolRepository.existsById(schoolId)) {
            throw new ResourceNotFoundException("School with id " + schoolId + " does not exist");
        }

        schoolRepository.deleteById(schoolId);
        return ResponseEntity.ok("School with id " + schoolId + " deleted successfully");
    }

    public ResponseEntity<School> removeStudentFromSchool(Long schoolId, Long studentId) {
        School foundSchool = schoolRepository.findById(schoolId).orElseThrow(() -> new ResourceNotFoundException("School with ID " + schoolId + " does not exist"));
        Student foundStudent = studentRepository.findById(studentId).orElseThrow(() -> new ResourceNotFoundException("Student with ID " + studentId + " does not exist"));

        foundSchool.removeStudent(foundStudent);
        schoolRepository.save(foundSchool);

        return ResponseEntity.ok(foundSchool);
    }
}
