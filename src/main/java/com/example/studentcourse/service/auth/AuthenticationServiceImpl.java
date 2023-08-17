package com.example.studentcourse.service.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.studentcourse.auth.AuthenticationRequest;
import com.example.studentcourse.auth.AuthenticationResponse;
import com.example.studentcourse.auth.RequestRefreshToken;
import com.example.studentcourse.model.Role;
import com.example.studentcourse.model.Student;
import com.example.studentcourse.repository.RoleCustomRepo;
import com.example.studentcourse.repository.StudentRepository;

import com.example.studentcourse.service.AuthenticationService;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final StudentRepository studentRepository;

    private final RoleCustomRepo roleCustomRepo;

    public final static String secret_key = "123";


    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                authenticationRequest.getEmail(),
                authenticationRequest.getPassword())
        );
        Student student = studentRepository.findByEmail(authenticationRequest.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Email is incorrect!!"));
        List<Role> role = null;
        if(student != null) {
            role = roleCustomRepo.findRoleByEmail(student.getEmail()).stream().collect(Collectors.toList());
        }

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        Set<Role> set = new HashSet<>();

        role.stream().forEach(r -> set.add(Role.builder().name(r.getName()).build()));


        student.setRoles(set);

        set.stream().forEach(i -> authorities.add(new SimpleGrantedAuthority(i.getName())));

        var jwtToken = jwtService.generateToken(student, authorities);
        var jwtRefreshToken = jwtService.generateRefreshToken(student, authorities);

        return new AuthenticationResponse(jwtToken, jwtRefreshToken);
    }

    @Override
    public AuthenticationResponse refreshToken(RequestRefreshToken requestRefreshToken) {

        DecodedJWT decodedJWT = JWT.decode(requestRefreshToken.getRefreshToken());
        System.out.println("subject: "+ decodedJWT.getSubject());
        String email = decodedJWT.getSubject();

        Student student = studentRepository.findByEmail(email).get();

        List<Role> role = null;

        if(student != null) {
            role = roleCustomRepo.findRoleByEmail(student.getEmail()).stream().collect(Collectors.toList());
        }

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        Set<Role> set = new HashSet<>();

        role.stream().forEach(r -> set.add(Role.builder().name(r.getName()).build()));

        student.setRoles(set);

        set.stream().forEach(i -> authorities.add(new SimpleGrantedAuthority(i.getName())));
        var newJwtToken = jwtService.generateToken(student, authorities);

        return new AuthenticationResponse(newJwtToken, requestRefreshToken.getRefreshToken());
    }
}
