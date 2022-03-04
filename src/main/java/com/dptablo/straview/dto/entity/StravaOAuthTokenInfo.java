package com.dptablo.straview.dto.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * <p>
 *      Strava API 를 통해 토큰 교환을 요청하고 승인 후 반환되는 token 정보.<br>
 *      https://www.strava.com/api/v3/oauth/token.
 * </p>
 * <pre>
 * {@code
 * {
 *   "token_type": "Bearer",
 *   "expires_at": 1568775134,
 *   "expires_in": 21600,
 *   "refresh_token": "e5n567567...",
 *   "access_token": "a4b945687g...",
 *   "athlete": {
 *     #{summary athlete representation}
 *   }
 * }
 * }
 * </pre>
 */
@Entity
@Table(name = "strava_oauth")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class StravaOAuthTokenInfo {
    @Id
    @Column(name = "athlete_id", nullable = false)
    private Long athleteId;

    @Column(name = "token_type", nullable = false)
    @JsonProperty("token_type")
    private String tokenType;

    @Column(name = "expires_at", nullable = false)
    @JsonProperty("expires_at")
    private Long expiresAt;

    @Column(name = "expires_in", nullable = false)
    @JsonProperty("expires_in")
    private Long expiresIn;

    @Column(name = "refresh_token", nullable = false)
    @JsonProperty("refresh_token")
    private String refreshToken;

    @Column(name = "access_token", nullable = false)
    @JsonProperty("access_token")
    private String accessToken;

    @OneToOne(mappedBy = "stravaOAuthTokenInfo")
    @JsonProperty("athlete")
    private StravaAthlete athlete;

    /**
     * 토큰의 만료 여부를 반환합니다.
     * @param timeZoneId time-zone ID, 지정되지 않는 경우 'Asia/Seoul'
     * @return true: 만료, false: 유효
     */
    public boolean isExpireToken(String timeZoneId) {
        timeZoneId = timeZoneId == null || timeZoneId.isEmpty() ? "Asia/Seoul" : timeZoneId;

        Instant instant = Instant.ofEpochSecond(expiresAt);
        ZonedDateTime value = ZonedDateTime.ofInstant(instant, ZoneId.of(timeZoneId));
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of(timeZoneId));
        return value.toEpochSecond() < now.toEpochSecond();
    }
}
