package com.dptablo.straview.controller;

import com.dptablo.straview.dto.entity.User;
import com.dptablo.straview.exception.StraviewErrorCode;
import com.dptablo.straview.exception.StraviewException;
import com.dptablo.straview.service.JwtAuthenticationService;
import com.dptablo.straview.service.UserService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class JwtAuthenticationController {
    private final JwtAuthenticationService jwtAuthenticationService;
    private final UserService userService;

    @PostMapping("/authenticate")
    public Map<String, Object> authenticate(
            HttpServletResponse response,
            @Parameter(description = "사용자 ID", required = true, example = "user1") @RequestParam String userId,
            @Parameter(description = "패스워드", required = true, example = "1234") @RequestParam String password
    ) throws StraviewException {
        String token = jwtAuthenticationService.authenticate(userId, password).orElseThrow(() ->
                new StraviewException(StraviewErrorCode.AUTHENTICATION_KEY_CREATION_FAILED,
                        String.format("userId: %s", userId))
        );

        User foundUser = userService.findByUserId(userId);

        response.setContentType(MediaType.APPLICATION_JSON.toString());

        HashMap<String, Object> map = new HashMap<>();
        map.put("userId", foundUser.getUserId());
        map.put("roles", foundUser.getRoles());
        map.put("jwtToken", token);
        return map;
    }
}
