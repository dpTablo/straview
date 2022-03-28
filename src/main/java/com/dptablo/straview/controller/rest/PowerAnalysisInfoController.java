package com.dptablo.straview.controller.rest;

import com.dptablo.straview.controller.rest.response.AnalyzeResponse;
import com.dptablo.straview.service.ActivityPowerAnalysisService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/strava/power")
@RequiredArgsConstructor
@Slf4j
public class PowerAnalysisInfoController {
    private final ActivityPowerAnalysisService powerAnalysisService;

    @Operation(summary = "파워 분석정보 생성 요청", description = "액티비티의 파워 분석 정보 생성을 요청합니다.")
    @Parameter(name = "activityManageId", required = true, description = "액티비티의 id 값", example = "6889126580")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "파워 정보가 생성되었거나 이전에 생성된 정보가 있으면 result에 true가 반환됩니다.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AnalyzeResponse.class)
                    )
            )
    })
    @GetMapping("/analyze/{activityManageId}")
    public AnalyzeResponse analyze(@PathVariable("activityManageId") Long activityManageId) {
        AnalyzeResponse analyzeResponse = new AnalyzeResponse();
        try {
            powerAnalysisService.analyze(activityManageId).orElseThrow(NullPointerException::new);
            analyzeResponse.setResult(true);
        } catch(NullPointerException e) {
            analyzeResponse.setResult(false);
        }
        return analyzeResponse;
    }
}
