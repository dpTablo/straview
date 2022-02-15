package com.dptablo.straview.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.dptablo.straview.dto.User;
import com.dptablo.straview.dto.enumtype.Role;
import com.dptablo.straview.repository.UserRepository;
import com.dptablo.straview.security.StraviewUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

@Service
@PropertySource("classpath:app.properties")
@RequiredArgsConstructor
public class AuthenticationService {
    @Value("${jwt.privateKey:dptablo_straview}")
    private String PRIVATE_KEY = "dptablo_straview";

    @Value("${jwt.expiryMinutes:60}")
    private long JWT_TOKEN_EXPIRY_MINUTES = 60;

    @Value("${jwt.issuer:dptablo:straview}")
    private String ISSUER = "dptablo:straview";

    private UserRepository userRepository;

    @Autowired
    public AuthenticationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User signUp(String userId, String password) {
        User user = User.builder()
                .userId(userId)
                .password(password)
                .role(Role.USER)
                .build();

        return userRepository.save(user);
    }

    public Optional<String> authenticate(String userId, String password) {
        try {
            User user = userRepository.findUserByUserIdAndPassword(userId, password).orElseThrow(NullPointerException::new);

            GrantedAuthority authority = new SimpleGrantedAuthority(user.getRole().toString());
            StraviewUserDetails userDetails = StraviewUserDetails.builder()
                    .username(user.getUserId())
                    .password(user.getPassword())
                    .enable(true)
                    .authorities(Arrays.asList(authority))
                    .build();

            return Optional.of(createToken(userDetails));
        } catch(Exception e) {
            return Optional.empty();
        }
    }

    public boolean verifyToken(UserDetails userDetails, String token) throws JWTVerificationException {
        Algorithm algorithm = Algorithm.HMAC256(PRIVATE_KEY);
        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer(ISSUER)
                .build();
        DecodedJWT decodedJWT = verifier.verify(token);

        if(decodedJWT.getSubject().equals(userDetails.getUsername())
                && decodedJWT.getToken().equals(token)
        ) {
            return true;
        } else {
            return false;
        }
    }

    private String createToken(UserDetails userDetails) throws JWTCreationException {
        Algorithm algorithm = Algorithm.HMAC256(PRIVATE_KEY);
        return JWT.create()
                .withSubject(userDetails.getUsername())
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .withIssuer(ISSUER)
                .withExpiresAt(new Date(
                    System.currentTimeMillis() + (1000 * 60 * JWT_TOKEN_EXPIRY_MINUTES)
                ))
                .sign(algorithm);
    }
}
