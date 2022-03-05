package com.dptablo.straview.controller;

import com.dptablo.straview.exception.StraviewException;
import com.dptablo.straview.service.StravaSynchronizeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping("/api/v1/strava")
@RequiredArgsConstructor
public class StravaSynchronizeController {
    private final StravaSynchronizeService stravaSynchronizeService;

    @Operation(summary = "test hello", description = "hello api example")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK !!"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST !!"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND !!"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR !!")
    })
    @GetMapping("/synchronize")
    public String synchronize(
            @Parameter(description = "이름", required = true, example = "Lee") @RequestParam String name
    ) {
        try {
            boolean isSynchronized = stravaSynchronizeService.synchronize();

            HashMap<String, Object> resultMap = new HashMap<>();
//            resultMap.
//
//            ObjectMapper objectMapper = new ObjectMapper();
//            objectMapper.set

            return "";
        } catch(StraviewException e) {
            return "";
        }
    }
}
