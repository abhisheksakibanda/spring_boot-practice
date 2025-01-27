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
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final TutorRepository tutorRepository;
    private final SchoolRepository schoolRepository;
    private final TeacherRepository teacherRepository;

    @Autowired
    public StudentService(
            StudentRepository studentRepository,
            TutorRepository tutorRepository,
            SchoolRepository schoolRepository,
            TeacherRepository teacherRepository) {
        this.studentRepository = studentRepository;
        this.tutorRepository = tutorRepository;
        this.schoolRepository = schoolRepository;
        this.teacherRepository = teacherRepository;
    }

    public ResponseEntity<List<Student>> getStudents() {
        List<Student> students = studentRepository.findAll();

        if (students.isEmpty()) {
            throw new ResourceNotFoundException("No students found");
        }

        return ResponseEntity.ok(students);
    }

    public ResponseEntity<String> addNewStudent(Student student) {
        if (studentRepository.findStudentByEmail(student.getEmail()).isPresent()) {
            throw new ConflictException("Email already taken");
        }
        studentRepository.save(student);
        return ResponseEntity.ok("Student added successfully");
    }

    // One-to-one relationship
    public ResponseEntity<Student> addTutorToStudent(Long studentId, Long tutorId) {
        Student student = studentRepository.findById(studentId).orElseThrow(() -> new ResourceNotFoundException("Student with ID '" + studentId + "' does not exist"));
        Tutor tutor = tutorRepository.findById(tutorId).orElseThrow(() -> new ResourceNotFoundException("Tutor with ID " + tutorId + " does not exist"));

        student.setTutor(tutor);
        studentRepository.save(student);

        return ResponseEntity.ok(student);
    }

    // One-to-many relationship
    public ResponseEntity<Student> addSchoolToStudent(Long studentId, Long schoolId) {
        School foundSchool = schoolRepository.findById(schoolId).orElseThrow(() -> new ResourceNotFoundException("School with ID " + schoolId + " does not exist"));
        Student foundStudent = studentRepository.findById(studentId).orElseThrow(() -> new ResourceNotFoundException("Student with ID " + studentId + " does not exist"));

        foundStudent.addSchool(foundSchool);
        studentRepository.save(foundStudent);

        return ResponseEntity.ok(foundStudent);
    }

    // Many-to-many relationship
    public ResponseEntity<Student> addTeacherToStudent(Long studentId, Long teacherId) {
        Student student = studentRepository.findById(studentId).orElseThrow(() -> new ResourceNotFoundException("Student with ID '" + studentId + "' does not exist"));
        Teacher teacher = teacherRepository.findById(teacherId).orElseThrow(() -> new ResourceNotFoundException("Teacher with ID " + teacherId + " does not exist"));

        student.addTeacher(teacher);
        studentRepository.save(student);

        return ResponseEntity.ok(student);
    }

    @Transactional
    public ResponseEntity<Student> updateStudent(Long studentId, String name, String email, String dob) {
        Student foundStudent = studentRepository.findById(studentId).orElseThrow(
                () -> new ResourceNotFoundException("Student with id " + studentId + " does not exist")
        );

        // Update the student's name if a new name is provided
        // Do not use repository method to update (save()) the student's name when in a transaction, as the entity is already managed
        if (name != null && !name.isBlank() && !name.equals(foundStudent.getName())) {
            foundStudent.setName(name);
        }

        if (email != null && !email.isBlank() && !email.equals(foundStudent.getEmail())) {
            if (studentRepository.findStudentByEmail(email).isPresent()) {
                throw new ConflictException("Email already taken");
            }
            foundStudent.setEmail(email);
        }

        if (dob != null && !dob.isBlank() && !dob.equals(foundStudent.getDob().toString())) {
            foundStudent.setDob(LocalDate.parse(dob));
        }
        return ResponseEntity.ok(foundStudent);
    }

    public ResponseEntity<String> deleteStudent(Long studentId) {
        if (!studentRepository.existsById(studentId)) {
            throw new ResourceNotFoundException("Student with id " + studentId + " does not exist");
        }
        studentRepository.deleteById(studentId);
        return ResponseEntity.ok("Student with id " + studentId + " deleted successfully");
    }

    // One-to-one relationship
    public ResponseEntity<Student> removeTutorFromStudent(Long studentId) {
        Student student = studentRepository.findById(studentId).orElseThrow(() -> new ResourceNotFoundException("Student with ID '" + studentId + "' does not exist"));

        student.setTutor(null);
        studentRepository.save(student);

        return ResponseEntity.ok(student);
    }

    // One-to-many relationship
    public ResponseEntity<Student> removeSchoolFromStudent(Long studentId, Long schoolId) {
        School school = schoolRepository.findById(schoolId).orElseThrow(() -> new ResourceNotFoundException("School with ID " + schoolId + " does not exist"));
        Student foundStudent = studentRepository.findById(studentId).orElseThrow(() -> new ResourceNotFoundException("Student with ID " + studentId + " does not exist"));

        if (foundStudent.getSchool() != null && foundStudent.getSchool().getName().equals(school.getName())) {
            foundStudent.removeSchool(school);

            studentRepository.save(foundStudent);
            return ResponseEntity.ok(foundStudent);
        } else {
            throw new ResourceNotFoundException("Student is not enrolled in " + school.getName());
        }
    }

    // Many-to-many relationship
    public ResponseEntity<Student> removeTeacherFromStudent(Long studentId, Long teacherId) {
        Student student = studentRepository.findById(studentId).orElseThrow(() -> new ResourceNotFoundException("Student with ID '" + studentId + "' does not exist"));
        Teacher teacher = teacherRepository.findById(teacherId).orElseThrow(() -> new ResourceNotFoundException("Teacher with ID " + teacherId + " does not exist"));

        student.removeTeacher(teacher);
        studentRepository.save(student);

        return ResponseEntity.ok(student);
    }
}
