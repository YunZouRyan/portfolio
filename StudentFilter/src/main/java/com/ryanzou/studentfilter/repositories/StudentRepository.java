package com.ryanzou.studentfilter.repositories;

import com.ryanzou.studentfilter.beans.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Integer> {

    // find by date before
    List<Student> findByEnrollmentDateAfter(LocalDate enrollmentDate);

    // find by GPA greater than
    List<Student> findByGpaGreaterThan(double gpa);

    // find by GPA in range
    List<Student> findByGpaBetween(double lower, double upper);

    // find by program selection
    List<Student> findByProgram(String program);

    // find by student name contains
    List<Student> findByNameContains(String keyword);

    // sort by enrollment date descending
    List<Student> findAllByOrderByEnrollmentDateDesc();
}
