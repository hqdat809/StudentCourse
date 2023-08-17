package com.example.studentcourse.service;

import com.example.studentcourse.model.Role;
import com.example.studentcourse.model.Student;
import com.example.studentcourse.repository.RoleCustomRepo;
import com.example.studentcourse.repository.StudentRepository;
import com.example.studentcourse.service.auth.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Optional;
import java.util.Set;

import static com.fasterxml.jackson.databind.type.LogicalType.Collection;

@ExtendWith(MockitoExtension.class)
@DataJpaTest
public class AuthServiceTest {
    @Mock
    private StudentRepository studentRepository;

    @Mock
    private RoleCustomRepo roleCustomRepo;

    @Mock
    private JwtService jwtService;
    private Student student;
    private Role role;
    private String token = "token";
    private String RefreshToken = "refreshToken";
    @BeforeEach
    public void init() {
        student = Student.builder().name("ha quoc dat").age(20)
                .address("648 tay son").email("hqdat222@gmail.com").build();
        role = Role.builder().name("STUDENT").build();
    }


}
