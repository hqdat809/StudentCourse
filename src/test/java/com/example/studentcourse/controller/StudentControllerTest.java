package com.example.studentcourse.controller;

import com.example.studentcourse.dto.StudentDto;
import com.example.studentcourse.dto.StudentResponse;
import com.example.studentcourse.model.Course;
import com.example.studentcourse.model.Student;
import com.example.studentcourse.repository.CourseRepository;
import com.example.studentcourse.repository.StudentRepository;
import com.example.studentcourse.service.StudentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

//@WebMvcTest(controllers = StudentController.class)
//@AutoConfigureMockMvc(addFilters = false)
//@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class StudentControllerTest {

    private Student student;
    private StudentDto studentDto;
    private Course course;


    @Autowired
    private MockMvc mockMvc ;

    @Mock
    private StudentService studentService;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private  CourseRepository courseRepository;

    @InjectMocks
    private StudentController studentController;

    @BeforeEach
    public void setUp(){
        mockMvc = MockMvcBuilders.standaloneSetup(studentController).build();
    }

    @BeforeEach
    public void init() {
        student = Student.builder().age(21)
                .name("Ha Quoc Dat").address("648 Tay son")
                .email("hqdat0809@gmail.com")
                .password("0392338494").build();
        studentDto = StudentDto.builder().age(21)
                .name("Ha Quoc Dat").address("648 Tay son")
                .email("hqdat0809@gmail.com")
                .build();
        course = Course.builder().name("Spring boot").build();
    }

    @Test
    void shouldCreateMockMvc() {
        Assertions.assertThat(mockMvc).isNotNull();
    }

    @Test
    public void AddStudentReturnJsonStudent() throws Exception {
        when(studentService.createStudent(Mockito.any(Student.class))).thenReturn(studentDto);
        ResultActions response = mockMvc.perform(post("/api/student/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(student)));
        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is(student.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.age", CoreMatchers.is(student.getAge())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.address", CoreMatchers.is(student.getAddress())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", CoreMatchers.is(student.getEmail())));
    }

    @Test
    public void AddStudentReturn400BadRequest() throws Exception {
        student.setName("");

        ResultActions response = mockMvc.perform(post("/api/student/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(student)));

        response.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void GetStudentByIdReturnJsonStudent() throws  Exception {
        when(studentService.getStudentById(Mockito.any(Integer.class))).thenReturn(studentDto);

        ResultActions response = mockMvc.perform(get("/api/student/{studentId}", 1)
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is(studentDto.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.age", CoreMatchers.is(studentDto.getAge())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.address", CoreMatchers.is(studentDto.getAddress())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", CoreMatchers.is(studentDto.getEmail())));
    }

    @Test
    public void GetStudentByIdReturn404NotFound() throws Exception {
        when(studentService.getStudentById(Mockito.any(Integer.class))).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        ResultActions response = mockMvc.perform(get("/api/student/{studentId}", 1)
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void GetStudentsReturnJsonStudentResponse() throws Exception {
        StudentResponse studentResponse = StudentResponse.builder().totalPages(10).pageNo(1).pageSize(10)
                .content(Arrays.asList(studentDto)).last(false).build();
        when(studentService.getStudentByPage(Mockito.any(Integer.class), Mockito.any(Integer.class))).thenReturn(studentResponse);

        ResultActions response = mockMvc.perform(get("/api/students", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .param("pageNo", "1")
                .param("pageSize", "10"));

        response.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void UpdateStudentReturnJsonStudentDto() throws Exception {
        Integer studentId = 1;
        when(studentService.updateStudent(Mockito.any(StudentDto.class), Mockito.any(Integer.class))).thenReturn(studentDto);

        ResultActions response = mockMvc.perform(put("/api/student/update/{studentId}", studentId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(studentDto)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is(studentDto.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.age", CoreMatchers.is(studentDto.getAge())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.address", CoreMatchers.is(studentDto.getAddress())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", CoreMatchers.is(studentDto.getEmail())));
    }

    @Test
    public void UpdateStudentReturn404NotFound() throws Exception {
        when(studentService.updateStudent(Mockito.any(StudentDto.class), Mockito.any(Integer.class)))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        ResultActions response = mockMvc.perform(put("/api/student/update/{studentId}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(studentDto)));


        response.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void DeleteStudentReturn200() throws Exception {
        doNothing().when(studentService).deleteStudent(Mockito.any(Integer.class));

        ResultActions response = mockMvc.perform(delete("/api/student/delete/{studentId}", 1)
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void RegisterCourseForStudentReturnJsonStudent() throws  Exception {
        studentDto.setCourses(Set.of(course));

        when(studentService.registerForCourse(eq(1), eq(1))).thenReturn(studentDto);

        ResultActions response = mockMvc.perform(post("/api/student/{studentId}/register", 1)
                        .param("courseId", "1")
                        .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.courses", CoreMatchers.notNullValue()));
    }
}
