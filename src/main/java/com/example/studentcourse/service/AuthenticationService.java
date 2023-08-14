package com.example.studentcourse.service;

import com.example.studentcourse.auth.AuthenticationRequest;
import com.example.studentcourse.auth.AuthenticationResponse;
import com.example.studentcourse.auth.RequestRefreshToken;

public interface AuthenticationService {

    AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest);

    AuthenticationResponse refreshToken(RequestRefreshToken refreshToken);
}
