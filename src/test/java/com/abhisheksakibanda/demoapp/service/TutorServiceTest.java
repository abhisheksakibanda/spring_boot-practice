package com.abhisheksakibanda.demoapp.service;

import com.abhisheksakibanda.demoapp.exception.ConflictException;
import com.abhisheksakibanda.demoapp.exception.ResourceNotFoundException;
import com.abhisheksakibanda.demoapp.model.Tutor;
import com.abhisheksakibanda.demoapp.repository.TutorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TutorServiceTest {

    @Mock
    private TutorRepository mockTutorRepository;

    @InjectMocks
    private TutorService tutorService;

    private Tutor tutor, newTutor;

    public static final String INSERT_SUCCESS_MESSAGE = "Tutor added successfully";

    @BeforeEach
    void setUp() {
        tutor = new Tutor();
        tutor.setId(1L);
        tutor.setFirstName("John");
        tutor.setLastName("Doe");

        newTutor = new Tutor();
        newTutor.setFirstName("Jane");
        newTutor.setLastName("Dane");
    }

    @Test
    void testGetTutors() {
        when(mockTutorRepository.findAll()).thenReturn(List.of(tutor));

        List<Tutor> tutors = tutorService.getTutors().getBody();

        assert tutors != null;
        assertFalse(tutors.isEmpty());
        assertEquals(tutors.getFirst().getId(), tutor.getId());
        assertEquals(tutors.getFirst().getFirstName(), tutor.getFirstName());
        assertEquals(tutors.getFirst().getLastName(), tutor.getLastName());
    }

    @Test
    void testGetTutorsWhenEmpty() {
        when(mockTutorRepository.findAll()).thenReturn(new ArrayList<>());

        assertThrows(ResourceNotFoundException.class, () -> tutorService.getTutors());
    }

    @Test
    void testAddNewTutor() {
        when(mockTutorRepository.existsByFirstName(anyString())).thenReturn(false);

        ResponseEntity<String> response = tutorService.addNewTutor(tutor);

        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals(INSERT_SUCCESS_MESSAGE, response.getBody());
    }

    @Test
    void testAddNewTutorWhenTutorExists() {
        when(mockTutorRepository.existsByFirstName(anyString())).thenReturn(true);

        assertThrows(ConflictException.class, () -> tutorService.addNewTutor(tutor));
    }

    @Test
    void testUpdateTutor() {
        when(mockTutorRepository.findById(anyLong())).thenReturn(Optional.of(tutor));

        ResponseEntity<Tutor> response = tutorService.updateTutor(1L, newTutor);

        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals(Objects.requireNonNull(response.getBody()).getFirstName(), newTutor.getFirstName());
    }

    @Test
    void testUpdateTutorWhenFirstNameBlank() {
        Tutor blankTutor = new Tutor();
        blankTutor.setFirstName("  ");
        blankTutor.setLastName("Doe");

        when(mockTutorRepository.findById(anyLong())).thenReturn(Optional.of(tutor));

        ResponseEntity<Tutor> response = tutorService.updateTutor(1L, blankTutor);

        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertFalse(Objects.requireNonNull(response.getBody()).getFirstName().isBlank());
    }

    @Test
    void testUpdateTutorWhenFirstNameNull() {
        Tutor blankTutor = new Tutor();
        blankTutor.setLastName("Doe");

        when(mockTutorRepository.findById(anyLong())).thenReturn(Optional.of(tutor));

        ResponseEntity<Tutor> response = tutorService.updateTutor(1L, blankTutor);

        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertNotNull(Objects.requireNonNull(response.getBody()).getFirstName());
    }

    @Test
    void testUpdateTutorWhenLastNameBlank() {
        Tutor blankTutor = new Tutor();
        blankTutor.setFirstName("John");
        blankTutor.setLastName("  ");

        when(mockTutorRepository.findById(anyLong())).thenReturn(Optional.of(tutor));

        ResponseEntity<Tutor> response = tutorService.updateTutor(1L, blankTutor);

        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertFalse(Objects.requireNonNull(response.getBody()).getLastName().isBlank());
    }

    @Test
    void testUpdateTutorWhenLastNameNull() {
        Tutor blankTutor = new Tutor();
        blankTutor.setFirstName("John");

        when(mockTutorRepository.findById(anyLong())).thenReturn(Optional.of(tutor));

        ResponseEntity<Tutor> response = tutorService.updateTutor(1L, blankTutor);

        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertNotNull(Objects.requireNonNull(response.getBody()).getLastName());
    }

    @Test
    void testUpdateTutorWhenTutorDoesNotExist() {
        when(mockTutorRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> tutorService.updateTutor(1L, newTutor));
    }

    @Test
    void testDeleteTutor() {
        when(mockTutorRepository.existsById(anyLong())).thenReturn(true);

        ResponseEntity<String> response = tutorService.deleteTutor(1L);

        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertTrue(Objects.requireNonNull(response.getBody()).contains("deleted successfully"));
    }

    @Test
    void testDeleteTutorWhenTutorExists() {
        when(mockTutorRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> tutorService.deleteTutor(1L));
    }
}