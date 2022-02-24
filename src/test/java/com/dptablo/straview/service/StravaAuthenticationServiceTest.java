package com.dptablo.straview.service;

import com.dptablo.straview.ApplicationProperty;
import com.dptablo.straview.dto.entity.StravaOAuthTokenInfo;
import com.dptablo.straview.exception.AuthenticationException;
import com.dptablo.straview.exception.StraviewErrorCode;
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

    @Test
    public void onError_INVALID_STRAVA_CLIENT_SECRET() {
        when(applicationProperty.getStravaApiOAuth2Token()).thenReturn("https://www.strava.com/api/v3/oauth/token");

        mockServer.expect(
                ExpectedCount.once(),
                requestTo(matchesPattern("https://www.strava.com/api/v3/oauth/.*")))
                    .andRespond(withSuccess("", MediaType.APPLICATION_JSON)
        );

        AuthenticationException exception =
                catchThrowableOfType(() -> service.authenticate("", ""), AuthenticationException.class);

        assertThat(exception.getErrorCode()).isEqualTo(StraviewErrorCode.INVALID_STRAVA_CLIENT_SECRET);
    }

    @Test
    public void authenticate() throws JsonProcessingException, AuthenticationException {
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

        StravaOAuthTokenInfo tokenInfo = service.authenticate(code, grantType);
        assertThat(tokenInfo.getTokenType()).isEqualTo("Bearer");
        assertThat(tokenInfo.getExpiresAt()).isEqualTo(1568775134);
        assertThat(tokenInfo.getExpiresIn()).isEqualTo(21600);
        assertThat(tokenInfo.getRefreshToken()).isEqualTo("e5n567567...");
        assertThat(tokenInfo.getAccessToken()).isEqualTo("a4b945687g...");
        assertThat(tokenInfo.getAthlete().getId()).isEqualTo(1234567890987654321L);
        assertThat(tokenInfo.getAthlete().getUserName()).isEqualTo("marianne_t");
    }

}