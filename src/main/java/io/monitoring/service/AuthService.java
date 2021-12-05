package io.monitoring.service;

import io.monitoring.exception.UserAuthException;
import io.monitoring.model.Role;
import io.monitoring.model.User;
import io.monitoring.model.dict.RoleType;
import io.monitoring.model.payload.LoginRequest;
import io.monitoring.model.payload.RegisterRequest;
import io.monitoring.repository.RoleRepository;
import io.monitoring.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class AuthService {

    private static final RuntimeException ROLE_NOT_FOUND_EXCEPTION = new RuntimeException("Error: Role is not found.");

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;

    @Autowired
    public AuthService(AuthenticationManager authenticationManager, UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder encoder) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
    }

    public Authentication authenticateUser(LoginRequest loginRequest) throws UserAuthException {
        try {
            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        } catch (AuthenticationException e) {
            logger.error("User Login: AuthenticationException: {}", e.getMessage(), e);
            throw new UserAuthException(e.getMessage(), e);
        }
    }

    public User addNewUser(RegisterRequest registerRequest) throws UserAuthException {

        if (userRepository.findByUsername(registerRequest.getUsername()).isPresent()) {
            throw new UserAuthException("Error: Username is already taken!");
        }

        User user = new User(null, registerRequest.getUsername(), encoder.encode(registerRequest.getPassword()), null);

        Set<Role> userRoles = collectRoles(registerRequest.getRoles());

        user.setRoles(userRoles);

        return userRepository.save(user);
    }

    private Set<Role> collectRoles(Set<String> stringRoleSet) {
        Set<Role> userRoles = new HashSet<>();
        Optional.ofNullable(stringRoleSet).ifPresentOrElse(roles -> roles.stream()
                        .map(RoleType::fromString)
                        .filter(Optional::isPresent)
                        .forEach(roleType -> {
                            Role userRole = roleRepository.findByName(roleType.get())
                                    .orElseThrow(() -> ROLE_NOT_FOUND_EXCEPTION);
                            userRoles.add(userRole);
                        }),
                () -> {
                    Role userRole = roleRepository.findByName(RoleType.ROLE_USER)
                            .orElseThrow(() -> ROLE_NOT_FOUND_EXCEPTION);
                    userRoles.add(userRole);
                });
        return userRoles;
    }
}
