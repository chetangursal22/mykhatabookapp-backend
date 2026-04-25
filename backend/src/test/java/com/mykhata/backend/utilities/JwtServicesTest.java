package com.mykhata.backend.utilities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class JwtServicesTest {

    private JwtServices jwtServices;
    private final String secretKey = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";
    private final long expiration = 300000; // 5 minutes

    @BeforeEach
    void setUp() {
        jwtServices = new JwtServices();
        ReflectionTestUtils.setField(jwtServices, "secretKey", secretKey);
        ReflectionTestUtils.setField(jwtServices, "jwtExpiration", expiration);
    }

    @Test
    void testGenerateToken() {
        String username = "testUser";
        String token = jwtServices.generateToken(username);
        assertNotNull(token);
        assertEquals(username, jwtServices.extractUsername(token));
    }

    @Test
    void testGenerateTokenWithClaims() {
        String username = "testUser";
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", "ADMIN");
        
        String token = jwtServices.generateToken(claims, username);
        assertNotNull(token);
        assertEquals(username, jwtServices.extractUsername(token));
        assertEquals("ADMIN", jwtServices.extractClaim(token, c -> c.get("role")));
    }

    @Test
    void testIsTokenValid() {
        String username = "testUser";
        String token = jwtServices.generateToken(username);
        assertTrue(jwtServices.isTokenValid(token, username));
        assertFalse(jwtServices.isTokenValid(token, "wrongUser"));
    }
}
