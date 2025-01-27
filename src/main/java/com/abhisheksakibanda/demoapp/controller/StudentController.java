package com.abhisheksakibanda.demoapp.controller;

import com.abhisheksakibanda.demoapp.model.Student;
import com.abhisheksakibanda.demoapp.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/student")
public class StudentController {

    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public ResponseEntity<List<Student>> getStudents() {
        return studentService.getStudents();
    }

    @PostMapping
    public ResponseEntity<String> registerNewStudent(@Valid @RequestBody Student student) {
        return studentService.addNewStudent(student);
    }

    @PutMapping(path = "{studentId}")
    public ResponseEntity<Student> updateStudent(@PathVariable("studentId") Long studentId, @RequestParam(required = false) String name, @RequestParam(required = false) String email, @RequestParam(required = false) String dob) {
        return studentService.updateStudent(studentId, name, email, dob);
    }

    @DeleteMapping(path = "{studentId}")
    public ResponseEntity<String> deleteStudent(@PathVariable("studentId") Long studentId) {
        return studentService.deleteStudent(studentId);
    }

    @PutMapping(path = "/addTutor/{studentId}", params = {"tutorName"})
    public ResponseEntity<Student> addTutorToStudent(@PathVariable Long studentId, @RequestParam Long tutorId) {
        return studentService.addTutorToStudent(studentId, tutorId);
    }

    @DeleteMapping(path = "/removeTutor/{studentId}")
    public ResponseEntity<Student> removeTutorFromStudent(@PathVariable Long studentId) {
        return studentService.removeTutorFromStudent(studentId);
    }

    @PutMapping(path = "/addSchool/{studentId}", params = {"schoolName"})
    public ResponseEntity<Student> addSchoolToStudent(@PathVariable Long studentId, @RequestParam Long schoolId) {
        return studentService.addSchoolToStudent(studentId, schoolId);
    }

    @DeleteMapping(path = "/removeSchool/{studentId}", params = {"schoolName"})
    public ResponseEntity<Student> removeSchoolFromStudent(@PathVariable Long studentId, @RequestParam Long schoolId) {
        return studentService.removeSchoolFromStudent(studentId, schoolId);
    }

    @PutMapping(path = "/addTeacher/{studentId}", params = {"teacherName"})
    public ResponseEntity<Student> addTeacherToStudent(@PathVariable Long studentId, @RequestParam Long teacherId) {
        return studentService.addTeacherToStudent(studentId, teacherId);
    }

    @DeleteMapping(path = "/removeTeacher/{studentId}", params = {"teacherName"})
    public ResponseEntity<Student> removeTeacherFromStudent(@PathVariable Long studentId, @RequestParam Long teacherId) {
        return studentService.removeTeacherFromStudent(studentId, teacherId);
    }
}
