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

    @Value("${jwt.issuer:dptablo:dptablo:straview}")
    private String issUser;

    @Value("${jwt.privateKey:dptablo_straview}")
    private String privateKey;

    @Value("${jwt.expiryMinutes:60}")
    private long jwtExpiryMinutes;

    @Value("${strava.clientId}")
    private String stravaClientId;

    @Value("${strava.clientSecretFilePath}")
    private String clientSecretFilePath;

    @Value("${strava.api.oauth2.authorize}")
    private String stravaApiOAuth2Authorize;

    @Value("${strava.api.oauth2.token}")
    private String stravaApiOAuth2Token;

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
            stream.forEach(s -> contentBuilder.append(s).append("\n"));
            return contentBuilder.toString();
        } catch (IOException e) {
            throw new AuthenticationException(StraviewErrorCode.INVALID_STRAVA_CLIENT_SECRET, "Client secret file not be read.");
        }
    }
}
