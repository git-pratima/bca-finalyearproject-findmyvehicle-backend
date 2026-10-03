package com.pratima.bca.findmyvehicle.security;

import com.pratima.bca.findmyvehicle.repository.UserRepository;
import com.pratima.bca.findmyvehicle.util.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserActivityFilter extends OncePerRequestFilter {

    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        try {
            String authHeader = request.getHeader("Authorization");

            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                String email = jwtUtils.getUsernameFromToken(token);  // your existing method

                if (email != null) {
                    // ✅ Update last active timestamp
                    userRepository.updateLastActive(email, LocalDateTime.now());
                    log.debug("Updated last active for: {}", email);
                }
            }
        } catch (Exception e) {
            log.debug("Could not update last active: {}", e.getMessage());
        }

        filterChain.doFilter(request, response);
    }
}