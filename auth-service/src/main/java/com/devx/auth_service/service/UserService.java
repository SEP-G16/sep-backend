package com.devx.auth_service.service;

import com.devx.auth_service.enums.Role;
import com.devx.auth_service.exception.UserNotFoundException;
import com.devx.auth_service.model.User;
import com.devx.auth_service.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

import java.util.List;

@Service
public class UserService implements ReactiveUserDetailsService {


    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final Scheduler jdbcScheduler;

    @Autowired
    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository, @Qualifier("jdbcScheduler") Scheduler jdbcScheduler) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.jdbcScheduler = jdbcScheduler;
    }

    @PostConstruct
    public void init() {
        User user = new User();
        user.setUsername("alex@example.com");
        user.setPassword(passwordEncoder.encode("alex1234"));
        user.setEnabled(true);
        user.setRoles(List.of(Role.ROLE_HOTEL_MANAGER));
        userRepository.save(user);

        User admin = new User();
        admin.setUsername("admin@example.com");
        admin.setPassword(passwordEncoder.encode("Admin000#"));
        admin.setEnabled(true);
        admin.setRoles(List.of(Role.ROLE_ADMIN));
        userRepository.save(admin);
    }

    public Mono<UserDetails> findByUsername(String username) {
        return Mono.fromCallable(() -> userRepository.findByUsername(username))
                .subscribeOn(jdbcScheduler)
                .map(user -> user.orElseThrow(() -> new UserNotFoundException("User "+ username +" not found")));
    }
}
