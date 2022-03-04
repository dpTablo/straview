package com.dptablo.straview.service;

import com.dptablo.straview.ApplicationProperty;
import com.dptablo.straview.dto.entity.StravaOAuthTokenInfo;
import com.dptablo.straview.exception.AuthenticationException;
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

    @Transactional
    public StravaOAuthTokenInfo authenticate(String code) throws AuthenticationException {
        Optional<StravaOAuthTokenInfo> tokenInfoOptional =
                stravaOAuthRepository.findById(applicationProperty.getStravaClientAthleteId());

        if(tokenInfoOptional.isPresent()) {
            return usingExistingTokenInfo(tokenInfoOptional.get());
        } else {
            return requestNewTokenInfo(code);
        }
    }

    private StravaOAuthTokenInfo requestNewTokenInfo(String code) throws AuthenticationException {
        StravaOAuthTokenInfo tokenInfo = stravaAuthenticationService.newAuthenticate(code, NEW_TOKEN_GRANT_TYPE);
        tokenInfo.setAthleteId(applicationProperty.getStravaClientAthleteId());

        return stravaOAuthRepository.save(tokenInfo);
    }

    private StravaOAuthTokenInfo usingExistingTokenInfo(StravaOAuthTokenInfo tokenInfo) throws AuthenticationException {
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