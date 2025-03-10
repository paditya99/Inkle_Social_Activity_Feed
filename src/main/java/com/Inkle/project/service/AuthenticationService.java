package com.Inkle.project.service;

import com.Inkle.project.dto.AuthResponse;
import com.Inkle.project.dto.LoginRequest;
import com.Inkle.project.dto.RegisterRequest;
import com.Inkle.project.entity.User;
import com.Inkle.project.enums.UserRole;
import com.Inkle.project.repository.UserRepository;
import com.Inkle.project.security.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthenticationService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public AuthResponse register(RegisterRequest request) {
        logger.debug("Starting registration process for user: {}", request.getUsername());
        
        try {
            // Check if username or email already exists
            if (userRepository.existsByUsername(request.getUsername())) {
                logger.warn("Username already exists: {}", request.getUsername());
                throw new RuntimeException("Username already exists");
            }
            if (userRepository.existsByEmail(request.getEmail())) {
                logger.warn("Email already exists: {}", request.getEmail());
                throw new RuntimeException("Email already exists");
            }

            User user = new User();
            user.setUsername(request.getUsername());
            user.setEmail(request.getEmail());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setRole(UserRole.USER);
            
            logger.debug("Saving new user to database: {}", request.getUsername());
            userRepository.save(user);
            
            String jwtToken = jwtService.generateToken(user);
            logger.info("Successfully registered user: {}", request.getUsername());
            
            AuthResponse response = new AuthResponse();
            response.setToken(jwtToken);
            return response;
        } catch (Exception e) {
            logger.error("Error during registration for user: {}", request.getUsername(), e);
            throw e;
        }
    }

    public AuthResponse login(LoginRequest request) {
        logger.debug("Starting login process for user: {}", request.getUsername());
        
        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    request.getUsername(),
                    request.getPassword()
                )
            );
            
            User user = userRepository.findByUsername(request.getUsername())
                    .orElseThrow(() -> {
                        logger.error("User not found: {}", request.getUsername());
                        return new RuntimeException("User not found");
                    });
            
            String jwtToken = jwtService.generateToken(user);
            logger.info("Successfully logged in user: {}", request.getUsername());
            
            AuthResponse response = new AuthResponse();
            response.setToken(jwtToken);
            return response;
        } catch (Exception e) {
            logger.error("Error during login for user: {}", request.getUsername(), e);
            throw e;
        }
    }
} 