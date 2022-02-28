package com.dptablo.straview.controller;

import com.dptablo.straview.ApplicationProperty;
import com.dptablo.straview.configuration.SecurityConfiguration;
import com.dptablo.straview.dto.entity.StravaOAuthTokenInfo;
import com.dptablo.straview.repository.StravaOAuthRepository;
import com.dptablo.straview.security.jwt.JwtRequestFilter;
import com.dptablo.straview.service.JwtAuthenticationService;
import com.dptablo.straview.service.StravaAuthenticationService;
import com.dptablo.straview.service.StravaOAuthService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = StravaAuthenticationController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfiguration.class)
        }
)
public class StravaAuthenticationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtAuthenticationService jwtAuthenticationService;

    @Mock
    private JwtRequestFilter jwtRequestFilter;

    @SpyBean
    private StravaOAuthService stravaOAuthService;

    @MockBean
    private StravaAuthenticationService stravaAuthenticationService;

    @MockBean
    private StravaOAuthRepository stravaOAuthRepository;

    @MockBean
    private ApplicationProperty applicationProperty;

    @Test
    @WithMockUser(roles = "USER")
    public void authenticate_success() throws Exception {
        String code = "1e0acc9278c9b3e430f658d775b715e8624247cc";

        StravaOAuthTokenInfo tokenInfo = StravaOAuthTokenInfo.builder().build();
        given(stravaAuthenticationService.newAuthenticate(code, "authorization_code")).willReturn(tokenInfo);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("state", "");
        params.add("code", code);
        params.add("scope", "read");

        mockMvc.perform(get("/api/auth/strava/authenticate").params(params))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/page/main"));

        verify(stravaOAuthService, times(1)).authenticate(code);
    }
}
