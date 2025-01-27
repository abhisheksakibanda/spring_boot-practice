package com.abhisheksakibanda.demoapp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;

import java.time.LocalDate;
import java.time.Period;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "Name is mandatory")
    private String name;

    @Email
    private String email;

    @Past
    private LocalDate dob;

    @Transient
    private int age;

    @OneToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_student_tutor"))
    private Tutor tutor;

    @ManyToOne
    private School school;

    @ManyToMany
    @JoinTable(
            name = "student_teacher",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "teacher_id")
    )
    private Set<Teacher> teachers = new HashSet<>();

    public Student(String name, String email, LocalDate dob) {
        this.name = name;
        this.email = email;
        this.dob = dob;
    }

    public Student() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public int getAge() {
        return Period.between(this.dob, LocalDate.now()).getYears();
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Tutor getTutor() {
        return tutor;
    }

    public void setTutor(Tutor tutor) {
        this.tutor = tutor;
    }

    public School getSchool() {
        return school;
    }

    public Set<Teacher> getTeachers() {
        return teachers;
    }

    public void setTeachers(Set<Teacher> teachers) {
        this.teachers = teachers;
    }

    public void setSchool(School school) {
        this.school = school;
    }

    public void addSchool(School school) {
        if (school.getStudents().add(this)) {
            this.school = school;
        }
    }

    public void removeSchool(School school) {
        if (school.getStudents().remove(this)) {
            this.school = null;
        }
    }

    public void addTeacher(Teacher teacher) {
        if (this.teachers.add(teacher)) {
            teacher.getStudents().add(this);
        }
    }

    public void removeTeacher(Teacher teacher) {
        if (this.teachers.remove(teacher)) {
            teacher.getStudents().remove(this);
        }
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", dob=" + dob +
                ", age=" + age +
                ", tutor=" + tutor +
                ", school=" + school +
                ", teachers=" + teachers +
                '}';
    }
}
