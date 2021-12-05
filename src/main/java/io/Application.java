package io;

import io.monitoring.model.Role;
import io.monitoring.model.dict.RoleType;
import io.monitoring.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.HashSet;
import java.util.Set;

@EnableAsync
@EnableScheduling
@SpringBootApplication
public class Application implements CommandLineRunner {

    private final RoleRepository roleRepository;

    @Autowired
    public Application(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) {
        if (roleRepository.findAll().isEmpty()) {
            // Add User Roles
            Set<Role> roles = new HashSet<>();
            roles.add(new Role(null, RoleType.ROLE_ADMIN));
            roles.add(new Role(null, RoleType.ROLE_USER));
            roleRepository.saveAll(roles);
        }
    }
}
