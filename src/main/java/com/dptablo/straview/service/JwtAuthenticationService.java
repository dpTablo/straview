package com.dptablo.straview.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.dptablo.straview.ApplicationProperty;
import com.dptablo.straview.dto.User;
import com.dptablo.straview.dto.enumtype.Role;
import com.dptablo.straview.repository.UserRepository;
import com.dptablo.straview.security.StraviewUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationService {
    private final UserRepository userRepository;
    private final ApplicationProperty applicationProperty;

    public User signUp(String userId, String password) {
        Set<Role> roles = Stream.of(Role.USER).collect(Collectors.toCollection(HashSet::new));

        User user = User.builder()
                .userId(userId)
                .password(password)
                .roles(roles)
                .build();

        return userRepository.save(user);
    }

    public Optional<String> authenticate(String userId, String password) {
        try {
            User user = userRepository.findUserByUserIdAndPassword(userId, password).orElseThrow(NullPointerException::new);

            HashSet<GrantedAuthority> authoritySet = user.getRoles().stream()
                    .map(role -> new SimpleGrantedAuthority(role.toString()))
                    .collect(Collectors.toCollection(HashSet::new));

            UserDetails userDetails = createUserDetails(user, authoritySet);
            return Optional.of(createToken(userDetails));
        } catch(Exception e) {
            return Optional.empty();
        }
    }

    public boolean verifyToken(String token) throws JWTVerificationException {
        DecodedJWT decodedJWT = decodeToken(token);
        return decodedJWT.getToken().equals(token);
    }

    public Optional<Authentication> getAuthentication(String token) {
        try {
            DecodedJWT decodedJWT = decodeToken(token);

            User user = userRepository.findById(decodedJWT.getSubject()).orElseThrow(NullPointerException::new);
            Collection<GrantedAuthority> authorityCollection =
                    user.getRoles().stream()
                            .map(role -> new SimpleGrantedAuthority(role.toString()))
                            .collect(Collectors.toSet());

            return Optional.of(new UsernamePasswordAuthenticationToken(user.getUserId(), null, authorityCollection));
        } catch (Exception e) {
            log.error(e.getMessage());
            return Optional.empty();
        }
    }

    private String createToken(UserDetails userDetails) throws JWTCreationException {
        Algorithm algorithm = Algorithm.HMAC256(applicationProperty.getPrivateKey());
        return JWT.create()
                .withSubject(userDetails.getUsername())
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .withIssuer(applicationProperty.getIssUser())
                .withExpiresAt(new Date(
                    System.currentTimeMillis() + (1000 * 60 * applicationProperty.getJwtExpiryMinutes())
                ))
                .sign(algorithm);
    }

    private DecodedJWT decodeToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(applicationProperty.getPrivateKey());
        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer(applicationProperty.getIssUser())
                .build();
        return verifier.verify(token);
    }

    private UserDetails createUserDetails(User user, Collection<? extends GrantedAuthority> authorities) {
        return StraviewUserDetails.builder()
                .username(user.getUserId())
                .password(user.getPassword())
                .enable(true)
                .authorities(authorities)
                .build();
    }
}
