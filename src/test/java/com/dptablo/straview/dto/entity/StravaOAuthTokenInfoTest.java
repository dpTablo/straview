package com.dptablo.straview.dto.entity;

import org.junit.jupiter.api.Test;

import java.time.*;

import static org.assertj.core.api.Assertions.*;

public class StravaOAuthTokenInfoTest {
    @Test
    public void isExpireToken() {
        // case : expire
        Instant instant = Instant.parse("2000-02-25T00:00:00.123Z");
        ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(instant, ZoneId.of("Asia/Seoul"));

        StravaOAuthTokenInfo tokenInfo = StravaOAuthTokenInfo.builder()
                .expiresAt(zonedDateTime.toEpochSecond())
                .build();
        assertThat(tokenInfo.isExpireToken("Asia/Seoul")).isTrue();

        // case : not expire
        tokenInfo = StravaOAuthTokenInfo.builder()
                .expiresAt(Instant.now().getEpochSecond() + 10000)
                .build();
        assertThat(tokenInfo.isExpireToken("Asia/Seoul")).isFalse();
    }
}