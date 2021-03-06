package com.dptablo.straview.service;

import com.dptablo.straview.ApplicationProperty;
import com.dptablo.straview.dto.entity.User;
import com.dptablo.straview.dto.enumtype.Role;
import com.dptablo.straview.repository.UserRepository;
import com.dptablo.straview.security.StraviewUserDetails;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class JwtAuthenticationServiceTest {
    @InjectMocks
    private JwtAuthenticationService jwtAuthenticationService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ApplicationProperty applicationProperty;

    @Spy
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Test
    public void authenticate() {
        when(applicationProperty.getPrivateKey()).thenReturn("dptablo_straview");
        when(applicationProperty.getIssUser()).thenReturn("dptablo:straview");
        when(applicationProperty.getJwtExpiryMinutes()).thenReturn(60L);

        User user = User.builder()
                .userId("user1")
                .password("$2a$10$ETJhCQBaE4Tsmd63zwQBLeUeerH5DFDChGbswHo0HTuU98elAoQRi") //1234
                .build();

        StraviewUserDetails userDetails = StraviewUserDetails.builder()
                .username(user.getUserId())
                .password(user.getPassword())
                .build();

        doReturn(Optional.of(user)).when(userRepository).findById(user.getUserId());

        Optional<String> token = jwtAuthenticationService.authenticate("user1", "1234");
        assertThat(token.isPresent()).isTrue();

        Optional<String> token2 = jwtAuthenticationService.authenticate("user1", "3456");
        assertThat(token2.isPresent()).isFalse();
    }

    @Test
    public void verifyToken() {
        when(applicationProperty.getPrivateKey()).thenReturn("dptablo_straview");
        when(applicationProperty.getIssUser()).thenReturn("dptablo:straview");
        when(applicationProperty.getJwtExpiryMinutes()).thenReturn(60L);

        User user = User.builder()
                .userId("user1")
                .password("$2a$10$ETJhCQBaE4Tsmd63zwQBLeUeerH5DFDChGbswHo0HTuU98elAoQRi")
                .build();

        doReturn(Optional.of(user)).when(userRepository).findById(user.getUserId());

        Optional<String> token = jwtAuthenticationService.authenticate("user1", "1111");
        assertThat(token.isPresent()).isTrue();
        assertThatNoException().isThrownBy(() ->
                assertThat(jwtAuthenticationService.verifyToken(token.get())).isTrue());
    }

    @Test
    public void signUp() {
        Set<Role> roles = Stream.of(Role.USER).collect(Collectors.toCollection(HashSet::new));
        User user = User.builder()
                .userId("user1")
                .password("$2a$10$ETJhCQBaE4Tsmd63zwQBLeUeerH5DFDChGbswHo0HTuU98elAoQRi") //1234
                .roles(roles)
                .build();

        doReturn(user).when(userRepository).save(any());

        User signUpUser = jwtAuthenticationService.signUp(user.getUserId(), user.getPassword());
        verify(userRepository).save(any(User.class));

        assertThat(signUpUser.getUserId()).isEqualTo(user.getUserId());
        assertThat(signUpUser.getPassword()).isEqualTo(user.getPassword());
        assertThat(signUpUser.getRoles()).contains(Role.USER);
    }

    @Test
    public void getAuthentication() {
        when(applicationProperty.getPrivateKey()).thenReturn("dptablo_straview");
        when(applicationProperty.getIssUser()).thenReturn("dptablo:straview");
        when(applicationProperty.getJwtExpiryMinutes()).thenReturn(60L);

        Set<Role> roles = Stream.of(Role.USER).collect(Collectors.toCollection(HashSet::new));
        User user = User.builder()
                .userId("user1")
                .password("$2a$10$ETJhCQBaE4Tsmd63zwQBLeUeerH5DFDChGbswHo0HTuU98elAoQRi") //1234
                .roles(roles)
                .build();

        doReturn(Optional.of(user)).when(userRepository).findById(user.getUserId());
        doReturn(Optional.of(user)).when(userRepository).findById(user.getUserId());

        Optional<String> token = jwtAuthenticationService.authenticate(user.getUserId(), user.getPassword());
        assertThat(token.isPresent()).isTrue();

        assertThatNoException().isThrownBy(() -> {
            Authentication authentication = jwtAuthenticationService.getAuthentication(token.get()).orElseThrow(NullPointerException::new);
            assertThat(authentication.getName()).isEqualTo("user1");
        });
    }
}