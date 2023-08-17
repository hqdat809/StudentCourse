package com.example.studentcourse.controller;

import com.example.studentcourse.dto.CourseDto;
import com.example.studentcourse.dto.CourseResponse;
import com.example.studentcourse.dto.StudentDto;
import com.example.studentcourse.dto.StudentResponse;
import com.example.studentcourse.model.Course;
import com.example.studentcourse.model.Student;
import com.example.studentcourse.service.CourseService;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@ExtendWith(MockitoExtension.class)
public class CourseControllerTest {

    private Course course;
    private CourseDto courseDto;

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private CourseService courseService;

    @InjectMocks
    private CourseController courseController;

    @BeforeEach
    public void setUp(){
        mockMvc = MockMvcBuilders.standaloneSetup(courseController).build();
    }

    @BeforeEach
    public void init() {
        course = Course.builder().name("Spring boot").build();
        courseDto = CourseDto.builder().name("Spring boot").build();
    }

    @Test
    void shouldCreateMockMvc() {
        Assertions.assertThat(mockMvc).isNotNull();
    }

    @Test
    void CreateCourseReturnJsonCourse() throws Exception {
        when(courseService.createCourse(Mockito.any(CourseDto.class))).thenReturn(courseDto);
        ResultActions response = mockMvc.perform(post("/api/course/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(courseDto)));

        response.andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    public void GetCourseByIdReturnJsonCourse() throws  Exception {
        when(courseService.getCourseById(Mockito.any(Integer.class))).thenReturn(courseDto);

        ResultActions response = mockMvc.perform(get("/api/course/{courseId}", 1)
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is(courseDto.getName())));
    }

    @Test
    public void GetCourseByIdReturn404NotFound() throws Exception {
        when(courseService.getCourseById(Mockito.any(Integer.class))).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        ResultActions response = mockMvc.perform(get("/api/course/{courseId}", 1)
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void GetCoursesReturnJsonCourseResponse() throws Exception {
        CourseResponse courseResponse = CourseResponse.builder().totalPages(10).pageNo(1).pageSize(10)
                .content(Arrays.asList(courseDto)).last(false).build();
        when(courseService.getCourseByPage(Mockito.any(Integer.class), Mockito.any(Integer.class))).thenReturn(courseResponse);

        ResultActions response = mockMvc.perform(get("/api/courses", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .param("pageNo", "1")
                .param("pageSize", "10"));

        response.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void UpdateCourseReturnJsonCourseDto() throws Exception {
        when(courseService.updateCourse(Mockito.any(Integer.class), Mockito.any(CourseDto.class))).thenReturn(courseDto);

        ResultActions response = mockMvc.perform(put("/api/course/update/{courseId}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(courseDto)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is(courseDto.getName())));
    }

    @Test
    public void UpdateCourseReturn404NotFound() throws Exception {
        when(courseService.updateCourse(Mockito.any(Integer.class), Mockito.any(CourseDto.class)))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        ResultActions response = mockMvc.perform(put("/api/course/update/{courseId}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(courseDto)));


        response.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void DeleteCourseReturn200() throws Exception {
        doNothing().when(courseService).deleteCourseById(Mockito.any(Integer.class));

        ResultActions response = mockMvc.perform(delete("/api/course/delete/{courseId}", 1)
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isOk());
    }


}
