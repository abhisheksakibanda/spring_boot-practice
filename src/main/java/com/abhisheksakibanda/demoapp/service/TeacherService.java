package com.abhisheksakibanda.demoapp.service;

import com.abhisheksakibanda.demoapp.exception.ConflictException;
import com.abhisheksakibanda.demoapp.exception.ResourceNotFoundException;
import com.abhisheksakibanda.demoapp.model.Student;
import com.abhisheksakibanda.demoapp.model.Teacher;
import com.abhisheksakibanda.demoapp.repository.StudentRepository;
import com.abhisheksakibanda.demoapp.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeacherService {

    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;

    @Autowired
    public TeacherService(TeacherRepository teacherRepository, StudentRepository studentRepository) {
        this.teacherRepository = teacherRepository;
        this.studentRepository = studentRepository;
    }

    public ResponseEntity<List<Teacher>> getTeachers() {
        List<Teacher> teachersList = teacherRepository.findAll();
        if (teachersList.isEmpty()) {
            throw new ResourceNotFoundException("No teachers found");
        } else {
            return ResponseEntity.ok(teachersList);
        }
    }

    public ResponseEntity<String> addNewTeacher(Teacher teacher) {
        if (teacherRepository.existsByFirstName(teacher.getFirstName())) {
            throw new ConflictException("Teacher already exists");
        }

        teacherRepository.save(teacher);
        return ResponseEntity.ok("Teacher added successfully");
    }

    public ResponseEntity<Teacher> addStudentToTeacher(Long teacherId, Long studentId) {
        Teacher teacher = teacherRepository.findById(teacherId).orElseThrow(() -> new ResourceNotFoundException("Teacher with ID " + teacherId + " does not exist"));
        Student student = studentRepository.findById(studentId).orElseThrow(() -> new ResourceNotFoundException("Student with name " + studentId + " does not exist"));

        teacher.addStudent(student);
        teacherRepository.save(teacher);

        return ResponseEntity.ok(teacher);
    }

    public ResponseEntity<Teacher> updateTeacher(Long teacherId, Teacher newTeacher) {
        Teacher foundTeacher = teacherRepository.findById(teacherId).orElseThrow(() -> new ResourceNotFoundException("Teacher with ID " + teacherId + " does not exist"));

        if (newTeacher.getFirstName() != null && !newTeacher.getFirstName().isBlank() && !foundTeacher.getFirstName().equals(newTeacher.getFirstName())) {
            foundTeacher.setFirstName(newTeacher.getFirstName());
        }

        if (newTeacher.getLastName() != null && !newTeacher.getLastName().isBlank() && !newTeacher.getLastName().equals(foundTeacher.getLastName())) {
            foundTeacher.setLastName(newTeacher.getLastName());
        }

        return ResponseEntity.ok(foundTeacher);
    }

    public ResponseEntity<String> deleteTeacher(Long teacherId) {
        if (!teacherRepository.existsById(teacherId)) {
            throw new ResourceNotFoundException("Teacher with id " + teacherId + " does not exist");
        }

        teacherRepository.deleteById(teacherId);
        return ResponseEntity.ok("Teacher deleted successfully");
    }

    public ResponseEntity<Teacher> removeStudentFromTeacher(Long teacherId, Long studentId) {
        Teacher teacher = teacherRepository.findById(teacherId).orElseThrow(() -> new ResourceNotFoundException("Teacher with ID " + teacherId + " does not exist"));
        Student student = studentRepository.findById(studentId).orElseThrow(() -> new ResourceNotFoundException("Student with name " + studentId + " does not exist"));

        teacher.removeStudent(student);
        teacherRepository.save(teacher);

        return ResponseEntity.ok(teacher);
    }
}
