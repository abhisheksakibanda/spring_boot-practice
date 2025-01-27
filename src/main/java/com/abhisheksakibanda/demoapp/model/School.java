package com.abhisheksakibanda.demoapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.HashSet;
import java.util.Set;

@Entity
public class School {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is mandatory")
    private String name;

    @NotBlank(message = "City is mandatory")
    private String city;

    // This is the inverse side of the relationship.
    // The mappedBy attribute is used to specify the relationship field in the owning side to enable bidirectional navigation.
    @JsonIgnore
    @OneToMany(mappedBy = "school")
    private Set<Student> students = new HashSet<>();

    public School() {
    }

    public School(String name, String city) {
        this.name = name;
        this.city = city;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Set<Student> getStudents() {
        return students;
    }

    public void setStudents(Set<Student> students) {
        this.students = students;
    }

    public void addStudent(Student student) {
        if (students.add(student)) {
            student.setSchool(this);
        }
    }

    public void removeStudent(Student student) {
        if (students.remove(student)) {
            student.setSchool(null);
        }
    }

    @Override
    public String toString() {
        return "School{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", city='" + city + '\'' +
                '}';
    }
}
