package com.abchawla.mapp.controllers;
import com.abchawla.mapp.model.User;
import com.abchawla.mapp.repo.UserRepository;
import com.abchawla.mapp.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;

    public AuthController(UserRepository userRepo,
                          PasswordEncoder pe,
                          AuthenticationManager am,
                          JwtUtil ju) {
        this.userRepo = userRepo;
        this.passwordEncoder = pe;
        this.authManager = am;
        this.jwtUtil = ju;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String username = body.get("username");
        String password = body.get("password");
        if (email == null || username == null || password == null) {
            return ResponseEntity.badRequest().body("Missing fields");
        }
        // Check uniqueness
        if (userRepo.findByEmail(email).isPresent()) {
            return ResponseEntity.badRequest().body("Email already in use");
        }
        if (userRepo.findByUsername(username).isPresent()) {
            return ResponseEntity.badRequest().body("Username already in use");
        }
        String hash = passwordEncoder.encode(password);
        User user = new User(null, email, username, hash);
        userRepo.save(user);
        return ResponseEntity.ok(Map.of("message", "User created"));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String usernameOrEmail = body.get("usernameOrEmail");
        String password = body.get("password");
        if (usernameOrEmail == null || password == null) {
            return ResponseEntity.badRequest().body("Missing fields");
        }
        try {
            authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(usernameOrEmail, password));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
        // Generate token
        // First, load the user details to get the actual username
        String actualUsername = userRepo.findByUsername(usernameOrEmail)
                .or(() -> userRepo.findByEmail(usernameOrEmail))
                .map(User::getUsername)
                .orElseThrow();

        String token = jwtUtil.generateToken(actualUsername);
        return ResponseEntity.ok(Map.of("token", token, "username", actualUsername));
    }
}
