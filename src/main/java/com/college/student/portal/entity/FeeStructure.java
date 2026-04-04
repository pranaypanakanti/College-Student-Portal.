package com.college.student.portal.entity;

import java.time.LocalDate;
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
@Table(name = "fee_structure")
public class FeeStructure {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String branch;
    private String semester;
    private Long tuitionFee;
    private Long hostelFee;
    private Long libraryFee;
    private Long labFee;
    private Long totalFee;
    private LocalDate dueDate;

    @CreationTimestamp
    private LocalDateTime createdAt;
}