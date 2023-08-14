package com.example.studentcourse.service.impl;

import com.example.studentcourse.dto.StudentDto;
import com.example.studentcourse.dto.StudentResponse;
import com.example.studentcourse.model.Course;
import com.example.studentcourse.model.Role;
import com.example.studentcourse.model.Student;
import com.example.studentcourse.repository.CourseRepository;
import com.example.studentcourse.repository.RoleRepository;
import com.example.studentcourse.repository.StudentRepository;
import com.example.studentcourse.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class StudentServiceImpl implements StudentService, UserDetailsService {
    @Autowired
    StudentRepository studentRepository;

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public StudentServiceImpl(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    private StudentDto mapToDto(Student student) {
        StudentDto studentDto = new StudentDto();
        studentDto.setId(student.getId());
        studentDto.setAge(student.getAge());
        studentDto.setAddress(student.getAddress());
        studentDto.setName(student.getName());
        studentDto.setCourses(student.getCourses());
        studentDto.setEmail(student.getEmail());
        return studentDto;
    }

    private Student mapToEntity(StudentDto studentDto) {
        Student student = new Student();
        student.setId(studentDto.getId());
        student.setName(studentDto.getName());
        student.setAddress(studentDto.getAddress());
        student.setAge(studentDto.getAge());
        student.setCourses(studentDto.getCourses());
        student.setEmail(studentDto.getEmail());
        return student;
    }

    @Override
    public Student saveStudent(Student student) {
        student.setPassword(passwordEncoder.encode(student.getPassword()));
        return studentRepository.save(student);
    }

    @Override
    public Role saveRole(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public void addRoleToStudent(String studentEmail, String roleName) {
        Student student = studentRepository.findByEmail(studentEmail).get();
        Role role = roleRepository.findByName(roleName).get();
        student.getRoles().add(role);
        studentRepository.save(student);
    }

    @Override
    public StudentDto createStudent(Student student) {
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
        studentData.setEmail(studentDto.getEmail());

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

        System.out.println("student course: " + student.getCourses());
        student.getCourses().stream().forEach(c -> {
            if (c.getId() == courseId) {
                throw new ResponseStatusException((HttpStatus.BAD_REQUEST), "Student is enrolled this course!!");
            }
        });

        student.getCourses().add(course);

        Student studentSaved = studentRepository.save(student);

        return mapToDto(studentSaved);

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Student student = studentRepository.findByEmail(username).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found student!!"));

        return Student.builder()
                .id(student.getId())
                .name(student.getName())
                .email(student.getUsername())
                .roles(student.getRoles())
                .build();
    }
}
