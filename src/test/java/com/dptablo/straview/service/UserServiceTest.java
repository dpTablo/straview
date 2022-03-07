package com.dptablo.straview.service;

import com.dptablo.straview.dto.entity.User;
import com.dptablo.straview.dto.enumtype.Role;
import com.dptablo.straview.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.HashSet;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Test
    void findByUserId() {
        HashSet<Role> roles = new HashSet<>();
        roles.add(Role.ADMIN);

        User user = User.builder()
                .userId("dptablo")
                .password("1234")
                .roles(roles)
                .build();

        when(userRepository.findById(user.getUserId())).thenReturn(Optional.ofNullable(user));
        assertThat(userService.findByUserId(user.getUserId())).isEqualTo(user);

        when(userRepository.findById(user.getUserId())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> userService.findByUserId(user.getUserId())).isInstanceOf(NullPointerException.class);
    }
}