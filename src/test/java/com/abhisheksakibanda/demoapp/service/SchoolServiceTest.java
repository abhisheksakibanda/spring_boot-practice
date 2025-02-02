package com.abhisheksakibanda.demoapp.service;

import com.abhisheksakibanda.demoapp.exception.ConflictException;
import com.abhisheksakibanda.demoapp.exception.ResourceNotFoundException;
import com.abhisheksakibanda.demoapp.model.School;
import com.abhisheksakibanda.demoapp.model.Student;
import com.abhisheksakibanda.demoapp.repository.SchoolRepository;
import com.abhisheksakibanda.demoapp.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SchoolServiceTest {

    public static final String INSERT_SUCCESS_MESSAGE = "School added successfully";
    public static final String DELETE_SUCCESS_SUBSTRING = "deleted successfully";

    @Mock
    private SchoolRepository mockSchoolRepository;

    @Mock
    private StudentRepository mockStudentRepository;

    @InjectMocks
    private SchoolService schoolService;

    private School school, newSchool;

    @BeforeEach
    void setUp() {
        school = new School();
        school.setId(1L);
        school.setName("School1");
        school.setCity("City1");

        newSchool = new School();
        newSchool.setName("School2");
        newSchool.setCity("City2");
    }

    @Test
    void testGetSchools() {
        when(mockSchoolRepository.findAll()).thenReturn(List.of(school, newSchool));

        List<School> schools = schoolService.getSchools().getBody();
        assert schools != null;
        assertFalse(schools.isEmpty());
        assertEquals(2, schools.size());
        assertEquals(schools.getFirst().getId(), school.getId());
        assertEquals(schools.getFirst().getName(), school.getName());
        assertEquals(schools.getFirst().getCity(), school.getCity());
    }

    @Test
    void testGetSchoolsWhenEmpty() {
        when(mockSchoolRepository.findAll()).thenReturn(List.of());

        assertThrows(ResourceNotFoundException.class, () -> schoolService.getSchools());
    }

    @Test
    void testAddSchool() {
        when(mockSchoolRepository.existsByName(anyString())).thenReturn(false);

        ResponseEntity<String> response = schoolService.addSchool(school);

        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals(INSERT_SUCCESS_MESSAGE, response.getBody());
    }

    @Test
    void testAddSchoolWhenSchoolExists() {
        when(mockSchoolRepository.existsByName(anyString())).thenReturn(true);

        assertThrows(ConflictException.class, () -> schoolService.addSchool(school));
    }

    @Test
    void testAddStudentToSchool() {
        Student student = new Student("John", "test@uni.edu", LocalDate.now());
        student.setId(1L);

        when(mockSchoolRepository.findById(anyLong())).thenReturn(Optional.of(school));
        when(mockStudentRepository.findById(anyLong())).thenReturn(Optional.of(student));

        ResponseEntity<School> response = schoolService.addStudentToSchool(1L, 1L);

        assertTrue(Objects.requireNonNull(response.getBody()).getStudents().contains(student));
    }

    @Test
    void testAddStudentToSchoolWhenSchoolNotExists() {
        when(mockSchoolRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> schoolService.addStudentToSchool(1L, 1L));
    }

    @Test
    void testAddStudentToSchoolWhenStudentNotExists() {
        when(mockSchoolRepository.findById(anyLong())).thenReturn(Optional.of(school));
        when(mockStudentRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> schoolService.addStudentToSchool(1L, 1L));
    }

    @Test
    void testUpdateSchool() {
        when(mockSchoolRepository.findById(anyLong())).thenReturn(Optional.of(school));

        ResponseEntity<School> response = schoolService.updateSchool(1L, newSchool);

        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals(Objects.requireNonNull(response.getBody()).getName(), newSchool.getName());
    }

    @Test
    void testUpdateSchoolWhenNameBlank() {
        School blankSchool = new School();
        blankSchool.setName("  ");
        blankSchool.setCity("City1");

        when(mockSchoolRepository.findById(anyLong())).thenReturn(Optional.of(school));

        ResponseEntity<School> response = schoolService.updateSchool(1L, blankSchool);

        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertFalse(Objects.requireNonNull(response.getBody()).getName().isBlank());
    }

    @Test
    void testUpdateSchoolWhenNameNull() {
        School blankSchool = new School();
        blankSchool.setCity("Sample City");

        when(mockSchoolRepository.findById(anyLong())).thenReturn(Optional.of(school));

        ResponseEntity<School> response = schoolService.updateSchool(1L, blankSchool);

        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertNotNull(Objects.requireNonNull(response.getBody()).getName());
    }

    @Test
    void testUpdateSchoolWhenLastNameBlank() {
        School blankSchool = new School();
        blankSchool.setName("School1");
        blankSchool.setCity("  ");

        when(mockSchoolRepository.findById(anyLong())).thenReturn(Optional.of(school));

        ResponseEntity<School> response = schoolService.updateSchool(1L, blankSchool);

        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertFalse(Objects.requireNonNull(response.getBody()).getCity().isBlank());
    }

    @Test
    void testUpdateSchoolWhenLastNameNull() {
        School blankSchool = new School();
        blankSchool.setName("Sample School");

        when(mockSchoolRepository.findById(anyLong())).thenReturn(Optional.of(school));

        ResponseEntity<School> response = schoolService.updateSchool(1L, blankSchool);

        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertNotNull(Objects.requireNonNull(response.getBody()).getCity());
    }

    @Test
    void testUpdateSchoolWhenSchoolDoesNotExist() {
        when(mockSchoolRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> schoolService.updateSchool(1L, newSchool));
    }

    @Test
    void testDeleteSchool() {
        when(mockSchoolRepository.existsById(anyLong())).thenReturn(true);

        ResponseEntity<String> response = schoolService.deleteSchool(1L);
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertTrue(Objects.requireNonNull(response.getBody()).contains(DELETE_SUCCESS_SUBSTRING));
    }

    @Test
    void testDeleteSchoolWhenSchoolNotExists() {
        when(mockSchoolRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> schoolService.deleteSchool(1L));
    }

    @Test
    void testRemoveStudentFromSchool() {
        Student student = new Student("John", "test@email.edu", LocalDate.now());
        student.setId(1L);
        school.addStudent(student);

        when(mockSchoolRepository.findById(anyLong())).thenReturn(Optional.of(school));
        when(mockStudentRepository.findById(anyLong())).thenReturn(Optional.of(student));

        ResponseEntity<School> response = schoolService.removeStudentFromSchool(1L, 1L);
        assertFalse(Objects.requireNonNull(response.getBody()).getStudents().contains(student));
    }

    @Test
    void testRemoveStudentFromSchoolWhenSchoolNotExists() {
        when(mockSchoolRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> schoolService.removeStudentFromSchool(1L, 1L));
    }

    @Test
    void testRemoveStudentFromSchoolWhenStudentsNotExists() {
        when(mockSchoolRepository.findById(anyLong())).thenReturn(Optional.of(school));
        when(mockStudentRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> schoolService.removeStudentFromSchool(1L, 1L));
    }
}