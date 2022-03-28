package com.dptablo.straview.controller.rest.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "파워 분석정보 요청 Response")
@Getter
@Setter
public class AnalyzeResponse {
    private Boolean result;
}
