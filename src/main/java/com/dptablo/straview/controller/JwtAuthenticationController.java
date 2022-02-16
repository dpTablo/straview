package com.dptablo.straview.controller;

import com.dptablo.straview.exception.StraviewErrorCode;
import com.dptablo.straview.exception.StraviewException;
import com.dptablo.straview.service.JwtAuthenticationService;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class JwtAuthenticationController {
    private final JwtAuthenticationService jwtAuthenticationService;

    @Autowired
    public JwtAuthenticationController(JwtAuthenticationService jwtAuthenticationService) {
        this.jwtAuthenticationService = jwtAuthenticationService;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<String> authenticate(
            @Parameter(description = "사용자 ID", required = true, example = "user1") @RequestParam String userId,
            @Parameter(description = "패스워드", required = true, example = "1234") @RequestParam String password
    ) throws StraviewException {
        String token = jwtAuthenticationService.authenticate(userId, password).orElseThrow(() ->
                new StraviewException(StraviewErrorCode.AUTHENTICATION_KEY_CREATION_FAILED,
                        String.format("userId: %s", userId))
        );

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add(HttpHeaders.WWW_AUTHENTICATE, token);

        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body("");
    }

    @PostMapping("/signUp")
    public ResponseEntity<String> signUp(
            @Parameter(description = "사용자 ID", required = true, example = "user1") @RequestParam String userId,
            @Parameter(description = "패스워드", required = true, example = "1234") @RequestParam String password
    ) {
        jwtAuthenticationService.signUp(userId, password);
        return ResponseEntity.ok().build();
    }
}
