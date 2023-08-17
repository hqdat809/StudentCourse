package com.example.studentcourse.repository;

import com.example.studentcourse.model.Course;
import com.example.studentcourse.model.Role;
import com.example.studentcourse.model.Student;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {
    Optional<Student> findByEmail (String email);

    @Query("SELECT s from Student s where s.name like %:name% and s.email like %:email% and s.age = :age and s.address like %:address% ")
    List<Student> filterWithAge(String name,  Integer age, String email, String address);

    @Query("SELECT s from Student s where s.name like %:name% and s.email like %:email% and s.address like %:address% ")
    List<Student> filterWithoutAge(String name, String email, String address);

    @Query("SELECT s.roles FROM Student s WHERE s.email = :email")
    Set<Role> findRoleByEmail(@Param("email") String email);

}
