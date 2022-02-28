package com.dptablo.straview.controller;

import com.dptablo.straview.exception.AuthenticationException;
import com.dptablo.straview.service.StravaOAuthService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/api/auth/strava")
@AllArgsConstructor
@Slf4j
public class StravaAuthenticationController {
    private final StravaOAuthService stravaOAuthService;

    /**
     * Strava OAuth API 의 새로운 인증 토큰을 요청합니다.
     * @param attributes
     * @param code Strava 의 requesting access 절차에서 획득한 'code' 값.
     * @return
     */
    @GetMapping("/authenticate")
    public RedirectView authenticate(
            RedirectAttributes attributes,
            @RequestParam String code
    ) {
        try {
            stravaOAuthService.authenticate(code);

            attributes.addFlashAttribute("flashAttribute", "redirectWithRedirectView");
            return new RedirectView("/page/main", true);
        } catch(AuthenticationException e) {
            log.error(e.getMessage());
            return new RedirectView("/page/OAuth2/strava", true);
        }
    }
}
