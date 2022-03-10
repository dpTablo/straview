package com.dptablo.straview.service;

import com.dptablo.straview.ApplicationProperty;
import com.dptablo.straview.dto.entity.StravaOAuthTokenInfo;
import com.dptablo.straview.exception.AuthenticationException;
import com.dptablo.straview.exception.StraviewErrorCode;
import com.dptablo.straview.repository.StravaOAuthRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@AllArgsConstructor
public class StravaOAuthService {
    public static final String NEW_TOKEN_GRANT_TYPE = "authorization_code";
    public static final String REFRESH_TOKEN_GRANT_TYPE = "refresh_token";

    private final StravaAuthenticationService stravaAuthenticationService;
    private final StravaOAuthRepository stravaOAuthRepository;
    private final ApplicationProperty applicationProperty;

    /**
     * <p>Strava OAuth 인증 정보를 요청합니다.</p>
     * <p>이전에 인증한 정보가 없을 경우에는 신규 인증을 요청합니다.</p>
     * <p>이전에 인증한 정보가 있을 경우에는 갱신을 요청합니다.</p>
     * <p>인증 성공한 정보는 DB에 반영됩니다.</p>
     *
     * @param code https://www.strava.com/oauth/authorize 를 통해서 전달받은 code 값
     * @return
     * @throws AuthenticationException
     */
    @Transactional
    public StravaOAuthTokenInfo authenticate(String code) throws AuthenticationException {
        Optional<StravaOAuthTokenInfo> tokenInfoOptional =
                stravaOAuthRepository.findById(applicationProperty.getStravaClientAthleteId());

        if(tokenInfoOptional.isPresent()) {
            return useExistingTokenInfo(tokenInfoOptional.get());
        } else {
            return requestNewTokenInfo(code);
        }
    }

    /**
     * <p>Strava OAuth 인증 정보를 요청합니다.</p>
     * <p>이전에 인증 정보가 없는 경우에는 AuthenticationException을 발생합니다.</p>
     * <p>이전 인증 정보가 만료된 경우에는 갱신을 요청합니다.</p>
     * <p>인증 성공한 정보는 DB에 반영됩니다.</p>
     * @return
     * @throws AuthenticationException
     */
    @Transactional
    public StravaOAuthTokenInfo authenticate() throws AuthenticationException {
        StravaOAuthTokenInfo tokenInfo =
                stravaOAuthRepository.findById(applicationProperty.getStravaClientAthleteId())
                        .orElseThrow(() ->
                                new AuthenticationException(StraviewErrorCode.STRAVA_AUTHENTICATION_TOKEN_NOT_FOUND));

        return useExistingTokenInfo(tokenInfo);
    }

    private StravaOAuthTokenInfo requestNewTokenInfo(String code) throws AuthenticationException {
        return stravaAuthenticationService.newAuthenticate(code, NEW_TOKEN_GRANT_TYPE);
    }

    private StravaOAuthTokenInfo useExistingTokenInfo(StravaOAuthTokenInfo tokenInfo) throws AuthenticationException {
        if(tokenInfo.isExpireToken(applicationProperty.getStravaClientTimeZone())) {
            StravaOAuthTokenInfo freshTokenInfo = stravaAuthenticationService.refreshAuthenticate(REFRESH_TOKEN_GRANT_TYPE);

            tokenInfo.setTokenType(freshTokenInfo.getTokenType());
            tokenInfo.setAccessToken(freshTokenInfo.getAccessToken());
            tokenInfo.setExpiresAt(freshTokenInfo.getExpiresAt());
            tokenInfo.setExpiresIn(freshTokenInfo.getExpiresIn());
            tokenInfo.setRefreshToken(freshTokenInfo.getRefreshToken());
        }
        return tokenInfo;
    }
}