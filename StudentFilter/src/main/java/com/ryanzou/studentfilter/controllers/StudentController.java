package com.ryanzou.studentfilter.controllers;

import com.ryanzou.studentfilter.beans.Student;
import com.ryanzou.studentfilter.repositories.StudentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Controller
public class StudentController {

    private final StudentRepository appointmentRepository;
    private final StudentRepository studentRepository;

    @GetMapping("/")
    public String index(Model model) {

        model.addAttribute("student", new Student());

        List<Student> students = studentRepository.findAll();
        List<Student> filteredList = new ArrayList<>(appointmentRepository.findAllByOrderByEnrollmentDateDesc());

        model.addAttribute("studentList", students);
        model.addAttribute("filteredList", students);
        return "index";
    }

    @GetMapping("/filter")
    public String filter(
            Model model,
            @RequestParam(name = "enrollmentDate", required = false) LocalDate enrollmentDate,
            @RequestParam(name = "minGpa", required = false) Double minGpa,
            @RequestParam(name = "program", required = false) String program,
            @RequestParam(name = "nameContains", required = false) String nameContains,
            @RequestParam(name = "minGpaRange", required = false) Double minGpaRange,
            @RequestParam(name = "maxGpaRange", required = false) Double maxGpaRange
    ) {
        List<Student> enrolledAfterDate = new ArrayList<>();
        List<Student> gpaGreaterThan = new ArrayList<>();
        List<Student> gpaWithinRange = new ArrayList<>();
        List<Student> inProgram = new ArrayList<>();
        List<Student> contains = new ArrayList<>();

        List<Student> filteredList = new ArrayList<>(appointmentRepository.findAllByOrderByEnrollmentDateDesc());
        if (enrollmentDate != null) {
            enrolledAfterDate = studentRepository.findByEnrollmentDateAfter(enrollmentDate);
        }
        if (minGpa != null) {
            gpaGreaterThan = studentRepository.findByGpaGreaterThan(minGpa);
        }
        if (program != null) {
            inProgram = studentRepository.findByProgram(program);
        }
        if (nameContains != null) {
            contains = studentRepository.findByNameContains(nameContains);
        }
        if (minGpaRange != null && maxGpaRange != null) {
            gpaWithinRange = studentRepository.findByGpaBetween(minGpaRange, maxGpaRange);
        }

        filteredList = checkSharedStudents(filteredList, enrolledAfterDate);
        filteredList = checkSharedStudents(filteredList, gpaGreaterThan);
        filteredList = checkSharedStudents(filteredList, gpaWithinRange);
        filteredList = checkSharedStudents(filteredList, inProgram);
        filteredList = checkSharedStudents(filteredList, contains);

        model.addAttribute("filteredList", filteredList);

        model.addAttribute("student", new Student());

        List<Student> students = studentRepository.findAll();

        model.addAttribute("studentList", students);

        return "index";
    }

    public List<Student> checkSharedStudents(List<Student> list1, List<Student> list2) {
        List<Student> returnList = new ArrayList<>();
        if (list1 != null && !list1.isEmpty()) {
            if (list2 != null && !list2.isEmpty()) {
                 for (Student s1 : list1) {
                     if (list2.contains(s1)) {
                         returnList.add(s1);
                     }
                 }

                return returnList;
            }
        }
        return list1;
    }
}