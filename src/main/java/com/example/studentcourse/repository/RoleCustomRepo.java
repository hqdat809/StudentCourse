package com.example.studentcourse.repository;

import com.example.studentcourse.model.Role;
import com.example.studentcourse.model.Student;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.cglib.core.Transformer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface RoleCustomRepo extends JpaRepository<Role, Integer> {
    @Query("SELECT s.roles FROM Student s WHERE s.email = :email")
    Set<Role> findRoleByEmail(@Param("email") String email);

//    @PersistenceContext
//    private EntityManager entityManager;
//
//    public List<Role> getRole(Student student) {
//        StringBuilder sql = new StringBuilder()
//                .append("select r.name as name from student s join student_role sr on s.id = sr.student_id\n" +
//                        "join roles r on r.id = sr.role_id");
//        sql.append("where 1=1 ");
//        if (student.getEmail() != null) {
//            sql.append(" and email = :email");
//        }
//
//        NativeQuery<Role> query = ((Session) entityManager.getDelegate()).createNativeQuery(sql.toString());
//
//        if(student.getEmail() != null) {
//            query.setParameter("email", student.getEmail());
//        }
//
//        query.addScalar("name", StandardBasicTypes.STRING);
//        query.setResultTransformer(Transformers.aliasToBean(Role.class));
//
//        return query.list();
//    }
}
