package com.example.studentcourse.dto;

import com.example.studentcourse.model.Course;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StudentDto {
    private Integer id;
    private String name;
    private Integer age;
    private String address;
    private Set<Course> courses;
}