package com.dptablo.straview.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:app.properties")
public class StravaOAuth2 {
    @Value("strava.api.oauth2.authorize")
    private String STRAVA_API_OAUTH2_AUTHORIZE_URL;

}
