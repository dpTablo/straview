package com.dptablo.straview.service;

import com.dptablo.straview.ApplicationProperty;
import com.dptablo.straview.dto.entity.StravaOAuthTokenInfo;
import com.dptablo.straview.exception.AuthenticationException;
import com.dptablo.straview.repository.StravaOAuthRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StravaOAuthServiceTest {
    @InjectMocks
    private StravaOAuthService stravaOAuthService;

    @Mock
    private StravaAuthenticationService stravaAuthenticationService;

    @Spy
    private StravaOAuthRepository stravaOAuthRepository;

    @Mock
    private ApplicationProperty applicationProperty;

    @Test
    public void authenticate_code_newToken() throws AuthenticationException {
        //given
        String code = "1e0acc9278c9b3e430f658d775b715e8624247cc";
        long athleteId = 12345L;

        StravaOAuthTokenInfo newTokenInfo = StravaOAuthTokenInfo.builder()
                .athleteId(athleteId)
                .tokenType("Bearer")
                .accessToken("dljksdjklsdfljklsd")
                .build();

        given(applicationProperty.getStravaClientAthleteId()).willReturn(athleteId);
        given(stravaAuthenticationService.newAuthenticate(code, "authorization_code")).willReturn(newTokenInfo);
        given(stravaOAuthRepository.findById(athleteId)).willReturn(Optional.empty());
        given(stravaOAuthRepository.save(newTokenInfo)).willReturn(newTokenInfo);

        //when
        StravaOAuthTokenInfo returnedTokenInfo = stravaOAuthService.authenticate(code);

        //then
        assertThat(returnedTokenInfo).isEqualTo(newTokenInfo);
        verify(stravaOAuthRepository, times(1)).save(newTokenInfo);
    }

    @Test
    public void authenticate_code_refreshToken() throws AuthenticationException {
        //given
        String code = "1e0acc9278c9b3e430f658d775b715e8624247cc";
        long athleteId = 12345L;

        Instant instant = Instant.parse("2000-01-01T00:00:00.123Z");
        ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(instant, ZoneId.of("Asia/Seoul"));
        long expiresAt = zonedDateTime.toEpochSecond();

        instant = Instant.now();
        zonedDateTime = ZonedDateTime.ofInstant(instant, ZoneId.of("Asia/Seoul"));
        long freshExpiresAt = zonedDateTime.toEpochSecond();

        StravaOAuthTokenInfo tokenInfo = StravaOAuthTokenInfo.builder()
                .athleteId(athleteId)
                .tokenType("Bearer")
                .accessToken("aa")
                .expiresAt(expiresAt)
                .expiresIn(1L)
                .refreshToken("aa11aa11")
                .build();

        StravaOAuthTokenInfo refreshTokenInfo = StravaOAuthTokenInfo.builder()
                .athleteId(athleteId)
                .tokenType("Bearer")
                .accessToken("bb")
                .expiresAt(freshExpiresAt)
                .expiresIn(99999999999L)
                .refreshToken("bb22bb22")
                .build();

        given(applicationProperty.getStravaClientTimeZone()).willReturn("Asia/Seoul");
        given(applicationProperty.getStravaClientAthleteId()).willReturn(athleteId);
        given(stravaOAuthRepository.findById(athleteId)).willReturn(Optional.of(tokenInfo));
        given(stravaAuthenticationService.refreshAuthenticate("refresh_token")).willReturn(refreshTokenInfo);

        //when
        StravaOAuthTokenInfo returnedTokenInfo = stravaOAuthService.authenticate(code);

        //then
        assertThat(returnedTokenInfo.getTokenType()).isEqualTo(refreshTokenInfo.getTokenType());
        assertThat(returnedTokenInfo.getAccessToken()).isEqualTo(refreshTokenInfo.getAccessToken());
        assertThat(returnedTokenInfo.getExpiresAt()).isEqualTo(refreshTokenInfo.getExpiresAt());
        assertThat(returnedTokenInfo.getExpiresIn()).isEqualTo(refreshTokenInfo.getExpiresIn());
        assertThat(returnedTokenInfo.getRefreshToken()).isEqualTo(refreshTokenInfo.getRefreshToken());
    }

    @Test
    public void authenticate_code_AuthenticationException() throws AuthenticationException {
        doThrow(AuthenticationException.class).when(stravaAuthenticationService).newAuthenticate(any(), any());
        assertThatThrownBy(() -> stravaOAuthService.authenticate("1e0acc9278c9b3e430f658d775b715e8624247cc"))
                .isInstanceOf(AuthenticationException.class);

        when(stravaOAuthRepository.findById(any())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> stravaOAuthService.authenticate("1e0acc9278c9b3e430f658d775b715e8624247cc"))
                .isInstanceOf(AuthenticationException.class);
    }

    @Test
    public void authenticate_reuseToken() throws AuthenticationException {
        //given
        long athleteId = 12345L;
        long expiresIn = 21600L;

        Instant instant = Instant.now();
        ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(instant, ZoneId.of("Asia/Seoul"));
        long nowEpochSecond = zonedDateTime.toEpochSecond();

        StravaOAuthTokenInfo tokenInfo = StravaOAuthTokenInfo.builder()
                .athleteId(athleteId)
                .tokenType("Bearer")
                .accessToken("aa")
                .expiresAt(nowEpochSecond + expiresIn)
                .expiresIn(expiresIn)
                .refreshToken("aa11aa11")
                .build();

        given(applicationProperty.getStravaClientAthleteId()).willReturn(athleteId);
        given(stravaOAuthRepository.findById(athleteId)).willReturn(Optional.ofNullable(tokenInfo));

        //when
        StravaOAuthTokenInfo returnedTokenInfo = stravaOAuthService.authenticate();

        //then
        assertThat(returnedTokenInfo.getAthleteId()).isEqualTo(returnedTokenInfo.getAthleteId());
        assertThat(returnedTokenInfo.getTokenType()).isEqualTo(returnedTokenInfo.getTokenType());
        assertThat(returnedTokenInfo.getAccessToken()).isEqualTo(returnedTokenInfo.getAccessToken());
        assertThat(returnedTokenInfo.getExpiresAt()).isEqualTo(returnedTokenInfo.getExpiresAt());
        assertThat(returnedTokenInfo.getExpiresIn()).isEqualTo(returnedTokenInfo.getExpiresIn());
        assertThat(returnedTokenInfo.getRefreshToken()).isEqualTo(returnedTokenInfo.getRefreshToken());
    }

    @Test
    public void authenticate_reuseToken_AuthenticationException() throws AuthenticationException {
        //given
        long athleteId = 12345L;
        long expiresIn = 21600L;

        given(applicationProperty.getStravaClientAthleteId()).willReturn(athleteId);
        given(stravaOAuthRepository.findById(athleteId)).willReturn(Optional.empty());

        //when & then
        assertThatThrownBy(() -> stravaOAuthService.authenticate()).isInstanceOf(AuthenticationException.class);
    }

}