package com.dptablo.straview.controller;

import com.dptablo.straview.service.StravaAuthenticationService;
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
    private final StravaAuthenticationService stravaAuthenticationService;

    @GetMapping("/authenticate")
    public RedirectView authenticate(
            RedirectAttributes attributes,
            @RequestParam String state,
            @RequestParam String code,
            @RequestParam String scope
    ) {
        try {
            //TODO 토큰 정보 DB 조회

            //TODO 토큰 정보 없는 경우 신규 토큰 요청
//            StravaOAuthTokenInfo stravaOAuthTokenInfo = stravaAuthenticationService.authenticate(code, "authorization_code");

            //TODO (토큰 만료 시) 토큰 리프레시
            //TODO 리프레시 토큰 DB 정장

            //TODO 메인 페이지 redirect
            attributes.addFlashAttribute("flashAttribute", "redirectWithRedirectView");
            return new RedirectView("/page/main", true);
        } catch(Exception e) {
            log.error(e.getMessage());
            return new RedirectView("/page/OAuth2/strava", true);
        }
    }
}
