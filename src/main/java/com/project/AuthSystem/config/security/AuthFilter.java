package com.project.AuthSystem.config.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Service
@Slf4j
@AllArgsConstructor
public class AuthFilter extends OncePerRequestFilter {

    private JwtTokenUtil jwtTokenUtil;
    private UserDetailsServiceImp userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try {
            final String authHeader = request.getHeader("Authorization");
            if (authHeader != null && authHeader.startsWith("Bearer ") && authHeader.length() > 7) {
                final String token = authHeader.substring(7);
                if (jwtTokenUtil.validateToken(token)) {
                    String email = jwtTokenUtil.getUserNameFromToken(token);
                    UserDetails userDetails = userDetailsService.loadUserByUsername(email);
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    authenticationToken.eraseCredentials();  // This ensures any sensitive data (password) is cleared.
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                } else {
                    log.info("Invalid token");
                    SecurityContextHolder.clearContext();
                }
            } else {
                log.info("User didn't have token");
                SecurityContextHolder.clearContext();
            }
        } catch (UsernameNotFoundException ex) {
            log.error("UserName not found exception", ex);
            SecurityContextHolder.clearContext();
            throw new UsernameNotFoundException("Invalid username or password");
        } catch (Exception ex) {
            log.error("Unexpected exception in the filter", ex);
            SecurityContextHolder.clearContext();
        }
        filterChain.doFilter(request, response);
    }
}
