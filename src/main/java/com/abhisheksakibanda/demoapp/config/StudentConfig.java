package com.abhisheksakibanda.demoapp.config;

import com.abhisheksakibanda.demoapp.model.School;
import com.abhisheksakibanda.demoapp.model.Student;
import com.abhisheksakibanda.demoapp.model.Teacher;
import com.abhisheksakibanda.demoapp.model.Tutor;
import com.abhisheksakibanda.demoapp.repository.SchoolRepository;
import com.abhisheksakibanda.demoapp.repository.StudentRepository;
import com.abhisheksakibanda.demoapp.repository.TeacherRepository;
import com.abhisheksakibanda.demoapp.repository.TutorRepository;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

@Configuration
public class StudentConfig {

    @Bean
    OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new io.swagger.v3.oas.models.info.Info()
                        .title("Student API")
                        .version("1.0")
                        .description("Documentation Student API v1.0"));
    }

    @Bean
    CommandLineRunner commandLineRunner(
            StudentRepository studentRepository,
            TutorRepository tutorRepository,
            SchoolRepository schoolRepository,
            TeacherRepository teacherRepository
    ) {
        return args -> {
            Student rahul = new Student(
                    "Rahul",
                    "rahul.singh@gmail.com",
                    LocalDate.of(2000, Month.NOVEMBER, 15)
            );
            Student anita = new Student(
                    "Anita",
                    "anita.keshav@gmail.com",
                    LocalDate.of(1995, Month.JUNE, 27)
            );

            Tutor john = new Tutor("John", "Doe");
            Tutor jane = new Tutor("Jane", "Doe");

            School universitySchool = new School("Test University", "Denver");

            Teacher charles = new Teacher("Charles", "Dickens");
            Teacher thomas = new Teacher("Thomas", "Edison");

            studentRepository.saveAll(List.of(rahul, anita));
            tutorRepository.saveAll(List.of(john, jane));
            schoolRepository.save(universitySchool);
            teacherRepository.saveAll(List.of(charles, thomas));
        };
    }
}
