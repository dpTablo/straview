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

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {
    @InjectMocks
    AuthenticationService authenticationService;

    @Mock
    UserRepository userRepository;

    @Test
    void authenticate() {
        User user = User.builder()
                .userId("user1")
                .password("1111")
                .role(Role.ADMIN)
                .build();

        StraviewUserDetails userDetails = StraviewUserDetails.builder()
                .username(user.getUserId())
                .password(user.getPassword())
                .build();

        doReturn(Optional.of(user)).when(userRepository).findUserByUserIdAndPassword("user1", "1111");

        Optional<String> token = authenticationService.authenticate("user1", "1111");
        assertThat(token.isPresent()).isTrue();
    }

    @Test
    void verifyToken() {
        User user = User.builder()
                .userId("user1")
                .password("1111")
                .role(Role.ADMIN)
                .build();

        StraviewUserDetails userDetails = StraviewUserDetails.builder()
                .username(user.getUserId())
                .password(user.getPassword())
                .build();

        doReturn(Optional.of(user)).when(userRepository).findUserByUserIdAndPassword("user1", "1111");

        Optional<String> token = authenticationService.authenticate("user1", "1111");
        assertThat(token.isPresent()).isTrue();
        assertThatNoException().isThrownBy(() ->
                assertThat(authenticationService.verifyToken(userDetails, token.get())).isTrue());
    }

    @Test
    void signUp() {
        User user = User.builder()
                .userId("user1")
                .password("1111")
                .role(Role.USER)
                .build();

        doReturn(user).when(userRepository).save(any());

        User signUpUser = authenticationService.signUp(user.getUserId(), user.getPassword());
        verify(userRepository).save(any(User.class));

        assertThat(signUpUser.getUserId()).isEqualTo(user.getUserId());
        assertThat(signUpUser.getPassword()).isEqualTo(user.getPassword());
        assertThat(signUpUser.getRole().toString()).isEqualTo("USER");
    }
}