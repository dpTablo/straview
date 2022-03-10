package com.dptablo.straview.reactive;

import com.dptablo.straview.ApplicationProperty;
import com.dptablo.straview.dto.entity.StravaOAuthTokenInfo;
import com.dptablo.straview.exception.AuthenticationException;
import com.dptablo.straview.service.StravaOAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class StravaWebClientFactory {
    private final ApplicationProperty applicationProperty;
    private final StravaOAuthService stravaOAuthService;

    public WebClient createApiWebClient() throws AuthenticationException {
        return WebClient.builder()
                .baseUrl(applicationProperty.getStravaApiV3UrlBase())
                .defaultHeader(HttpHeaders.AUTHORIZATION, getAccessToken())
                .build();
    }

    private String getAccessToken() throws AuthenticationException {
        StravaOAuthTokenInfo tokenInfo = stravaOAuthService.authenticate();
        return tokenInfo.getTokenType()
                .concat(" ")
                .concat(tokenInfo.getAccessToken());
    }
}
