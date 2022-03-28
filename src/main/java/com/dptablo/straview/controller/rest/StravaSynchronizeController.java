package com.dptablo.straview.controller.rest;

import com.dptablo.straview.dto.entity.StravaSyncInfo;
import com.dptablo.straview.exception.StraviewException;
import com.dptablo.straview.service.StravaSynchronizeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/strava")
@RequiredArgsConstructor
@Slf4j
public class StravaSynchronizeController {
    private final StravaSynchronizeService stravaSynchronizeService;

    @Operation(summary = "Strava 데이터 동기화", description = "Strava의 데이터를 요청하여 서버의 DB와 동기화 합니다.")
    @ApiResponse(
            responseCode = "200",
            description = "동기화에 성공한 선수id와 시간이 반환됩니다.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StravaSyncInfo.class)
            )
    )
    @GetMapping("/synchronize")
    public StravaSyncInfo synchronize() throws StraviewException, JsonProcessingException {
        return stravaSynchronizeService.synchronize();
    }
}
