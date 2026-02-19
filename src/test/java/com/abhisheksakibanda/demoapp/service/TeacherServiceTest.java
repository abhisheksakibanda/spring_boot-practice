package com.abhisheksakibanda.demoapp.service;

import com.abhisheksakibanda.demoapp.exception.ConflictException;
import com.abhisheksakibanda.demoapp.exception.ResourceNotFoundException;
import com.abhisheksakibanda.demoapp.model.Student;
import com.abhisheksakibanda.demoapp.model.Teacher;
import com.abhisheksakibanda.demoapp.repository.StudentRepository;
import com.abhisheksakibanda.demoapp.repository.TeacherRepository;
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
class TeacherServiceTest {

    public static final String INSERT_SUCCESS_MESSAGE = "Teacher added successfully";
    public static final String DELETE_SUCCESS_SUBSTRING = "deleted successfully";

    @Mock
    private TeacherRepository mockTeacherRepository;

    @Mock
    private StudentRepository mockStudentRepository;

    @InjectMocks
    private TeacherService teacherService;

    private Teacher teacher, newTeacher;


    @BeforeEach
    void setUp() {
        teacher = new Teacher();
        teacher.setId(1L);
        teacher.setFirstName("John");
        teacher.setLastName("Doe");

        newTeacher = new Teacher();
        newTeacher.setFirstName("Jane");
        newTeacher.setLastName("Doe");
    }

    @Test
    void testGetTeachers() {
        when(mockTeacherRepository.findAll()).thenReturn(List.of(teacher, newTeacher));

        List<Teacher> teachers = teacherService.getTeachers().getBody();
        assert teachers != null;
        assertFalse(teachers.isEmpty());
        assertEquals(2, teachers.size());
        assertEquals(teachers.getFirst().getId(), teacher.getId());
        assertEquals(teachers.getFirst().getFirstName(), teacher.getFirstName());
        assertEquals(teachers.getFirst().getLastName(), teacher.getLastName());
    }

    @Test
    void testGetTeachersWhenEmpty() {
        when(mockTeacherRepository.findAll()).thenReturn(List.of());

        assertThrows(ResourceNotFoundException.class, () -> teacherService.getTeachers());
    }

    @Test
    void testAddNewTeacher() {
        when(mockTeacherRepository.existsByFirstName(anyString())).thenReturn(false);

        ResponseEntity<String> response = teacherService.addNewTeacher(teacher);

        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals(INSERT_SUCCESS_MESSAGE, response.getBody());
    }

    @Test
    void testAddNewTeacherWhenTeacherExists() {
        when(mockTeacherRepository.existsByFirstName(anyString())).thenReturn(true);

        assertThrows(ConflictException.class, () -> teacherService.addNewTeacher(teacher));
    }

    @Test
    void testAddStudentToTeacher() {
        Student student = new Student("John", "test@uni.edu", LocalDate.now());
        student.setId(1L);

        when(mockTeacherRepository.findById(anyLong())).thenReturn(Optional.of(teacher));
        when(mockStudentRepository.findById(anyLong())).thenReturn(Optional.of(student));

        ResponseEntity<Teacher> response = teacherService.addStudentToTeacher(1L, 1L);

        assertTrue(Objects.requireNonNull(response.getBody()).getStudents().contains(student));
    }

    @Test
    void testAddStudentToTeacherWhenTeacherNotExists() {
        when(mockTeacherRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> teacherService.addStudentToTeacher(1L, 1L));
    }

    @Test
    void testAddStudentToTeacherWhenStudentNotExists() {
        when(mockTeacherRepository.findById(anyLong())).thenReturn(Optional.of(teacher));
        when(mockStudentRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> teacherService.addStudentToTeacher(1L, 1L));
    }

    @Test
    void testUpdateTeacher() {
        when(mockTeacherRepository.findById(anyLong())).thenReturn(Optional.of(teacher));

        ResponseEntity<Teacher> response = teacherService.updateTeacher(1L, newTeacher);

        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals(Objects.requireNonNull(response.getBody()).getFirstName(), newTeacher.getFirstName());
    }

    @Test
    void testUpdateTeacherWhenFirstNameBlank() {
        Teacher blankTeacher = new Teacher();
        blankTeacher.setFirstName("  ");
        blankTeacher.setLastName("Doe");

        when(mockTeacherRepository.findById(anyLong())).thenReturn(Optional.of(teacher));

        ResponseEntity<Teacher> response = teacherService.updateTeacher(1L, blankTeacher);

        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertFalse(Objects.requireNonNull(response.getBody()).getFirstName().isBlank());
    }

    @Test
    void testUpdateTeacherWhenNameNull() {
        Teacher blankTeacher = new Teacher();
        blankTeacher.setLastName("Doey");

        when(mockTeacherRepository.findById(anyLong())).thenReturn(Optional.of(teacher));

        ResponseEntity<Teacher> response = teacherService.updateTeacher(1L, blankTeacher);

        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertNotNull(Objects.requireNonNull(response.getBody()).getFirstName());
    }

    @Test
    void testUpdateTeacherWhenLastNameBlank() {
        Teacher blankTeacher = new Teacher();
        blankTeacher.setFirstName("Teacher1");
        blankTeacher.setLastName("  ");

        when(mockTeacherRepository.findById(anyLong())).thenReturn(Optional.of(teacher));

        ResponseEntity<Teacher> response = teacherService.updateTeacher(1L, blankTeacher);

        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertFalse(Objects.requireNonNull(response.getBody()).getLastName().isBlank());
    }

    @Test
    void testUpdateTeacherWhenLastNameNull() {
        Teacher blankTeacher = new Teacher();
        blankTeacher.setFirstName("John");

        when(mockTeacherRepository.findById(anyLong())).thenReturn(Optional.of(teacher));

        ResponseEntity<Teacher> response = teacherService.updateTeacher(1L, blankTeacher);

        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertNotNull(Objects.requireNonNull(response.getBody()).getLastName());
    }

    @Test
    void testUpdateTeacherWhenTeacherDoesNotExist() {
        when(mockTeacherRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> teacherService.updateTeacher(1L, newTeacher));
    }

    @Test
    void testDeleteTeacher() {
        when(mockTeacherRepository.existsById(anyLong())).thenReturn(true);

        ResponseEntity<String> response = teacherService.deleteTeacher(1L);
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertTrue(Objects.requireNonNull(response.getBody()).contains(DELETE_SUCCESS_SUBSTRING));
    }

    @Test
    void testDeleteTeacherWhenTeacherNotExists() {
        when(mockTeacherRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> teacherService.deleteTeacher(1L));
    }

    @Test
    void testRemoveStudentFromTeacher() {
        Student student = new Student("John", "test@email.edu", LocalDate.now());
        student.setId(1L);
        teacher.addStudent(student);

        when(mockTeacherRepository.findById(anyLong())).thenReturn(Optional.of(teacher));
        when(mockStudentRepository.findById(anyLong())).thenReturn(Optional.of(student));

        ResponseEntity<Teacher> response = teacherService.removeStudentFromTeacher(1L, 1L);
        assertFalse(Objects.requireNonNull(response.getBody()).getStudents().contains(student));
    }

    @Test
    void testRemoveStudentFromTeacherWhenTeacherNotExists() {
        when(mockTeacherRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> teacherService.removeStudentFromTeacher(1L, 1L));
    }

    @Test
    void testRemoveStudentFromTeacherWhenStudentsNotExists() {
        when(mockTeacherRepository.findById(anyLong())).thenReturn(Optional.of(teacher));
        when(mockStudentRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> teacherService.removeStudentFromTeacher(1L, 1L));
    }
}