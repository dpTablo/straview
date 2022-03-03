package com.dptablo.straview.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import javax.servlet.ServletContext;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WebSecuritySpringBootIntegrationTest {
    @Autowired
    private Environment environment;

    @Autowired
    private ServletContext servletContext;

    @Test
    public void apiSubPath_thenForbidden() {
        String port = environment.getProperty("local.server.port");
        String contextPath = servletContext.getContextPath();
        String uri = String.format("http://localhost:%s%s/api/hello", port, contextPath);

        WebClient.ResponseSpec responseSpec = WebClient.create()
                .get()
                .uri(uri)
                .retrieve();

        try {
            responseSpec.toEntity(String.class).block();
        } catch(WebClientResponseException e) {
            assertThat(e.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        }

    }
}
