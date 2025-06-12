package com.example.authentication_with_springboot.configuration.security;

import com.example.authentication_with_springboot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    // Loads user info from the database  using UserDetailsService.
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return userRepository.findByUsername(username).map(user ->
                User.builder()
                        .username(username)
                        .password(user.getPassword())
                        .build()
        ).orElseThrow(() -> new UsernameNotFoundException(
                "User with username [%s] not found".formatted(username)));
    }

}