package com.dptablo.straview.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

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
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class StravaOAuthTokenInfo {
    @JsonProperty("token_type")
    private String tokenType;

    @JsonProperty("expires_at")
    private Integer expiresAt;

    @JsonProperty("expires_in")
    private Integer expiresIn;

    @JsonProperty("refresh_token")
    private String refreshToken;

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("athlete")
    private StravaAthlete athlete;
}
