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
        User admin = new User();
        admin.setUsername("admin@ceylonr.com");
        admin.setPassword(passwordEncoder.encode("admin1234"));
        admin.setEnabled(true);
        admin.setRoles(List.of(Role.ROLE_ADMIN));
        userRepository.save(admin);

        User hotelManager = new User();
        hotelManager.setUsername("hotelmgr@ceylonr.com");
        hotelManager.setPassword(passwordEncoder.encode("alex1234"));
        hotelManager.setEnabled(true);
        hotelManager.setRoles(List.of(Role.ROLE_HOTEL_MANAGER));
        userRepository.save(hotelManager);

        User restaurantManager = new User();
        restaurantManager.setUsername("restaurantmgr@ceylonr.com");
        restaurantManager.setPassword(passwordEncoder.encode("restaurant1234"));
        restaurantManager.setEnabled(true);
        restaurantManager.setRoles(List.of(Role.ROLE_RESTAURANT_MANAGER));
        userRepository.save(restaurantManager);

        User chef = new User();
        chef.setUsername("chef@ceylonr.com");
        chef.setPassword(passwordEncoder.encode("chef1234"));
        chef.setEnabled(true);
        chef.setRoles(List.of(Role.ROLE_CHEF));
        userRepository.save(chef);

        User frontDesk = new User();
        frontDesk.setUsername("frontdesk@ceylonr.com");
        frontDesk.setPassword(passwordEncoder.encode("frontdesk1234"));
        frontDesk.setEnabled(true);
        frontDesk.setRoles(List.of(Role.ROLE_FRONT_DESK));
        userRepository.save(frontDesk);
    }

    public Mono<UserDetails> findByUsername(String username) {
        return Mono.fromCallable(() -> userRepository.findByUsername(username))
                .subscribeOn(jdbcScheduler)
                .map(user -> user.orElseThrow(() -> new UserNotFoundException("User "+ username +" not found")));
    }
}
