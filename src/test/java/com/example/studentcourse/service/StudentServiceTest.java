package com.example.studentcourse.service;

import com.example.studentcourse.dto.StudentDto;
import com.example.studentcourse.dto.StudentResponse;
import com.example.studentcourse.model.Course;
import com.example.studentcourse.model.Student;
import com.example.studentcourse.repository.CourseRepository;
import com.example.studentcourse.repository.StudentRepository;
import com.example.studentcourse.service.impl.StudentServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.junit.jupiter.api.Assertions.assertAll;


import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@DataJpaTest
public class StudentServiceTest {

    @Mock PasswordEncoder passwordEncoder;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private StudentServiceImpl studentService;

    private Student student;
    private StudentDto studentDto;
    private StudentResponse studentResponse;
    private Course course;

    @BeforeEach
    public void init() {

        student = Student.builder().id(1).age(21)
                .name("Ha Quoc Dat").address("648 Tay son")
                .email("hqdat080932@gmail.com")
                .password("0392338494").build();
        studentDto = StudentDto.builder().age(21)
                .name("Ha Quoc Dat").address("648 Tay son")
                .email("hqdat0809@gmail.com")
                .build();
         course = Course.builder().name("ABC").build();

    }

    @Test
    public void CreateStudentReturnStudentDto() {

        Mockito.when(studentRepository.save(Mockito.any(Student.class))).thenReturn(student);


        StudentDto studentReturn = studentService.createStudent(student);
        Assertions.assertThat(studentReturn).isNotNull();
        Assertions.assertThat(studentReturn.getId()).isGreaterThan(0);
    }

    @Test
    public void GetStudentByIdReturnStudentDto() {
        Mockito.when(studentRepository.findById(1)).thenReturn(Optional.ofNullable(student));

        StudentDto studentReturn = studentService.getStudentById(1);

        Assertions.assertThat(studentReturn).isNotNull();
        Assertions.assertThat(studentReturn.getId()).isGreaterThan(0);
    }

    @Test
    public void GetStudentByPageReturnStudentResponse() {
        Page<Student> students = Mockito.mock(Page.class);

        Mockito.when(studentRepository.findAll(Mockito.any(Pageable.class))).thenReturn(students);

        StudentResponse studentsReturn = studentService.getStudentByPage(1, 10);
        Assertions.assertThat(studentsReturn).isNotNull();
    }

    @Test
    public void UpdateStudentReturnStudentDto() {
        Mockito.when(studentRepository.findById(1)).thenReturn(Optional.ofNullable(student));
        Mockito.when(studentRepository.save(Mockito.any(Student.class))).thenReturn(student);

        StudentDto studentReturn = studentService.updateStudent(studentDto, 1);

        Assertions.assertThat(studentReturn).isNotNull();
    }

    @Test
    public void DeleteStudentReturnVoid() {
        Mockito.when(studentRepository.findById(1)).thenReturn(Optional.ofNullable(student));
        Mockito.doNothing().when(studentRepository).delete(student);

        assertAll(() -> studentService.deleteStudent(1));
    }

    @Test
    public void RegisterForCourseReturnStudent() {
        Mockito.when(studentRepository.findById(1)).thenReturn(Optional.ofNullable(student));
        Mockito.when(courseRepository.findById(1)).thenReturn(Optional.ofNullable(course));
        Mockito.when(studentRepository.save(Mockito.any(Student.class))).thenReturn(student);

        StudentDto studentReturn = studentService.registerForCourse(1, 1);

        Assertions.assertThat(studentReturn).isNotNull();
        Assertions.assertThat(studentReturn.getCourses()).isNotNull();
    }
}
