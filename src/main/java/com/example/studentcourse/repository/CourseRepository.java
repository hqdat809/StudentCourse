package com.example.studentcourse.repository;

import com.example.studentcourse.model.Course;
import com.example.studentcourse.model.Student;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {
    @Query("SELECT c.students FROM Course c WHERE c.id = :courseId")
    Set<Student> findStudentByCourseId(@Param("courseId") Integer courseId);

    @Query("SELECT c from Course c  where c.name like %:name% ")
    List<Course> searchCourseByName(String name);
}
