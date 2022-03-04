package com.dptablo.straview.controller;

import com.dptablo.straview.ApplicationProperty;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/page")
@RequiredArgsConstructor
public class PageController {
    private final ApplicationProperty applicationProperty;

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("pageName", "로그인 페이지");
        return "dummy";
    }

    @GetMapping("/main")
    public String main(Model model) {
        model.addAttribute("pageName", "스트라바 인증 성공 후 이동된 main 페이지");
        return "dummy";
    }

    @GetMapping("/OAuth2/strava")
    public String stravaOauth2SignIn(Model model) {
        model.addAttribute("stravaClientId", applicationProperty.getStravaClientId());
        model.addAttribute("stravaAuthRedirectUrl", applicationProperty.getStravaAuthRedirectUrl());
        return "/page/oauth2_strava";
    }
}
