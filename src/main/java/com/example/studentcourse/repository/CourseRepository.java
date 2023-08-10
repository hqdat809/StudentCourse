package com.example.studentcourse.repository;

import com.example.studentcourse.model.Course;
import com.example.studentcourse.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface CourseRepository extends JpaRepository<Course, Integer> {
    @Query("SELECT c.students FROM Course c WHERE c.id = :courseId")
    Set<Student> findStudentByCourseId(@Param("courseId") Integer courseId);
}
