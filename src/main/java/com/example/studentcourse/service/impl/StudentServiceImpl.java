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
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.rmi.StubNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

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
    public Student  saveStudent(Student student) {
        System.out.println(student.getPassword());
        student.setPassword(passwordEncoder.encode(student.getPassword()));
        System.out.println("student with password: " + student.getPassword());
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
        System.out.println(student);
        Student studentResponse = saveStudent(student);

        return mapToDto(studentResponse);
    }

    @Override
    public StudentDto getStudentById(Integer studentId) {
        Student studentData = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResponseStatusException((HttpStatus.NOT_FOUND), "Invalid student id "+ studentId));

        return mapToDto(studentData);
    }


    @Override
    public List<StudentDto> filterStudent(String name, Integer age, String email, String address) {
        System.out.println("name: "+ name);
        System.out.println("age: "+ age);
        System.out.println("email: "+ email);
        System.out.println("address: "+ address);
        List<Student> studentData;
        List<StudentDto> studentDtos = new ArrayList<>();



        if (age == null) {
            studentData = studentRepository.filterWithoutAge(name, email, address);
        } else  {
            studentData = studentRepository.filterWithAge(name,age , email, address);
        }

        System.out.println("data: " + studentData.size());

        if (studentData != null && !studentData.isEmpty() ) {
            for (Student student:studentData) {
//                System.out.println("student: "+ student);
//                System.out.println("student course: "+ student.getCourses());
                studentDtos.add(mapToDto(student));
            }
        }
        return studentDtos;
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

        if (studentDto.getName() != null) {
            studentData.setName(studentDto.getName());
        }
        if (studentDto.getAge() != null) {
            studentData.setAge(studentDto.getAge());
        }
        if (studentDto.getAddress() != null) {
            studentData.setAddress(studentDto.getAddress());
        }
        if (studentDto.getEmail() != null) {
            studentData.setEmail(studentDto.getEmail());
        }

        Student studentUpdated = studentRepository.save(studentData);

        System.out.println("student update: " + studentUpdated);

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

        Set<Course> listCourse = new HashSet<>();

        System.out.println("student course: " + student.getCourses());
        if (student.getCourses() != null && !student.getCourses().isEmpty()) {
            student.getCourses().stream().forEach(c -> {
                if (c.getId() == courseId) {
                    throw new ResponseStatusException((HttpStatus.BAD_REQUEST), "Student is enrolled this course!!");
                }
                listCourse.add(c);
            });
        } else {
            listCourse.add(course);
        }
        student.setCourses(listCourse);

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
