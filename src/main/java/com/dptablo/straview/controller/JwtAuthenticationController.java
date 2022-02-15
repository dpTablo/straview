package com.dptablo.straview.controller;

import com.dptablo.straview.dto.User;
import com.dptablo.straview.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class JwtAuthenticationController {
    private final AuthenticationService authenticationService;

    @Autowired
    public JwtAuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<String> authenticate(
            @Parameter(description = "사용자 ID", required = true, example = "user1") @RequestParam String userId,
            @Parameter(description = "패스워드", required = true, example = "1234") @RequestParam String password
    ) {
        try {
            String token = authenticationService.authenticate(userId, password).orElseThrow(NullPointerException::new);

            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.add(HttpHeaders.WWW_AUTHENTICATE, token);

            return ResponseEntity.ok()
                    .headers(responseHeaders)
                    .body("");
        } catch (NullPointerException e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("");
        }
    }

    @PostMapping("/signUp")
    public ResponseEntity<String> signUp(
            @Parameter(description = "사용자 ID", required = true, example = "user1") @RequestParam String userId,
            @Parameter(description = "패스워드", required = true, example = "1234") @RequestParam String password
    ) {
        try {
            authenticationService.signUp(userId, password);
            return ResponseEntity.ok().build();
        } catch(Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("");
        }
    }
}
