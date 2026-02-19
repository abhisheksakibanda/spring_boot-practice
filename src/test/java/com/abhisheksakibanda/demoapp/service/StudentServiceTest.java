package com.abhisheksakibanda.demoapp.service;

import com.abhisheksakibanda.demoapp.exception.ConflictException;
import com.abhisheksakibanda.demoapp.exception.ResourceNotFoundException;
import com.abhisheksakibanda.demoapp.model.School;
import com.abhisheksakibanda.demoapp.model.Student;
import com.abhisheksakibanda.demoapp.model.Teacher;
import com.abhisheksakibanda.demoapp.model.Tutor;
import com.abhisheksakibanda.demoapp.repository.SchoolRepository;
import com.abhisheksakibanda.demoapp.repository.StudentRepository;
import com.abhisheksakibanda.demoapp.repository.TeacherRepository;
import com.abhisheksakibanda.demoapp.repository.TutorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    public static final String INSERT_SUCCESS_MESSAGE = "Student added successfully";
    public static final String DELETE_SUCCESS_SUBSTRING = "deleted successfully";

    @Mock
    private StudentRepository mockStudentRepository;

    @Mock
    private TutorRepository mockTutorRepository;

    @Mock
    private SchoolRepository mockSchoolRepository;

    @Mock
    private TeacherRepository mockTeacherRepository;

    @InjectMocks
    private StudentService studentService;

    private Student student, newStudent;

    @BeforeEach
    void setUp() {
        student = new Student();
        student.setId(1L);
        student.setName("John");
        student.setEmail("test@email.edu");
        student.setDob(LocalDate.now());

        newStudent = new Student();
        newStudent.setId(2L);
        newStudent.setName("Jane");
        newStudent.setEmail("sample@email.edu");
        newStudent.setDob(LocalDate.now());
    }

    @Test
    void testGetStudents() {
        when(mockStudentRepository.findAll()).thenReturn(List.of(student, newStudent));

        List<Student> students = studentService.getStudents().getBody();
        assert students != null;
        assertFalse(students.isEmpty());
        assertEquals(2, students.size());
        assertEquals(students.getFirst().getId(), student.getId());
        assertEquals(students.getFirst().getName(), student.getName());
        assertEquals(students.getFirst().getEmail(), student.getEmail());
        assertEquals(students.getFirst().getDob(), student.getDob());
    }

    @Test
    void testGetStudentsWhenEmpty() {
        when(mockStudentRepository.findAll()).thenReturn(List.of());

        assertThrows(ResourceNotFoundException.class, () -> studentService.getStudents());
    }

    @Test
    void testAddNewStudent() {
        when(mockStudentRepository.findStudentByEmail(anyString())).thenReturn(Optional.empty());

        ResponseEntity<String> response = studentService.addNewStudent(student);

        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals(INSERT_SUCCESS_MESSAGE, response.getBody());
    }

    @Test
    void testAddNewStudentWhenStudentExists() {
        when(mockStudentRepository.findStudentByEmail(anyString())).thenReturn(Optional.of(student));

        assertThrows(ConflictException.class, () -> studentService.addNewStudent(student));
    }

    @Test
    void testAddTutorToStudent() {
        Tutor tutor = new Tutor("Sample", "Tutor");
        tutor.setId(1L);

        when(mockStudentRepository.findById(anyLong())).thenReturn(Optional.of(student));
        when(mockTutorRepository.findById(anyLong())).thenReturn(Optional.of(tutor));

        ResponseEntity<Student> response = studentService.addTutorToStudent(1L, 1L);

        assertEquals(Objects.requireNonNull(response.getBody()).getTutor(), tutor);
    }

    @Test
    void testAddTutorToStudentWhenStudentNotExists() {
        when(mockStudentRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> studentService.addTutorToStudent(1L, 1L));
    }

    @Test
    void testAddTutorToStudentWhenTutorNotExists() {
        when(mockStudentRepository.findById(anyLong())).thenReturn(Optional.of(student));
        when(mockTutorRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> studentService.addTutorToStudent(1L, 1L));
    }

    @Test
    void testAddSchoolToStudent() {
        School school = new School("Sample School", "Sample City");
        school.setId(1L);

        when(mockStudentRepository.findById(anyLong())).thenReturn(Optional.of(student));
        when(mockSchoolRepository.findById(anyLong())).thenReturn(Optional.of(school));

        ResponseEntity<Student> response = studentService.addSchoolToStudent(1L, 1L);

        assertEquals(Objects.requireNonNull(response.getBody()).getSchool(), school);
    }

    @Test
    void testAddSchoolToStudentWhenSchoolNotExists() {
        when(mockSchoolRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> studentService.addSchoolToStudent(1L, 1L));
    }

    @Test
    void testAddSchoolToStudentWhenStudentNotExists() {
        School school = new School("Sample School", "Sample City");
        school.setId(1L);

        when(mockSchoolRepository.findById(anyLong())).thenReturn(Optional.of(school));
        when(mockStudentRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> studentService.addSchoolToStudent(1L, 1L));
    }

    @Test
    void testAddTeacherToStudent() {
        Teacher teacher = new Teacher("Sample", "Teacher");
        teacher.setId(1L);

        when(mockStudentRepository.findById(anyLong())).thenReturn(Optional.of(student));
        when(mockTeacherRepository.findById(anyLong())).thenReturn(Optional.of(teacher));

        ResponseEntity<Student> response = studentService.addTeacherToStudent(1L, 1L);

        assertTrue(Objects.requireNonNull(response.getBody()).getTeachers().contains(teacher));
    }

    @Test
    void testAddTeacherToStudentWhenStudentNotExists() {
        assertThrows(ResourceNotFoundException.class, () -> studentService.addSchoolToStudent(1L, 1L));
    }

    @Test
    void testUpdateStudent() {
    }

    @Test
    void testDeleteStudent() {
        when(mockStudentRepository.existsById(anyLong())).thenReturn(true);

        ResponseEntity<String> response = studentService.deleteStudent(1L);
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertTrue(Objects.requireNonNull(response.getBody()).contains(DELETE_SUCCESS_SUBSTRING));
    }

    @Test
    void testDeleteStudentWhenStudentNotExists() {
        when(mockStudentRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> studentService.deleteStudent(1L));
    }

    @Test
    void testRemoveTutorFromStudent() {
        Tutor tutor = new Tutor("Sample", "Tutor");
        tutor.setId(1L);
        student.setTutor(tutor);

        when(mockStudentRepository.findById(anyLong())).thenReturn(Optional.of(student));

        ResponseEntity<Student> response = studentService.removeTutorFromStudent(1L);

        assertNull(Objects.requireNonNull(response.getBody()).getTutor());
    }

    @Test
    void testRemoveTutorFromStudentWhenStudentNotExists() {
        assertThrows(ResourceNotFoundException.class, () -> studentService.removeTutorFromStudent(1L));
    }

    @Test
    void testRemoveSchoolFromStudent() {
        School school = new School("Sample School", "Sample City");
        school.setId(1L);
        student.setSchool(school);

        Set<Student> students = new HashSet<>();
        students.add(student);
        school.setStudents(students);

        when(mockSchoolRepository.findById(anyLong())).thenReturn(Optional.of(school));
        when(mockStudentRepository.findById(anyLong())).thenReturn(Optional.of(student));

        ResponseEntity<Student> response = studentService.removeSchoolFromStudent(1L, 1L);

        assertNull(Objects.requireNonNull(response.getBody()).getSchool());
    }

    @Test
    void testRemoveSchoolFromStudentWhenStudentNotEnrolled() {
        assertThrows(ResourceNotFoundException.class, () -> studentService.removeSchoolFromStudent(1L, 1L));
    }

    @Test
    void testRemoveSchoolFromStudentWhenStudentEnrolledInDifferentSchool() {
        School school = new School("Sample School", "Sample City");
        school.setId(1L);
        student.setSchool(new School("Different School", "Different City"));

        Set<Student> students = new HashSet<>();
        students.add(student);
        school.setStudents(students);

        when(mockSchoolRepository.findById(anyLong())).thenReturn(Optional.of(school));
        when(mockStudentRepository.findById(anyLong())).thenReturn(Optional.of(student));

        assertThrows(ResourceNotFoundException.class, () -> studentService.removeSchoolFromStudent(1L, 1L));
    }

    @Test
    void testRemoveTeacherFromStudent() {
        Teacher teacher = new Teacher("Sample", "Teacher");
        teacher.setId(1L);
        student.addTeacher(teacher);

        when(mockStudentRepository.findById(anyLong())).thenReturn(Optional.of(student));
        when(mockTeacherRepository.findById(anyLong())).thenReturn(Optional.of(teacher));

        ResponseEntity<Student> response = studentService.removeTeacherFromStudent(1L, 1L);

        assertFalse(Objects.requireNonNull(response.getBody()).getTeachers().contains(teacher));
    }

    @Test
    void testRemoveTeacherFromStudentWhenStudentNotExists() {
        assertThrows(ResourceNotFoundException.class, () -> studentService.removeTeacherFromStudent(1L, 1L));
    }
}