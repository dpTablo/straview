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

import java.time.Instant;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
    public void authenticate_reuseToken() throws Exception {
        //given
        StravaOAuthTokenInfo tokenInfo = StravaOAuthTokenInfo.builder()
                .tokenType("Bearer")
                .accessToken("kljfdsakljfdsaljk")
                .refreshToken("fgdlkjr2349823r4iuf34")
                .expiresAt(Instant.now().getEpochSecond())
                .expiresIn(21600L)
                .build();

        given(stravaOAuthRepository.findById(applicationProperty.getStravaClientAthleteId()))
                .willReturn(Optional.ofNullable(tokenInfo));
        given(applicationProperty.getStravaApiOAuth2Authorize()).willReturn("https://www.strava.com/oauth/mobile/authorize");
        given(applicationProperty.getStravaClientId()).willReturn(483489398);
        given(applicationProperty.getStravaAuthRedirectUrl()).willReturn("http://localhost:8080/straview/api/auth/strava/authenticate");

        //when & then
        mockMvc.perform(get("/api/auth/strava/authenticate"))
                .andExpect(status().isOk());

        verify(stravaOAuthService, times(1)).authenticate();
    }

    @Test
    @WithMockUser(roles = "USER")
    public void authenticate_OAuth2Authorize() throws Exception {
        //given
        given(stravaOAuthRepository.findById(applicationProperty.getStravaClientAthleteId())).willReturn(Optional.empty());
        given(applicationProperty.getStravaApiOAuth2Authorize()).willReturn("https://www.strava.com/oauth/mobile/authorize");
        given(applicationProperty.getStravaClientId()).willReturn(483489398);
        given(applicationProperty.getStravaAuthRedirectUrl()).willReturn("http://localhost:8080/straview/api/auth/strava/authenticate");

        //when & then
        mockMvc.perform(get("/api/auth/strava/authenticate"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void authenticate_tokenExchange() throws Exception {
        //given
        String code = "fdsjklkdfskljdsf";

        StravaOAuthTokenInfo tokenInfo = StravaOAuthTokenInfo.builder()
                .tokenType("Bearer")
                .accessToken("kljfdsakljfdsaljk")
                .refreshToken("fgdlkjr2349823r4iuf34")
                .expiresAt(Instant.now().getEpochSecond())
                .expiresIn(21600L)
                .build();

        given(stravaOAuthRepository.findById(applicationProperty.getStravaClientAthleteId())).willReturn(Optional.empty());
        given(applicationProperty.getStravaApiOAuth2Authorize()).willReturn("https://www.strava.com/oauth/mobile/authorize");
        given(applicationProperty.getStravaClientId()).willReturn(483489398);
        given(applicationProperty.getStravaAuthRedirectUrl()).willReturn("http://localhost:8080/straview/api/auth/strava/authenticate");
        given(applicationProperty.getStravaClientAthleteId()).willReturn(390930L);
        given(stravaAuthenticationService.newAuthenticate(code, "authorization_code")).willReturn(tokenInfo);

        //when & then
        MultiValueMap<String, String> paramsMap = new LinkedMultiValueMap<>();
        paramsMap.add("code", code);

        mockMvc.perform(get("/api/auth/strava/authenticate").params(paramsMap))
                .andExpect(status().isOk());

        verify(stravaOAuthService, times(1)).authenticate(code);
    }
}
