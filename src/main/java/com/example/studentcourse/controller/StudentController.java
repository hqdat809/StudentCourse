package com.example.studentcourse.controller;

import com.example.studentcourse.dto.StudentDto;
import com.example.studentcourse.dto.StudentResponse;
import com.example.studentcourse.model.Student;
import com.example.studentcourse.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreFilter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class StudentController {
    @Autowired
    StudentService studentService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/student/create")
    public ResponseEntity<StudentDto> createStudent(@Valid @RequestBody Student student) {
        return new ResponseEntity<>(studentService.createStudent(student), HttpStatus.CREATED);
    }

    @GetMapping("/student/{studentId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<StudentDto> getStudentById(@PathVariable Integer studentId) {
        return new ResponseEntity<>(studentService.getStudentById(studentId), HttpStatus.OK);
    }

    @GetMapping("/students")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<StudentResponse> getStudents(
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "0", required = false) int pageSize) {
        return new ResponseEntity<>(studentService.getStudentByPage(pageNo, pageSize), HttpStatus.OK);
    }

    @PutMapping("/student/update/{studentId}")
    @PreAuthorize("hasAuthority('ADMIN') or #studentId == authentication.credentials") // Check studentid same as userLogin's id
    public ResponseEntity<?> updateStudent(@Valid @RequestBody StudentDto studentDto, @PathVariable Integer studentId ) {
        return new ResponseEntity<>(studentService.updateStudent(studentDto, studentId), HttpStatus.OK);
    }


    @DeleteMapping("/student/delete/{studentId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> deleteStudent(@PathVariable Integer studentId ) {
        studentService.deleteStudent(studentId);
        return new ResponseEntity<>("Delete student success!!",HttpStatus.OK);
    }

    @PostMapping("/student/{studentId}/register")
    @PreAuthorize("hasAuthority('ADMIN') or #studentId == authentication.credentials") // Check studentId same as studentLogin's id
    public ResponseEntity<StudentDto> registerForCourse(@PathVariable Integer studentId, @RequestParam Integer courseId) {
        StudentDto studentInfo = studentService.registerForCourse(studentId,courseId);
        return new ResponseEntity<>(studentInfo, HttpStatus.OK);
    }

}
