package com.example.studentcourse.dto;

import com.example.studentcourse.model.Student;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CourseDto {
    private Integer id;
    private String name;
    private Set<Student> students;
}
