package com.dptablo.straview.service;

import com.dptablo.straview.ApplicationProperty;
import com.dptablo.straview.dto.entity.StravaOAuthTokenInfo;
import com.dptablo.straview.exception.AuthenticationException;
import com.dptablo.straview.exception.StraviewErrorCode;
import com.dptablo.straview.repository.StravaOAuthRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;

import java.util.HashMap;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.Matchers.matchesPattern;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RestClientTest(StravaAuthenticationService.class)
@AutoConfigureWebClient(registerRestTemplate = true)
public class StravaAuthenticationServiceTest {
    @Autowired
    private StravaAuthenticationService service;

    @Autowired
    private MockRestServiceServer mockServer;

    @MockBean
    private ApplicationProperty applicationProperty;

    @MockBean
    private StravaOAuthRepository stravaOAuthRepository;

    @Test
    public void onError_INVALID_STRAVA_CLIENT_SECRET() {
        when(applicationProperty.getStravaApiOAuth2Token()).thenReturn("https://www.strava.com/api/v3/oauth/token");

        mockServer.expect(
                ExpectedCount.once(),
                requestTo(matchesPattern("https://www.strava.com/api/v3/oauth/.*")))
                    .andRespond(withSuccess("", MediaType.APPLICATION_JSON)
        );

        AuthenticationException exception =
                catchThrowableOfType(() -> service.newAuthenticate("", ""), AuthenticationException.class);

        assertThat(exception.getErrorCode()).isEqualTo(StraviewErrorCode.INVALID_STRAVA_CLIENT_SECRET);
    }

    @Test
    public void newAuthenticate() throws JsonProcessingException, AuthenticationException {
        when(applicationProperty.getStravaApiOAuth2Token()).thenReturn("https://www.strava.com/api/v3/oauth/token");

        String code = "abcdefg";
        String grantType = "authorization_code";

        HashMap<String, Object> athleteMap = new HashMap<>();
        athleteMap.put("id", 1234567890987654321L);
        athleteMap.put("username", "marianne_t");

        HashMap<String, Object> jsonResultMap = new HashMap<>();
        jsonResultMap.put("token_type", "Bearer");
        jsonResultMap.put("expires_at", 1568775134);
        jsonResultMap.put("expires_in", 21600);
        jsonResultMap.put("refresh_token", "e5n567567...");
        jsonResultMap.put("access_token", "a4b945687g...");
        jsonResultMap.put("athlete", athleteMap);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResult = objectMapper.writeValueAsString(jsonResultMap);

        mockServer.expect(
                ExpectedCount.once(),
                requestTo(matchesPattern("https://www.strava.com/api/v3/oauth/.*")))
                    .andRespond(withSuccess(jsonResult, MediaType.APPLICATION_JSON)
        );

        StravaOAuthTokenInfo tokenInfo = service.newAuthenticate(code, grantType);
        assertThat(tokenInfo.getTokenType()).isEqualTo("Bearer");
        assertThat(tokenInfo.getExpiresAt()).isEqualTo(1568775134);
        assertThat(tokenInfo.getExpiresIn()).isEqualTo(21600);
        assertThat(tokenInfo.getRefreshToken()).isEqualTo("e5n567567...");
        assertThat(tokenInfo.getAccessToken()).isEqualTo("a4b945687g...");
        assertThat(tokenInfo.getAthlete().getId()).isEqualTo(1234567890987654321L);
        assertThat(tokenInfo.getAthlete().getUserName()).isEqualTo("marianne_t");
    }

    @Test
    public void refreshAuthenticate() throws JsonProcessingException, AuthenticationException {
        StravaOAuthTokenInfo tokenInfo = StravaOAuthTokenInfo.builder()
                .tokenType("Bearer")
                .expiresAt(1568775134L)
                .expiresAt(21600L)
                .refreshToken("e5n567567...")
                .accessToken("a4b945687g...")
                .build();

        StravaOAuthTokenInfo newTokenInfo = StravaOAuthTokenInfo.builder()
                .tokenType("Bearer")
                .expiresAt(1668775134L)
                .expiresAt(3600L)
                .refreshToken("fkfkdlk49943")
                .accessToken("kdfklfgd94")
                .build();

        when(applicationProperty.getStravaApiOAuth2Token()).thenReturn("https://www.strava.com/api/v3/oauth/token");
        when(applicationProperty.getStravaClientAthleteId()).thenReturn(1435834L);
        when(stravaOAuthRepository.findById(1435834L)).thenReturn(Optional.of(tokenInfo));

        HashMap<String, Object> jsonResultMap = new HashMap<>();
        jsonResultMap.put("token_type", newTokenInfo.getTokenType());
        jsonResultMap.put("expires_at", newTokenInfo.getExpiresAt());
        jsonResultMap.put("expires_in", newTokenInfo.getExpiresIn());
        jsonResultMap.put("refresh_token", newTokenInfo.getRefreshToken());
        jsonResultMap.put("access_token", newTokenInfo.getAccessToken());

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResult = objectMapper.writeValueAsString(jsonResultMap);

        mockServer.expect(
                ExpectedCount.once(),
                requestTo(matchesPattern("https://www.strava.com/api/v3/oauth/.*")))
                    .andRespond(withSuccess(jsonResult, MediaType.APPLICATION_JSON)
        );

        StravaOAuthTokenInfo returnedTokenInfo = service.refreshAuthenticate(StravaOAuthService.REFRESH_TOKEN_GRANT_TYPE);
        assertThat(returnedTokenInfo.getTokenType()).isEqualTo(newTokenInfo.getTokenType());
        assertThat(returnedTokenInfo.getAccessToken()).isEqualTo(newTokenInfo.getAccessToken());
        assertThat(returnedTokenInfo.getRefreshToken()).isEqualTo(newTokenInfo.getRefreshToken());
        assertThat(returnedTokenInfo.getExpiresAt()).isEqualTo(newTokenInfo.getExpiresAt());
        assertThat(returnedTokenInfo.getExpiresIn()).isEqualTo(newTokenInfo.getExpiresIn());
    }

}