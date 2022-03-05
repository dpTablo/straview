package com.dptablo.straview.reactive;

import com.dptablo.straview.ApplicationProperty;
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
        return WebClient.create()
                .mutate()
                .baseUrl(applicationProperty.getStravaApiV3UrlBase())
                .defaultHeader(HttpHeaders.AUTHORIZATION, getAccessToken())
                .build();
    }

    private String getAccessToken() throws AuthenticationException {
        return stravaOAuthService.authenticate().getAccessToken();
    }
}
