package com.college.student.portal.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "subject")
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false)
    private String subjectCode;

    @Column(nullable = false)
    private String subjectName;

    private String branch;
    private String semester;
    private int credits;
    private int theoryMarks;
    private int practicalMarks;

    @CreationTimestamp
    private LocalDateTime createdAt;
}