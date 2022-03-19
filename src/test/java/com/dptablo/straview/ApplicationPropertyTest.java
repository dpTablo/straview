package com.dptablo.straview;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(
        classes = { ApplicationProperty.class }
)
public class ApplicationPropertyTest {
    @Autowired
    private ApplicationProperty applicationProperty;

    @Test
    public void test() {
        assertThat(applicationProperty.getStraviewAdminAccount().length() >= 1).isTrue();
        assertThat(applicationProperty.getStraviewAdminPassword().length() >= 1).isTrue();
        assertThat(applicationProperty.getPrivateKey()).isEqualTo("dptablo_straview");
        assertThat(applicationProperty.getJwtExpiryMinutes()).isEqualTo(60);
        assertThat(applicationProperty.getIssUser()).isEqualTo("dptablo:straview");
        assertThat(applicationProperty.getStravaClientId()).isEqualTo(77198);
        assertThat(applicationProperty.getStravaClientTimeZone()).isEqualTo("Asia/Seoul");
        assertThat(applicationProperty.getStravaClientAthleteId()).isEqualTo(81334314);
        assertThat(applicationProperty.getClientSecretFilePath()).isEqualTo("/Users/dptablo/development/strava-client-secret");
        assertThat(applicationProperty.getStravaAuthRedirectUrl()).isEqualTo("http://localhost:8080/straview/api/auth/strava/authenticate");
        assertThat(applicationProperty.getStravaApiOAuth2Authorize()).isEqualTo("https://www.strava.com/oauth/authorize");
        assertThat(applicationProperty.getStravaApiOAuth2Token()).isEqualTo("https://www.strava.com/api/v3/oauth/token");
        assertThat(applicationProperty.getStravaApiV3Timeout()).isEqualTo(20);
        assertThat(applicationProperty.getStravaApiV3UrlBase()).isEqualTo("https://www.strava.com/api/v3");
        assertThat(applicationProperty.getStravaApiV3UrlAthlete()).isEqualTo("/athlete");
        assertThat(applicationProperty.getStravaApiV3UrlAthleteActivities()).isEqualTo("/athlete/activities");
        assertThat(applicationProperty.getStravaApiV3UrlGear()).isEqualTo("/gear");
    }
}