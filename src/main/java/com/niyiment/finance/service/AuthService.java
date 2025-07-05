package com.niyiment.finance.service;


import com.niyiment.finance.dto.LoginRequest;
import com.niyiment.finance.dto.LoginResponse;
import com.niyiment.finance.dto.RegisterRequest;
import com.niyiment.finance.exception.DuplicationResourceException;
import com.niyiment.finance.models.User;
import com.niyiment.finance.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.lang.module.ResolutionException;


@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public LoginResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new DuplicationResourceException("User with this email already exists");
        }

        User user = new User();
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        userRepository.save(user);

        String jwtToken = jwtService.generateToken(user);

        return new LoginResponse(jwtToken, jwtService.getExpirationTime());
    }

    public LoginResponse authentication(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.email(), request.password()
                    )
            );
            log.info("Authentication stage passed: {}", request);
            log.info("User repository: {}", userRepository);
            User user = userRepository.findByEmail(request.email())
                    .orElseThrow(() -> new ResolutionException("User not found"));
            log.debug("User found: {}", user);

            String jwtToken = jwtService.generateToken(user);
            log.debug("Jwt token: {}", jwtToken);

            return new LoginResponse(jwtToken, jwtService.getExpirationTime());
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new RuntimeException("Invalid credentials");
        }
    }
}
