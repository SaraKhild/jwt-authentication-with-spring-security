package com.example.authentication_with_springboot.configuration.filter;

import com.example.authentication_with_springboot.configuration.security.CustomUserDetailsService;
import com.example.authentication_with_springboot.configuration.security.JwtService;
import com.example.authentication_with_springboot.exception.RestAuthenticationEntryPoint;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// Ensures the filter is executed once per request — not multiple times in a chain.
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    // A custom service to extract and validate JWT tokens.
    private final JwtService jwtService;

//    private static final String[] excludedEndpoints = new String[]{"/api/auth", "/api/public-resource"};

    // A service that loads user info from the database by email (or username).
    private final CustomUserDetailsService customUserDetailsService;

    private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    public JwtAuthenticationFilter(JwtService jwtService, CustomUserDetailsService customUserDetailsService, RestAuthenticationEntryPoint restAuthenticationEntryPoint) {
        this.jwtService = jwtService;
        this.customUserDetailsService = customUserDetailsService;
        this.restAuthenticationEntryPoint = restAuthenticationEntryPoint;
    }

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    // This is the main method that runs on every HTTP request.
    /*  Goal :
     * Extract the token from the Authorization header.
     * Validate the token.
     * Extract the username.
     * Load user details from the database.
     * Create an Authentication object.
     * Store it in the Spring Security context.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try {
            // Extract the token from the Authorization header.
            var token = jwtService.extractTokenFromRequest(request).get();
            logger.info("JWT Token: {}", token);

            if (jwtService.isBlacklisted(token)) {
                logger.warn("JWT is blacklisted");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                restAuthenticationEntryPoint.commence(
                        request,
                        response,
                        new BadCredentialsException("Please login to access this resource")

                );
                return;
            }
            // Validate token
            jwtService.validateToken(token);
            logger.info("Token is valid, proceeding to extract username...");

            // Extract username
            String username = jwtService.extractUsername(token);
            logger.info("Username:{}", username);

            // Loads user info from the database
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

            // This is Spring Security’s object to represent the authenticated user token
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null,
                    userDetails.getAuthorities());
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            // Stores the user authentication info in Spring’s SecurityContext and can access the resource.
            SecurityContextHolder.getContext().setAuthentication(authToken);
//          *****************************Another way********************************
//            SecurityContext context = SecurityContextHolder.createEmptyContext();
//            context.setAuthentication(authToken);
//            SecurityContextHolder.setContext(context);
        } catch (Exception e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
            restAuthenticationEntryPoint.commence(
                    request,
                    response,
                    new BadCredentialsException("Invalid or expired token, please try again")
            );
            return;
        }
        // Passes the request on to the next filter (or the controller).
        filterChain.doFilter(request, response);
    }

    /*
     * JwtDecoder:You're manually parsing and validating JWT tokens in method validateToken so
     * you're doing the decoding and validation manually, so there's no need for a JwtDecoder
     */

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {

        String path = request.getRequestURI();
        return path.startsWith("/api/auth") || path.startsWith("/api/public-resource");
        //*****************another way******************
//       return Arrays.stream(excludedEndpoints).anyMatch(excludedEndpoint -> request.getRequestURI().startsWith(excludedEndpoint));
    }

}