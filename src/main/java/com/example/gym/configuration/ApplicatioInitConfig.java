package com.example.gym.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.gym.entity.Role;
import com.example.gym.entity.User;
import com.example.gym.enums.RolePlay;
import com.example.gym.repository.UserRepository;

@Configuration
public class ApplicatioInitConfig {
    private UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    public ApplicatioInitConfig(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Bean
    ApplicationRunner applicationRunner() {
        return args -> {
            if (userRepository.findByPhoneNumber("admin").isEmpty()) {
                Role role = new Role();
                role.setId(2L);
                role.setName(RolePlay.ADMIN.name());
                User user = User.builder()
                        .phoneNumber("admin")
                        .password(passwordEncoder.encode("admin"))
                        .email("")
                        .role(role)
                        .active(true)
                        .build();
                userRepository.save(user);
            }
        };
    }
}
