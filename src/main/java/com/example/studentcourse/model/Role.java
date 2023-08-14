package com.example.studentcourse.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Data
@Table(name = "Roles")
public class Role {
    @Id
    @SequenceGenerator(name = "roles_sequence", sequenceName = "roles_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "roles_sequence")
    private Integer id;

    private String name;

    @ManyToMany(mappedBy = "roles", fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SELECT)
    @JsonBackReference
    private Set<Student> students = new HashSet<>();
}
