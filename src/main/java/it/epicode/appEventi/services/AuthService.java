package it.epicode.appEventi.services;

import it.epicode.appEventi.dto.AuthResponse;
import it.epicode.appEventi.dto.LoginRequest;
import it.epicode.appEventi.exceptions.EmailAlreadyExistsException;
import it.epicode.appEventi.models.User;
import it.epicode.appEventi.payloads.RegisterRequest;
import it.epicode.appEventi.repositories.UserRepository;
import it.epicode.appEventi.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    public void register(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException("Email già registrata");
        }

        User user = new User();
        user.setName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());
        userRepository.save(user);
    }


    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        String token = jwtUtils.generateToken(user);
        return new AuthResponse(token);
    }
}





