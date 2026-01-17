package com.ryanzou.studentfilter;


import com.ryanzou.studentfilter.beans.Student;
import com.ryanzou.studentfilter.repositories.StudentRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@AllArgsConstructor
@Component
public class BootStrapData implements CommandLineRunner {

    private StudentRepository studentRepository;

    @Override
    public void run(String... args) throws Exception {

        Student student1 = new Student("Alice Johnson", "alice@example.com", LocalDate.parse("2023-09-01"), 3.8, "Computer Science");
        Student student2 = new Student("Bob Smith", "bob@example.com", LocalDate.parse("2024-01-15"), 3.2, "Business");
        Student student3 = new Student("Charlie Lee", "charlie@example.com", LocalDate.parse("2022-05-10"), 3.6, "Data Science");
        Student student4 = new Student("David Kim", "david@example.com", LocalDate.parse("2023-06-20"), 3.9, "Computer Science");
        Student student5 = new Student("Emma Brown", "emma@example.com", LocalDate.parse("2021-12-05"), 3.1, "Business");

        studentRepository.save(student1);
        studentRepository.save(student2);
        studentRepository.save(student3);
        studentRepository.save(student4);
        studentRepository.save(student5);

    }
}
