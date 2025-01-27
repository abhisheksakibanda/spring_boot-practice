package com.abhisheksakibanda.demoapp.controller;

import com.abhisheksakibanda.demoapp.model.School;
import com.abhisheksakibanda.demoapp.service.SchoolService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/school")
public class SchoolController {

    private final SchoolService schoolService;

    @Autowired
    public SchoolController(SchoolService schoolService) {
        this.schoolService = schoolService;
    }

    @GetMapping
    public ResponseEntity<List<School>> getSchools() {
        return schoolService.getSchools();
    }

    @PostMapping
    public ResponseEntity<String> addSchool(@Valid @RequestBody School school) {
        return schoolService.addSchool(school);
    }

    @PutMapping(path = "{schoolId}")
    public ResponseEntity<School> updateSchool(@PathVariable("schoolId") Long schoolId, @RequestBody School school) {
        return schoolService.updateSchool(schoolId, school);
    }

    @PutMapping(path = "/addStudent/{schoolId}", params = {"studentId"})
    public ResponseEntity<School> addStudentToSchool(@PathVariable Long schoolId, @RequestParam Long studentId) {
        return schoolService.addStudentToSchool(schoolId, studentId);
    }

    @DeleteMapping(path = "{schoolId}")
    public ResponseEntity<String> deleteSchool(@PathVariable("schoolId") Long schoolId) {
        return schoolService.deleteSchool(schoolId);
    }

    @DeleteMapping(path = "/removeStudent/{schoolId}", params = {"studentId"})
    public ResponseEntity<School> removeStudentFromSchool(@PathVariable Long schoolId, @RequestParam Long studentId) {
        return schoolService.removeStudentFromSchool(schoolId, studentId);
    }
}
