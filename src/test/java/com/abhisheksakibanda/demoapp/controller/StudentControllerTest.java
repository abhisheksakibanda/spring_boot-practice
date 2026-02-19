package com.abhisheksakibanda.demoapp.controller;

import com.abhisheksakibanda.demoapp.model.Student;
import com.abhisheksakibanda.demoapp.service.StudentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentControllerTest {

    @Mock
    private StudentService mockStudentService;

    @InjectMocks
    private StudentController studentController;

    @Test
    void testGetStudents() {
        Student student = new Student("Dummy Student", "sample@email.com", LocalDate.now());
        when(mockStudentService.getStudents()).thenReturn(ResponseEntity.ok(List.of(student)));

        List<Student> students = studentController.getStudents().getBody();

        assertNotNull(students);
        assertEquals("Dummy Student", students.getFirst().getName());
        assertEquals("sample@email.com", students.getFirst().getEmail());
    }

    @Test
    void testRegisterNewStudent() {
        Student student = new Student("Dummy Student", "sample@email.com", LocalDate.now());
        when(mockStudentService.addNewStudent(any())).thenReturn(ResponseEntity.ok("Student added successfully"));

        ResponseEntity<String> response = studentController.registerNewStudent(student);

        assertNotNull(response);
        assertEquals("Student added successfully", response.getBody());
        assertTrue(response.getStatusCode().is2xxSuccessful());
    }

    @Test
    void testUpdateStudent() {
        Student student = new Student("Updated Name", "update@email.com", LocalDate.now());
        when(mockStudentService.updateStudent(anyLong(), anyString(), anyString(), anyString())).thenReturn(ResponseEntity.ok(student));

        ResponseEntity<Student> response = studentController.updateStudent(1L, "Updated Name", "update@email.com", LocalDate.now().toString());

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals("Updated Name", response.getBody().getName());
        assertEquals("update@email.com", response.getBody().getEmail());
        assertTrue(response.getStatusCode().is2xxSuccessful());
    }

    @Test
    void testDeleteStudent() {
        when(mockStudentService.deleteStudent(anyLong())).thenReturn(ResponseEntity.ok("Student deleted successfully"));

        ResponseEntity<String> response = studentController.deleteStudent(1L);

        assertNotNull(response);
        assertEquals("Student deleted successfully", response.getBody());
        assertTrue(response.getStatusCode().is2xxSuccessful());
    }

    @Test
    void testTutorToStudent() {
        Student student = new Student("Dummy Student", "sample@email.com", LocalDate.now());
        when(mockStudentService.addTutorToStudent(anyLong(), anyLong())).thenReturn(ResponseEntity.ok(student));

        ResponseEntity<Student> response = studentController.addTutorToStudent(1L, 1L);

        assertNotNull(response);
        assertNotNull(response.getBody());
    }

    @Test
    void testRemoveTutorFromStudent() {
        Student student = new Student("Dummy Student", "sample@email.com", LocalDate.now());
        when(mockStudentService.removeTutorFromStudent(anyLong())).thenReturn(ResponseEntity.ok(student));

        ResponseEntity<Student> response = studentController.removeTutorFromStudent(1L);

        assertNotNull(response);
        assertNotNull(response.getBody());
    }

    @Test
    void testAddTeacherToStudent() {
        Student student = new Student("Dummy Student", "sample@email.com", LocalDate.now());
        when(mockStudentService.addTeacherToStudent(anyLong(), anyLong())).thenReturn(ResponseEntity.ok(student));

        ResponseEntity<Student> response = studentController.addTeacherToStudent(1L, 1L);

        assertNotNull(response);
        assertNotNull(response.getBody());
    }

    @Test
    void testAddSchoolToStudent() {
        Student student = new Student("Dummy Student", "sample@email.com", LocalDate.now());
        when(mockStudentService.addSchoolToStudent(anyLong(), anyLong())).thenReturn(ResponseEntity.ok(student));

        ResponseEntity<Student> response = studentController.addSchoolToStudent(1L, 1L);

        assertNotNull(response);
        assertNotNull(response.getBody());
    }

    @Test
    void testRemoveTeacherFromStudent() {
        Student student = new Student("Dummy Student", "sample@email.com", LocalDate.now());
        when(mockStudentService.removeTeacherFromStudent(anyLong(), anyLong())).thenReturn(ResponseEntity.ok(student));

        ResponseEntity<Student> response = studentController.removeTeacherFromStudent(1L, 1L);

        assertNotNull(response);
        assertNotNull(response.getBody());
    }

    @Test
    void testRemoveSchoolFromStudent() {
        Student student = new Student("Dummy Student", "sample@email.com", LocalDate.now());
        when(mockStudentService.removeSchoolFromStudent(anyLong(), anyLong())).thenReturn(ResponseEntity.ok(student));

        ResponseEntity<Student> response = studentController.removeSchoolFromStudent(1L, 1L);

        assertNotNull(response);
        assertNotNull(response.getBody());
    }
}