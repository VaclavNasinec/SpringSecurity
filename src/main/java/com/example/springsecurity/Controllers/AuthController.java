package com.example.springsecurity.Controllers;

import com.example.springsecurity.Dto.AuthResponseDTO;
import com.example.springsecurity.Dto.LoginDto;
import com.example.springsecurity.Dto.RegisterDto;
import com.example.springsecurity.Models.Role;
import com.example.springsecurity.Models.UserEntity;
import com.example.springsecurity.Repos.RoleRepo;
import com.example.springsecurity.Repos.UserRepo;
import com.example.springsecurity.Security.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private AuthenticationManager authenticationManager;
    private UserRepo userRepo;
    private RoleRepo roleRepo;
    private PasswordEncoder passwordEncoder;
   private TokenService tokenService;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, UserRepo userRepo, RoleRepo roleRepo, PasswordEncoder passwordEncoder, TokenService tokenService) {
        this.authenticationManager = authenticationManager;
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterDto registerDto) {
        if (userRepo.existsByUsername(registerDto.getUsername())) {
            return ResponseEntity.badRequest().body("Username is already taken");
        }

        UserEntity user = new UserEntity();
        user.setUsername(registerDto.getUsername());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));

        Role role = roleRepo.findByName("USER").get();
        user.setRoles(Collections.singletonList(role));

        userRepo.save(user);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = tokenService.generateToken(authentication);
        return ResponseEntity.ok(new AuthResponseDTO(token));
    }
}
