package com.example.studentcourse.repository;

import com.example.studentcourse.model.Student;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class StudentRepositoryTest {
    @Autowired
    private StudentRepository studentRepository;

    @Test
    public void StudentRepository_SaveAll_ReturnSavedStudent() {
        Student student = Student.builder().id(1).age(21)
                .name("Ha Quoc Dat").address("648 Tay son")
                .email("hqdat080932@gmail.com")
                .password("0392338494").build();

        Student studentReturn = studentRepository.save(student);

        Assertions.assertThat(studentReturn).isNotNull();
        Assertions.assertThat(studentReturn.getId()).isGreaterThan(0);
    }
}
