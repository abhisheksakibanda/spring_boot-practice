package com.abhisheksakibanda.demoapp.controller;

import com.abhisheksakibanda.demoapp.model.Tutor;
import com.abhisheksakibanda.demoapp.service.TutorService;
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
class TutorControllerTest {

    @Mock
    private TutorService mockTutorService;

    @InjectMocks
    private TutorController tutorController;

    @Test
    void testGetTutors() {
        when(mockTutorService.getTutors()).thenReturn(ResponseEntity.ok(List.of(new Tutor("Dummy First Name", "Dummy Last Name"))));

        List<Tutor> tutors = tutorController.getTutors().getBody();

        assertNotNull(tutors);
        assertEquals("Dummy First Name", tutors.getFirst().getFirstName());
        assertEquals("Dummy Last Name", tutors.getFirst().getLastName());
    }

    @Test
    void testRegisterNewTutor() {
        Tutor tutor = new Tutor("Dummy First Name", "Dummy Last Name");
        when(mockTutorService.addNewTutor(tutor)).thenReturn(ResponseEntity.ok("Tutor added successfully"));

        ResponseEntity<String> response = tutorController.registerNewTutor(tutor);

        assertNotNull(response);
        assertEquals("Tutor added successfully", response.getBody());
        assertTrue(response.getStatusCode().is2xxSuccessful());
    }

    @Test
    void testUpdateTutor() {
        Tutor tutor = new Tutor("Updated First Name", "Updated Last Name");
        when(mockTutorService.updateTutor(1L, tutor)).thenReturn(ResponseEntity.ok(tutor));

        ResponseEntity<Tutor> response = tutorController.updateTutor(1L, tutor);

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals("Updated First Name", response.getBody().getFirstName());
        assertEquals("Updated Last Name", response.getBody().getLastName());
        assertTrue(response.getStatusCode().is2xxSuccessful());
    }

    @Test
    void testDeleteTutor() {
        when(mockTutorService.deleteTutor(1L)).thenReturn(ResponseEntity.ok("Tutor with id 1 deleted successfully"));

        ResponseEntity<String> response = tutorController.deleteTutor(1L);

        assertNotNull(response);
        assertEquals("Tutor with id 1 deleted successfully", response.getBody());
        assertTrue(response.getStatusCode().is2xxSuccessful());
    }
}