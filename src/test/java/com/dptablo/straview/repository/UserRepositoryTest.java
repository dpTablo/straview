package com.dptablo.straview.repository;

import com.dptablo.straview.dto.User;
import com.dptablo.straview.dto.enumtype.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;

    @Test
    void save() {
        HashSet<Role> roles = Stream.of(Role.ADMIN, Role.USER).collect(Collectors.toCollection(HashSet::new));

        User user = User.builder()
                .userId("user1")
                .password("1111")
                .roles(roles)
                .build();

        assertThatCode(() -> userRepository.save(user)).doesNotThrowAnyException();

        Optional<User> user2 = userRepository.findById(user.getUserId());
        assertThat(user2.isPresent()).isTrue();
    }

    @Test
    void findUserByUserIdAndPassword() {
        User user = User.builder()
                .userId("user1")
                .password("1111")
                .build();

        assertThatCode(() -> userRepository.save(user)).doesNotThrowAnyException();

        Optional<User> user1 = userRepository.findUserByUserIdAndPassword("user1","1111");
        assertThat(user1.isPresent()).isTrue();

        Optional<User> userNotMatchedPassword = userRepository.findUserByUserIdAndPassword("user1","xxxx");
        assertThat(userNotMatchedPassword.isPresent()).isFalse();

        Optional<User> noUser = userRepository.findUserByUserIdAndPassword("user222","xxxx");
        assertThat(noUser.isPresent()).isFalse();
    }

}