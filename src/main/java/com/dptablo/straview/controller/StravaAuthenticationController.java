package com.dptablo.straview.controller;

import com.dptablo.straview.ApplicationProperty;
import com.dptablo.straview.exception.AuthenticationException;
import com.dptablo.straview.exception.StraviewInternalServerException;
import com.dptablo.straview.service.StravaOAuthService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URI;
import java.net.URISyntaxException;

@Controller
@RequestMapping("/api/auth/strava")
@AllArgsConstructor
@Slf4j
public class StravaAuthenticationController {
    private final StravaOAuthService stravaOAuthService;
    private final ApplicationProperty applicationProperty;

    /**
     * <p>Strava OAuth API 의 인증 정보를 확인합니다.</p>
     * <p>code 파라메터 값이 있을 때에는 새로운 인증을 요청하고, code 파라메터 값이 없을 때에는 기존 토큰 정보를 이용합니다.</p>
     * <p>인증에 성공한 경우에는 successUrl로 리다이렉트 됩니다. 지정되지 않은 경우 이동하지 않습니다.</p>
     *
     * @param code Strava 의 requesting access 절차에서 획득한 'code' 값.
     * @param successUrl 인증 성공 후 redirect url
     * @return
     */
    @GetMapping("/authenticate")
    public ResponseEntity<Object> authenticate(
            @RequestParam(required = false, defaultValue = "") String code,
            @RequestParam(required = false, defaultValue = "") String successUrl
    ) throws AuthenticationException, URISyntaxException {
        try {
            reuseToken();
        } catch (StraviewInternalServerException e) {
            String url = applicationProperty.getStravaApiOAuth2Authorize()
                    .concat("?client_id=").concat(applicationProperty.getStravaClientId().toString())
                    .concat("&redirect_uri=").concat(applicationProperty.getStravaAuthRedirectUrl())
                    .concat("&response_type=code")
                    .concat("&approval_prompt=auto")
                    .concat("&scope=read_all");

            if(code.isEmpty()) { // strava 인증 페이지
                HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.setLocation(URI.create(url));
                return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
            } else {
                stravaOAuthService.authenticate(code);
            }
        }

        if(!successUrl.isEmpty()) {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setLocation(URI.create(successUrl));
            return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
        } else {
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    private void reuseToken() throws StraviewInternalServerException {
        try {
            stravaOAuthService.authenticate();
        } catch (AuthenticationException e) {
            throw new StraviewInternalServerException(e.getErrorCode(), e);
        }
    }
}
