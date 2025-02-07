package com.project.AuthSystem.service;

import com.project.AuthSystem.DTO.LoginRequest;
import com.project.AuthSystem.DTO.LoginResponse;
import com.project.AuthSystem.DTO.SignupRequest;
import com.project.AuthSystem.config.exception.custom.AuthenticationException;
import com.project.AuthSystem.config.security.JwtTokenUtil;
import com.project.AuthSystem.model.Roles;
import com.project.AuthSystem.model.Users;
import com.project.AuthSystem.repository.RolesRepository;
import com.project.AuthSystem.repository.UsersRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;


@Service
@Slf4j
@AllArgsConstructor
public class AuthService {

    private AuthenticationManager authenticationManager;
    private JwtTokenUtil jwtTokenUtil;
    private PasswordEncoder passwordEncoder;
    private UsersRepository usersRepository;
    private RolesRepository rolesRepository;

    public LoginResponse login(LoginRequest loginRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
            LoginResponse response = new LoginResponse();
            response.setToken(jwtTokenUtil.generateToken(loginRequest.getEmail()));
            return response;
        } catch (Exception ex) {
            log.error("Error in login ", ex);
            throw new AuthenticationException(ex.getMessage());
        }
    }

    public String signup(SignupRequest signupRequest) {
        try {
            Users user = new Users();
            user.setUsername(signupRequest.getUsername());
            user.setEmail(signupRequest.getEmail());
            user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
            Set<Roles> roles = new HashSet<>();
            roles.add(rolesRepository.getByName("USER"));
            user.setRoles(roles);
            usersRepository.saveAndFlush(user);
            return "Signup Success";
        } catch (DataIntegrityViolationException ex) {
            log.info("This user already exist");
            log.error("DataIntegrityViolationException", ex);
            throw new AuthenticationException("This username already exist");
        } catch (Exception ex) {
            log.error("Error in signup ", ex);
            throw new RuntimeException();
        }
    }
}
