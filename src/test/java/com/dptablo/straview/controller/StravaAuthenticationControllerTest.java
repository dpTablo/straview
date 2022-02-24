package com.dptablo.straview.controller;

import com.dptablo.straview.security.jwt.JwtAccessDeniedHandler;
import com.dptablo.straview.security.jwt.JwtAuthenticationEntryPoint;
import com.dptablo.straview.security.jwt.JwtRequestFilter;
import com.dptablo.straview.service.StravaAuthenticationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StravaAuthenticationController.class)
@AutoConfigureMockMvc(addFilters = false)
public class StravaAuthenticationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtRequestFilter jwtRequestFilter;

    @MockBean
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @MockBean
    private JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @MockBean
    private StravaAuthenticationService stravaAuthenticationService;

    @Test
    @WithMockUser(username = "user1", authorities = "USER", roles = "USER")
    public void authenticate() throws Exception {
        // http://localhost:8080/straview/page/home?state=&code=cb736b746c21c959955f0548720cd0c1334f5b44&scope=read
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("state", "");
        params.add("code", "cb736b746c21c959955f0548720cd0c1334f5b44");
        params.add("scope", "read");

        mockMvc.perform(get("/api/auth/strava/authenticate").params(params))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/page/main"));
    }
}