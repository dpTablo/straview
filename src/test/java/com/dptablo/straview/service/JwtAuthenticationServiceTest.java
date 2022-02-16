package com.dptablo.straview.service;

import com.dptablo.straview.dto.User;
import com.dptablo.straview.dto.enumtype.Role;
import com.dptablo.straview.repository.UserRepository;
import com.dptablo.straview.security.StraviewUserDetails;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationServiceTest {
    @InjectMocks
    JwtAuthenticationService jwtAuthenticationService;

    @Mock
    UserRepository userRepository;

    @Test
    void authenticate() {
        User user = User.builder()
                .userId("user1")
                .password("1111")
                .build();

        StraviewUserDetails userDetails = StraviewUserDetails.builder()
                .username(user.getUserId())
                .password(user.getPassword())
                .build();

        doReturn(Optional.of(user)).when(userRepository).findUserByUserIdAndPassword("user1", "1111");

        Optional<String> token = jwtAuthenticationService.authenticate("user1", "1111");
        assertThat(token.isPresent()).isTrue();
    }

    @Test
    void verifyToken() {
        User user = User.builder()
                .userId("user1")
                .password("1111")
                .build();

        doReturn(Optional.of(user)).when(userRepository).findUserByUserIdAndPassword("user1", "1111");

        Optional<String> token = jwtAuthenticationService.authenticate("user1", "1111");
        assertThat(token.isPresent()).isTrue();
        assertThatNoException().isThrownBy(() ->
                assertThat(jwtAuthenticationService.verifyToken(token.get())).isTrue());
    }

    @Test
    void signUp() {
        Set<Role> roles = Stream.of(Role.USER).collect(Collectors.toCollection(HashSet::new));
        User user = User.builder()
                .userId("user1")
                .password("1111")
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
    void getAuthentication() {
        Set<Role> roles = Stream.of(Role.USER).collect(Collectors.toCollection(HashSet::new));
        User user = User.builder()
                .userId("user1")
                .password("1111")
                .roles(roles)
                .build();

        doReturn(Optional.of(user)).when(userRepository).findById(user.getUserId());
        doReturn(Optional.of(user)).when(userRepository).findUserByUserIdAndPassword(user.getUserId(), user.getPassword());

        Optional<String> token = jwtAuthenticationService.authenticate(user.getUserId(), user.getPassword());
        assertThat(token.isPresent()).isTrue();

        assertThatNoException().isThrownBy(() -> {
            Authentication authentication = jwtAuthenticationService.getAuthentication(token.get()).orElseThrow(NullPointerException::new);
            assertThat(authentication.getName()).isEqualTo("user1");
        });
    }
}