package com.dptablo.straview.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
class JwtAuthenticationControllerTest {
    @Autowired
    JwtAuthenticationController controller;

    @Test
    void authenticate() {
        ResponseEntity signUpResponseEntity = controller.signUp("user1", "1111");
        assertThat(signUpResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        assertThatNoException().isThrownBy(() -> {
            ResponseEntity responseEntity = controller.authenticate("user1", "1111");
            assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(responseEntity.getHeaders().get(HttpHeaders.WWW_AUTHENTICATE)).isNotEmpty();
        });
    }
}