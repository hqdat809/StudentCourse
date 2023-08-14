package com.example.studentcourse;

import com.example.studentcourse.model.Role;
import com.example.studentcourse.model.Student;
import com.example.studentcourse.service.StudentService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.HashSet;

@SpringBootApplication
@EnableWebSecurity
@EnableJpaRepositories
public class StudentCourseApplication {

	public static void main(String[] args) {
		SpringApplication.run(StudentCourseApplication.class, args);
	}

	@Bean
	BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

//	@Bean
//	CommandLineRunner run(StudentService studentService) {
//		return args -> {
//			studentService.saveRole(Role.builder().name("ADMIN").build());
//			studentService.saveRole(Role.builder().name("STUDENT").build());
//
//			studentService.saveStudent(Student.builder()
//					.name("ha quoc dat")
//					.age(20)
//					.address("648 tay son")
//					.email("hqdat0809@gmail.com")
//					.password("0392338494")
//					.build());
//			studentService.saveStudent(Student.builder()
//					.name("ha quoc dat 2")
//					.age(20)
//					.address("648 tay son")
//					.email("hqdat08092001@gmail.com")
//					.password("0392338494")
//					.build());
//
//			studentService.addRoleToStudent	("hqdat0809@gmail.com", "ADMIN");
//			studentService.addRoleToStudent("hqdat08092001@gmail.com", "STUDENT");
//		};
//	}
}
