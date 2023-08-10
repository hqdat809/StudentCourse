package com.example.studentcourse.controller;

import com.example.studentcourse.dto.CourseDto;
import com.example.studentcourse.dto.CourseResponse;
import com.example.studentcourse.dto.StudentDto;
import com.example.studentcourse.model.Course;
import com.example.studentcourse.service.CourseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CourseController {
    @Autowired
    CourseService courseService;

    @PostMapping("/course/create")
    public ResponseEntity<CourseDto> createStudent(@Valid @RequestBody CourseDto courseDto) {
        return new ResponseEntity<>(courseService.createCourse(courseDto), HttpStatus.CREATED);
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<CourseDto> getCourseById(@PathVariable Integer courseId) {
        return new ResponseEntity<>(courseService.getCourseById(courseId), HttpStatus.OK);
    }

    @GetMapping("/courses")
    public ResponseEntity<CourseResponse> getCourseByPage(
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "0", required = false) int pageSize) {
        return new ResponseEntity<>(courseService.getCourseByPage(pageNo, pageSize), HttpStatus.OK);
    }

    @DeleteMapping("/course/delete/{courseId}")
    public ResponseEntity<String> deleteCourseById(@PathVariable Integer courseId) {
        courseService.deleteCourseById(courseId);
        return new ResponseEntity<>("Delete course success!!", HttpStatus.OK);
    }

    @PutMapping("/course/update/{courseId}")
    public ResponseEntity<CourseDto> updateCourse(@PathVariable Integer courseId, @Valid @RequestBody CourseDto courseDto) {
        CourseDto courseUpdated = courseService.updateCourse(courseId, courseDto);

        return new ResponseEntity<>(courseUpdated, HttpStatus.OK);
    }
}
