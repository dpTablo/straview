package com.dptablo.straview.dto.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;

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
    private Integer expiresAt;

    @Column(name = "expires_in", nullable = false)
    @JsonProperty("expires_in")
    private Integer expiresIn;

    @Column(name = "refresh_token", nullable = false)
    @JsonProperty("refresh_token")
    private String refreshToken;

    @Column(name = "access_token", nullable = false)
    @JsonProperty("access_token")
    private String accessToken;

    @OneToOne(mappedBy = "stravaOAuthTokenInfo")
    @JsonProperty("athlete")
    private StravaAthlete athlete;
}
