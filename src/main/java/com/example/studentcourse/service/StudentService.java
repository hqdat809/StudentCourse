package com.example.studentcourse.service;

import com.example.studentcourse.dto.StudentDto;
import com.example.studentcourse.dto.StudentResponse;
import com.example.studentcourse.model.Student;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public interface StudentService {
    StudentDto createStudent(StudentDto studentDto);

    StudentDto getStudentById(Integer studentId);

    StudentResponse getStudentByPage(int pageNo, int pageSize);

    StudentDto updateStudent(StudentDto studentDto, Integer studentId);

    void deleteStudent(Integer studentId);

    StudentDto registerForCourse(Integer studentId, Integer courseId);
}
