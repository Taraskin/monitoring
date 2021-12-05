package io.monitoring.controller;

import io.monitoring.component.auth.JwtUtils;
import io.monitoring.exception.UserAuthException;
import io.monitoring.model.User;
import io.monitoring.model.payload.JwtResponse;
import io.monitoring.model.payload.LoginRequest;
import io.monitoring.model.payload.MessageResponse;
import io.monitoring.model.payload.RegisterRequest;
import io.monitoring.security.UserDetailsImpl;
import io.monitoring.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final AuthService authService;
    private final JwtUtils jwtUtils;

    @Autowired
    public AuthController(AuthService authService, JwtUtils jwtUtils) {
        this.authService = authService;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/login")
    public ResponseEntity<?> userLogin(@RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authService.authenticateUser(loginRequest);

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateJwtToken(authentication);

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            List<String> roles = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());

            logger.info("User: {} logged in", userDetails.getUsername());

            return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(), roles));

        } catch (UserAuthException e) {
            logger.error("User Login - UserAuthException: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest registerRequest) {
        try {
            User user = authService.addNewUser(registerRequest);

            logger.info("Registered new user: {} with roles: {}", user.getUsername(), user.getRoles());

            return ResponseEntity.status(HttpStatus.CREATED).build();

        } catch (UserAuthException e) {
            logger.error("User Registration - UserAuthException: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
        }
    }
}