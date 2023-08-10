package com.example.studentcourse.repository;

import com.example.studentcourse.model.Course;
import com.example.studentcourse.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface StudentRepository extends JpaRepository<Student, Integer> {
    @Query("SELECT s.courses FROM Student s WHERE s.id = :studentId")
    Set<Course> findCoursesByStudentId(@Param("studentId") Integer studentId);

    Student findAllById(Integer studentId);
}
