package com.abhisheksakibanda.demoapp.controller;

import com.abhisheksakibanda.demoapp.model.Teacher;
import com.abhisheksakibanda.demoapp.service.TeacherService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TeacherControllerTest {

    @Mock
    private TeacherService mockTeacherService;

    @InjectMocks
    private TeacherController teacherController;

    @Test
    void testGetTeachers() {
        Teacher teacher = new Teacher("Dummy First Name", "Dummy Last Name");
        when(mockTeacherService.getTeachers()).thenReturn(ResponseEntity.ok(List.of(teacher)));

        List<Teacher> teachers = teacherController.getTeachers().getBody();

        assertNotNull(teachers);
        assertEquals("Dummy First Name", teachers.getFirst().getFirstName());
        assertEquals("Dummy Last Name", teachers.getFirst().getLastName());
    }

    @Test
    void testAddTeacher() {
        Teacher teacher = new Teacher("Dummy First Name", "Dummy Last Name");
        when(mockTeacherService.addNewTeacher(teacher)).thenReturn(ResponseEntity.ok("Teacher added successfully"));

        ResponseEntity<String> response = teacherController.addTeacher(teacher);

        assertNotNull(response);
        assertEquals("Teacher added successfully", response.getBody());
        assertTrue(response.getStatusCode().is2xxSuccessful());
    }

    @Test
    void testUpdateTeacher() {
        Teacher teacher = new Teacher("Updated First Name", "Updated Last Name");
        when(mockTeacherService.updateTeacher(1L, teacher)).thenReturn(ResponseEntity.ok(teacher));

        ResponseEntity<Teacher> response = teacherController.updateTeacher(1L, teacher);

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals("Updated First Name", response.getBody().getFirstName());
        assertEquals("Updated Last Name", response.getBody().getLastName());
        assertTrue(response.getStatusCode().is2xxSuccessful());
    }

    @Test
    void testAddStudentToTeacher() {
        Teacher teacher = new Teacher("Dummy First Name", "Dummy Last Name");
        when(mockTeacherService.addStudentToTeacher(1L, 1L)).thenReturn(ResponseEntity.ok(teacher));

        ResponseEntity<Teacher> response = teacherController.addStudentToTeacher(1L, 1L);

        assertNotNull(response);
        assertNotNull(response.getBody());
    }

    @Test
    void testDeleteTeacher() {
        when(mockTeacherService.deleteTeacher(1L)).thenReturn(ResponseEntity.ok("Teacher with id 1 deleted successfully"));

        ResponseEntity<String> response = teacherController.deleteTeacher(1L);

        assertNotNull(response);
        assertEquals("Teacher with id 1 deleted successfully", response.getBody());
        assertTrue(response.getStatusCode().is2xxSuccessful());
    }

    @Test
    void testRemoveStudentFromTeacher() {
        Teacher teacher = new Teacher("Dummy First Name", "Dummy Last Name");
        when(mockTeacherService.removeStudentFromTeacher(1L, 1L)).thenReturn(ResponseEntity.ok(teacher));

        ResponseEntity<Teacher> response = teacherController.removeStudentFromTeacher(1L, 1L);

        assertNotNull(response);
        assertNotNull(response.getBody());
    }
}