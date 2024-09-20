package project.source.configs;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import project.source.exceptions.NotFoundException;
import project.source.models.entities.Role;
import project.source.models.entities.User;
import project.source.repositories.RoleRepository;
import project.source.repositories.UserRepository;
import project.source.models.enums.UserStatus;

import java.util.List;

@Configuration
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ApplicationInitConfig {
    RoleRepository roleRepository;
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;

    @Bean
    ApplicationRunner applicationRunner() {
        return args -> {
            List<String> roles = List.of(Role.ADMIN,Role.USER,Role.HOTEL);
            for (String roleType : roles) {
                if (!roleRepository.existsByName(roleType)) {
                    Role role = Role.builder()
                            .name(roleType)
                            .build();
                    roleRepository.save(role);
                }
            }

            final String ADMIN_USERNAME = "admin";
            final String ADMIN_PASSWORD = "admin";

            if (userRepository.findByUsername(ADMIN_USERNAME).isEmpty()) {
                User adminUser = User.builder()
                        .username(ADMIN_USERNAME)
                        .password(passwordEncoder.encode(ADMIN_PASSWORD))
                        .status(UserStatus.ACTIVE)
                        .build();

                Role role = roleRepository.findByName(Role.ADMIN).orElseThrow(() -> new NotFoundException("Role not found"));
                adminUser.setRole(role);

                userRepository.save(adminUser);
                log.warn("Admin user has been created with default password: admin");
            }
        };
    }
}
