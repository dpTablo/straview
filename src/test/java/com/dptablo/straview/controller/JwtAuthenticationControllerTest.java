package com.dptablo.straview.controller;

import com.dptablo.straview.security.jwt.JwtAccessDeniedHandler;
import com.dptablo.straview.security.jwt.JwtAuthenticationEntryPoint;
import com.dptablo.straview.service.JwtAuthenticationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(JwtAuthenticationController.class)
@AutoConfigureMockMvc(addFilters = false)
public class JwtAuthenticationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @MockBean
    private JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @MockBean
    private JwtAuthenticationService jwtAuthenticationService;

    @Test
    public void authenticate() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("userId", "user1");
        params.add("password", "1111");

        doReturn(Optional.of("token_abcdefg")).when(jwtAuthenticationService).authenticate(anyString(), anyString());
        mockMvc.perform(post("/api/auth/authenticate").params(params))
                .andExpect(status().isOk())
                .andExpect(header().exists(HttpHeaders.WWW_AUTHENTICATE));
    }

    @Test
    public void authenticate_emptyToken() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("userId", "user1");
        params.add("password", "1111");

        doReturn(Optional.empty()).when(jwtAuthenticationService).authenticate(anyString(), anyString());
        mockMvc.perform(post("/api/auth/authenticate").params(params))
                .andExpect(status().is5xxServerError());
    }

}