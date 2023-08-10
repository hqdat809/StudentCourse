package com.example.studentcourse.service.impl;

import com.example.studentcourse.dto.StudentDto;
import com.example.studentcourse.dto.StudentResponse;
import com.example.studentcourse.model.Course;
import com.example.studentcourse.model.Student;
import com.example.studentcourse.repository.CourseRepository;
import com.example.studentcourse.repository.StudentRepository;
import com.example.studentcourse.service.CourseService;
import com.example.studentcourse.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl implements StudentService {
    @Autowired
    StudentRepository studentRepository;

    @Autowired
    CourseRepository courseRepository;

    private StudentDto mapToDto(Student student) {
        StudentDto studentDto = new StudentDto();
        studentDto.setId(student.getId());
        studentDto.setAge(student.getAge());
        studentDto.setAddress(student.getAddress());
        studentDto.setName(student.getName());
        studentDto.setCourses(student.getCourses());
        return studentDto;
    }

    private Student mapToEntity(StudentDto studentDto) {
        Student student = new Student();
        student.setId(studentDto.getId());
        student.setName(studentDto.getName());
        student.setAddress(studentDto.getAddress());
        student.setAge(studentDto.getAge());
        student.setCourses(studentDto.getCourses());
        return student;
    }

    @Override
    public StudentDto createStudent(StudentDto studentDto) {
        Student student = mapToEntity(studentDto);

        Student studentResponse = studentRepository.save(student);

        return mapToDto(studentResponse);
    }

    @Override
    public StudentDto getStudentById(Integer studentId) {
        Student studentData = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResponseStatusException((HttpStatus.NOT_FOUND), "Invalid student id "+ studentId));

        return mapToDto(studentData);
    }

    @Override
    public StudentResponse getStudentByPage(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Student> studentData = studentRepository.findAll(pageable);

//      Map to StudentResponse
        List<Student> listStudentData = studentData.getContent();
        List<StudentDto> content = new ArrayList<>();
        for (Student student:listStudentData) {
            content.add(mapToDto(student));
        }
        StudentResponse studentResponse = new StudentResponse();
        studentResponse.setContent(content);
        studentResponse.setPageNo(studentData.getNumber());
        studentResponse.setPageSize(studentData.getSize());
        studentResponse.setTotalPages(studentData.getTotalPages());
        studentResponse.setTotalElements(studentData.getTotalElements());
        studentResponse.setLast(studentData.isLast());

        return studentResponse;
    }

    @Override
    public StudentDto updateStudent(StudentDto studentDto, Integer studentId) {
        Student studentData = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResponseStatusException((HttpStatus.NOT_FOUND), "Invalid student id "+ studentId));

        studentData.setName(studentDto.getName());
        studentData.setAge(studentDto.getAge());
        studentData.setAddress(studentDto.getAddress());

        Student studentUpdated = studentRepository.save(studentData);

        return mapToDto(studentUpdated);
    }

    @Override
    public void deleteStudent(Integer studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResponseStatusException((HttpStatus.NOT_FOUND), "Invalid student id "+ studentId));
        studentRepository.delete(student);
    }

    @Override
    public StudentDto registerForCourse(Integer studentId, Integer courseId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResponseStatusException((HttpStatus.NOT_FOUND), "Invalid student id "+ studentId));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResponseStatusException((HttpStatus.NOT_FOUND), "Invalid course id "+ courseId));

        student.getCourses().add(course);


        Student studentSaved = studentRepository.save(student);

        return mapToDto(studentSaved);

    }
}
