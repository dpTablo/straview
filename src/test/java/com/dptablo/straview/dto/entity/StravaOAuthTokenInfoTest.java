package com.dptablo.straview.dto.entity;

import org.junit.jupiter.api.Test;

import java.time.*;

import static org.assertj.core.api.Assertions.*;

public class StravaOAuthTokenInfoTest {
    @Test
    public void isExpireToken() {
        // case : expire1
        Instant instant = Instant.parse("2022-02-25T00:00:00.123Z");
        ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(instant, ZoneId.of("Asia/Seoul"));

        StravaOAuthTokenInfo tokenInfo = StravaOAuthTokenInfo.builder()
                .expiresAt(zonedDateTime.toEpochSecond())
                .expiresIn(3600L)
                .build();
        assertThat(tokenInfo.isExpireToken("Asia/Seoul")).isTrue();

        // case : not expire
        instant = Instant.now();

        tokenInfo = StravaOAuthTokenInfo.builder()
                .expiresAt(Instant.now().getEpochSecond() - 1000)
                .expiresIn(3600L)
                .build();
        assertThat(tokenInfo.isExpireToken("Asia/Seoul")).isFalse();
    }
}