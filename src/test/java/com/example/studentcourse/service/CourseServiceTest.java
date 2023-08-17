package com.example.studentcourse.service;

import com.example.studentcourse.dto.CourseDto;
import com.example.studentcourse.dto.CourseResponse;
import com.example.studentcourse.model.Course;
import com.example.studentcourse.repository.CourseRepository;
import com.example.studentcourse.service.impl.CourseServiceImpl;
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
import static org.junit.jupiter.api.Assertions.assertAll;


import java.util.Optional;

@DataJpaTest
@ExtendWith(MockitoExtension.class)
public class CourseServiceTest {
    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private CourseServiceImpl courseService;

    private Course course;
    private  CourseDto courseDto;
    @BeforeEach
    public void init() {
        course = Course.builder().name("ABC").build();
        courseDto = CourseDto.builder().name("ABC1").build();
    }

    @Test
    public void CreateCourseReturnCourseDto() {

        Mockito.when(courseRepository.save(Mockito.any(Course.class))).thenReturn(course);

        CourseDto courseReturn = courseService.createCourse(courseDto);

        Assertions.assertThat(courseReturn).isNotNull();
    }

    @Test
    public void GetCourseByIdReturnCourseDto() {
        Mockito.when(courseRepository.findById(1)).thenReturn(Optional.ofNullable(course));

        CourseDto courseReturn = courseService.getCourseById(1);
        Assertions.assertThat(courseReturn).isNotNull();

    }

    @Test
    public void GetCourseByPageReturnCourseResponse() {
        Page<Course> coursePage = Mockito.mock(Page.class);

        Mockito.when(courseRepository.findAll(Mockito.any(Pageable.class))).thenReturn(coursePage);

        CourseResponse coursesReturn = courseService.getCourseByPage(1, 10);
        Assertions.assertThat(coursesReturn).isNotNull();
    }

    @Test
    public void DeleteCourseByIdReturnVoid() {
        Mockito.when(courseRepository.findById(1)).thenReturn(Optional.ofNullable(course));
        Mockito.doNothing().when(courseRepository).delete(Mockito.any(Course.class));

        assertAll(() -> courseService.deleteCourseById(1));
    }

    @Test
    public void UpdateCourseReturnCourseDto() {
        Mockito.when(courseRepository.findById(1)).thenReturn(Optional.ofNullable(course));
        Mockito.when(courseRepository.save(Mockito.any(Course.class))).thenReturn(course);

        CourseDto courseReturn = courseService.updateCourse(1, courseDto);
        Assertions.assertThat(courseReturn).isNotNull();
        Assertions.assertThat(courseReturn.getName()).isEqualTo(courseDto.getName());
    }

}
