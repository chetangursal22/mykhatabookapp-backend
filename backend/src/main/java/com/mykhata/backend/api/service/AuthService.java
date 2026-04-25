package com.mykhata.backend.api.service;

import com.mykhata.backend.api.dtos.AuthResponse;
import com.mykhata.backend.api.dtos.AuthTypeRequest;
import com.mykhata.backend.api.repository.UserRepository;
import com.mykhata.backend.entity.Users;
import com.mykhata.backend.utilities.JwtServices;
import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final JwtServices jwtServices;

    public AuthService(UserRepository userRepository, JwtServices jwtServices) {
        this.userRepository = userRepository;
        this.jwtServices = jwtServices;
    }

    public Optional<AuthResponse> authenticate(AuthTypeRequest request) {
        Optional<Users> userOptional = userRepository.findByEmail(request.getEmail());

        if (userOptional.isPresent()) {
            Users user = userOptional.get();

            Map<String, Object> claims = new HashMap<>();
            claims.put("role", user.getRole());

            String jwt = jwtServices.generateToken(claims, user.getEmail());
            
            return Optional.of(new AuthResponse(jwt, user.getRole()));
        }
        return Optional.empty();
    }

    public Cookie createJwtCookie(String jwt) {
        Cookie cookie = new Cookie("jwt", jwt);
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // Set to true if using HTTPS
        cookie.setPath("/");
        cookie.setMaxAge((int) (jwtServices.getExpirationTime() / 1000));
        return cookie;
    }
}
