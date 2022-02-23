package com.dptablo.straview.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/page")
@PropertySource("classpath:app.properties")
public class PageController {
    @Value("${strava.clientId}")
    private String STRAVA_CLIENT_ID;

    @Value("${strava.auth.redirectUrl}")
    private String STRAVA_AUTH_REDIRECT_URL;

    @GetMapping("/home")
    public String home() {
        return "home";
    }

    @GetMapping("/main")
    public String main(Model model) {
        model.addAttribute("pageName", "스트라바 인증 성공 후 이동된 main 페이지");
        return "dummy";
    }

    @GetMapping("/OAuth2/strava")
    public String stravaOauth2SignIn(Model model) {
        model.addAttribute("stravaClientId", STRAVA_CLIENT_ID);
        model.addAttribute("stravaAuthRedirectUrl", STRAVA_AUTH_REDIRECT_URL);
        return "/page/oauth2_strava";
    }
}
