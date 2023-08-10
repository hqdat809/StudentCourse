package com.example.studentcourse.model;

import com.example.studentcourse.dto.CourseDto;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Student {
/*
    Id
    StudentCode
    Password
    Name
    Age
    Address
    Courses
 */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @NotNull(message = "Name shouldn't be null!!")
    @NotBlank(message = "Name shouldn't be blank!!")
    @Size(min = 5, message = "Name should be at least 5 characters")
    @Size(max = 50, message = "Name should be less than 5 characters")
    private String name;

    @NotNull(message = "Age shouldn't be null")
    @Min(value = 0, message = "Age shouldn't be less than 1")
    @Max(value = 200, message = "Age should be less than 200")
    private Integer age;

    @NotNull(message = "Address shouldn't be null")
    @NotBlank(message = "Address shouldn't be blank!!")
    private String address;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "course_student",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id"))
    @JsonIgnore
    private Set<Course> courses = new HashSet<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Set<Course> getCourses() {
        return courses;
    }

    public void setCourses(Set<Course> courses) {
        this.courses = courses;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age='" + age + '\'' +
                ", address='" + address + '\'' +
                ", courses=" + courses +
                '}';
    }
}
