package com.dptablo.straview;

import com.dptablo.straview.exception.AuthenticationException;
import com.dptablo.straview.exception.StraviewErrorCode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

@Component
@PropertySource("classpath:app.properties")
@Getter
@Slf4j
public class ApplicationProperty {
    private final ApplicationContext applicationContext;

    @Value("{$straview.admin.account:straview}")
    private String straviewAdminAccount;

    @Value("{$straview.admin.password:straview1234}")
    private String straviewAdminPassword;

    @Value("${jwt.issuer:dptablo:dptablo:straview}")
    private String issUser;

    @Value("${jwt.privateKey:dptablo_straview}")
    private String privateKey;

    @Value("${jwt.expiryMinutes:60}")
    private long jwtExpiryMinutes;

    @Value("${strava.clientId}")
    private Integer stravaClientId;

    @Value("${strava.clientTimeZone}")
    private String stravaClientTimeZone;

    @Value("${strava.clientAthleteId}")
    private Long stravaClientAthleteId;

    @Value("${strava.clientSecretFilePath}")
    private String clientSecretFilePath;

    @Value("${strava.auth.redirectUrl}")
    private String stravaAuthRedirectUrl;

    @Value("${strava.api.oauth2.authorize}")
    private String stravaApiOAuth2Authorize;

    @Value("${strava.api.oauth2.token}")
    private String stravaApiOAuth2Token;

    @Value("${strava.api.v3.timeout}")
    private Integer stravaApiV3Timeout;

    @Value("${strava.api.v3.url.baseUrl}")
    private String stravaApiV3UrlBase;

    @Value("${strava.api.v3.url.athlete}")
    private String stravaApiV3UrlAthlete;

    @Value("${strava.api.v3.url.athlete.activities}")
    private String stravaApiV3UrlAthleteActivities;

    @Value("${strava.api.v3.url.gear}")
    private String stravaApiV3UrlGear;

    private String clientSecret;

    public ApplicationProperty(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @PostConstruct
    public void initialize() {
        try {
            clientSecret = readClientSecretFile();
        } catch (AuthenticationException e) {
            log.error(e.getMessage());
            ((ConfigurableApplicationContext) applicationContext).close();
        }
    }

    private String readClientSecretFile() throws AuthenticationException {
        try (Stream<String> stream = Files.lines(Paths.get(clientSecretFilePath), StandardCharsets.UTF_8)) {
            StringBuilder contentBuilder = new StringBuilder();
            return stream.findFirst().orElseThrow(IOException::new);
        } catch (IOException e) {
            throw new AuthenticationException(StraviewErrorCode.INVALID_STRAVA_CLIENT_SECRET, "Client secret file not be read.");
        }
    }
}
