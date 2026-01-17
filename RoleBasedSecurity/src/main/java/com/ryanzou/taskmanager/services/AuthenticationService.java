package com.ryanzou.taskmanager.services;

import com.ryanzou.taskmanager.beans.Role;
import com.ryanzou.taskmanager.beans.User;
import com.ryanzou.taskmanager.models.AuthenticationRequest;
import com.ryanzou.taskmanager.models.AuthenticationResponse;
import com.ryanzou.taskmanager.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthenticationService {

    private AuthenticationManager authenticationManager;

    private UserRepository userRepository;

    private JwtService jwtService;

    private PasswordEncoder passwordEncoder;

    public AuthenticationResponse register(AuthenticationRequest authenticationRequest) {
        User user = User
                .builder()
                .email(authenticationRequest.getEmail())
                .password(passwordEncoder.encode(authenticationRequest.getPassword()))
                .role(Role.USER)
                .build();

        userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse.builder().token(jwtToken).build();

    }

    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(),
                authenticationRequest.getPassword())
        );
        User user = userRepository.findByEmail(authenticationRequest.getEmail()).orElseThrow();
        var jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse.builder().token(jwtToken).build();
    }
}
