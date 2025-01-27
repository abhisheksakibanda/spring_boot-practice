package com.abhisheksakibanda.demoapp.controller;

import com.abhisheksakibanda.demoapp.model.Teacher;
import com.abhisheksakibanda.demoapp.service.TeacherService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/teacher")
public class TeacherController {

    private final TeacherService teacherService;

    @Autowired
    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @GetMapping
    public ResponseEntity<List<Teacher>> getTeachers() {
        return teacherService.getTeachers();
    }

    @PostMapping
    public ResponseEntity<String> addTeacher(@Valid @RequestBody Teacher teacher) {
        return teacherService.addNewTeacher(teacher);
    }

    @PutMapping(path = "{teacherId}")
    public ResponseEntity<Teacher> updateTeacher(@PathVariable("teacherId") Long teacherId, @RequestBody Teacher newTeacher) {
        return teacherService.updateTeacher(teacherId, newTeacher);
    }

    @PutMapping(path = "/addStudent/{teacherId}", params = {"studentId"})
    public ResponseEntity<Teacher> addStudentToTeacher(@PathVariable Long teacherId, @RequestParam Long studentId) {
        return teacherService.addStudentToTeacher(teacherId, studentId);
    }

    @DeleteMapping(path = "{teacherId}")
    public ResponseEntity<String> deleteTeacher(@PathVariable("teacherId") Long teacherId) {
        return teacherService.deleteTeacher(teacherId);
    }

    @DeleteMapping(path = "/removeStudent/{teacherId}", params = {"studentId"})
    public ResponseEntity<Teacher> removeStudentFromTeacher(@PathVariable Long teacherId, @RequestParam Long studentId) {
        return teacherService.removeStudentFromTeacher(teacherId, studentId);
    }
}
