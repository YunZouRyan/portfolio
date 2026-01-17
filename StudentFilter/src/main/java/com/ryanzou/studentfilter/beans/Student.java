package com.ryanzou.studentfilter.beans;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NonNull
    @Column(length = 50)
    private String name;

    @NonNull
    @Column(length = 100)
    private String email;

    @NonNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate enrollmentDate;

    @NonNull
    private double gpa;

    @NonNull
    @Column(length = 50)
    private String program;
}
