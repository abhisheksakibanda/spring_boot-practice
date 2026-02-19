package com.abhisheksakibanda.demoapp.controller;

import com.abhisheksakibanda.demoapp.model.School;
import com.abhisheksakibanda.demoapp.service.SchoolService;
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
class SchoolControllerTest {

    @Mock
    private SchoolService mockSchoolService;

    @InjectMocks
    private SchoolController schoolController;

    @Test
    void testGetSchools() {
        when(mockSchoolService.getSchools()).thenReturn(ResponseEntity.ok(List.of(new School("Dummy School", "Dummy City"))));

        List<School> schools = schoolController.getSchools().getBody();

        assertNotNull(schools);
        assertEquals("Dummy School", schools.getFirst().getName());
        assertEquals("Dummy City", schools.getFirst().getCity());
    }

    @Test
    void testAddSchool() {
        School school = new School("Dummy School", "Dummy City");
        when(mockSchoolService.addSchool(school)).thenReturn(ResponseEntity.ok("School added successfully"));

        ResponseEntity<String> response = schoolController.addSchool(school);

        assertNotNull(response);
        assertEquals("School added successfully", response.getBody());
        assertTrue(response.getStatusCode().is2xxSuccessful());
    }

    @Test
    void testUpdateSchool() {
        School school = new School("Updated School", "Updated City");
        when(mockSchoolService.updateSchool(1L, school)).thenReturn(ResponseEntity.ok(school));

        ResponseEntity<School> response = schoolController.updateSchool(1L, school);

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals("Updated School", response.getBody().getName());
        assertEquals("Updated City", response.getBody().getCity());
        assertTrue(response.getStatusCode().is2xxSuccessful());
    }

    @Test
    void testAddStudentToSchool() {
        School school = new School("School with Student", "City");
        when(mockSchoolService.addStudentToSchool(1L, 1L)).thenReturn(ResponseEntity.ok(school));

        ResponseEntity<School> response = schoolController.addStudentToSchool(1L, 1L);

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals("School with Student", response.getBody().getName());
        assertTrue(response.getStatusCode().is2xxSuccessful());
    }

    @Test
    void testDeleteSchool() {
        when(mockSchoolService.deleteSchool(1L)).thenReturn(ResponseEntity.ok("School deleted successfully"));

        ResponseEntity<String> response = schoolController.deleteSchool(1L);

        assertNotNull(response);
        assertEquals("School deleted successfully", response.getBody());
        assertTrue(response.getStatusCode().is2xxSuccessful());
    }

    @Test
    void testRemoveStudentFromSchool() {
        School school = new School("School with Student", "City");
        when(mockSchoolService.removeStudentFromSchool(1L, 1L)).thenReturn(ResponseEntity.ok(school));

        ResponseEntity<School> response = schoolController.removeStudentFromSchool(1L, 1L);

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals("School with Student", response.getBody().getName());
        assertTrue(response.getStatusCode().is2xxSuccessful());
    }
}