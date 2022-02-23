package com.dptablo.straview.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WebSecuritySpringBootIntegrationTest {
    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    public void apiSubPath_thenForbidden() {
        ResponseEntity<String> result = testRestTemplate.getForEntity("/api/hello", String.class);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }
}
