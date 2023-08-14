package com.example.studentcourse.controller;

import com.example.studentcourse.auth.AuthenticationRequest;
import com.example.studentcourse.auth.AuthenticationResponse;
import com.example.studentcourse.auth.RequestRefreshToken;
import com.example.studentcourse.service.AuthenticationService;
import com.example.studentcourse.service.auth.AuthenticationServiceImpl;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.AccessType;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    @Autowired
    AuthenticationService authenticationService;
    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AuthenticationResponse> login(@Valid @RequestBody AuthenticationRequest authenticationRequest) {

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(authenticationService.authenticate(authenticationRequest));
    }

    @PostMapping(value ="/refresh")
    public ResponseEntity<AuthenticationResponse> refreshToken(@RequestBody RequestRefreshToken requestRefreshToken) {
        return new ResponseEntity<>(authenticationService.refreshToken(requestRefreshToken), HttpStatus.OK);
    }
}
