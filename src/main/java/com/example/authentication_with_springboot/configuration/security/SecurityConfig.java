package com.example.authentication_with_springboot.configuration.security;

import com.example.authentication_with_springboot.configuration.filter.JwtAuthenticationFilter;
import com.example.authentication_with_springboot.exception.RestAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    //The HttpSecurity object lets you configure: What requests are secure, Which ones are public, What filters to use
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                //Disables CSRF protection, which is okay for stateless REST APIs (no sessions). Note: is important for browsers, not APIs using tokens.
                .csrf(AbstractHttpConfigurer::disable)
                //Adds your custom JWT filter before the default username-password filter.
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                //Handles authentication errors
                .exceptionHandling(exceptionHandling ->
                        exceptionHandling
                                .authenticationEntryPoint(new RestAuthenticationEntryPoint())  // RestAuthenticationEntryPoint likely returns a JSON error like { "error": "Unauthorized" }.
                )
                .authorizeHttpRequests((auth) ->
                        auth
                                .requestMatchers(HttpMethod.GET, "api/public-resource").permitAll() //Anyone can access it
                                .requestMatchers("api/auth/**").permitAll() //  Everything else requires login (a valid token)
                                .anyRequest().authenticated()
                )
                //Tells Spring NOT to use HTTP sessions. Each request must have a valid JWT.
                .sessionManagement(manager -> manager
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        //Finalizes and builds the security filter chain.
        return http.build();
    }

    /*
   - Both of these methods are used to configure an AuthenticationManager in Spring Security,
   - Custom logic and more control
   - It delegates to the built-in Spring Boot auto-configuration,
    which registers your UserDetailsService, PasswordEncoder, and any AuthenticationProvider you've defined.
    */
    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {

        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        //to load user
        authenticationProvider.setUserDetailsService(userDetailsService);
        // to check the password
        authenticationProvider.setPasswordEncoder(passwordEncoder);

        return new ProviderManager(authenticationProvider);
    }
// ************************Another way by auto config***************************
//    @Bean
//    public AuthenticationManager authenticationManager(
//            final AuthenticationConfiguration authenticationConfiguration) throws Exception {
//
//        return authenticationConfiguration.getAuthenticationManager();
//    }

    /*
     - encode passwords.
     - Declares a BCrypt password encoder to hash and check passwords securely.
    */
    @Bean
    public static PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }

}