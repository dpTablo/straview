package com.dptablo.straview.controller;

import com.dptablo.straview.exception.StraviewException;
import com.dptablo.straview.service.StravaSynchronizeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/strava")
@RequiredArgsConstructor
@Slf4j
public class StravaSynchronizeController {
    private final StravaSynchronizeService stravaSynchronizeService;

    @GetMapping("/synchronize")
    public ResponseEntity<Map<String, Object>> synchronize() {
        //
        HashMap<String, Object> resultMap = new HashMap<>();
        try {
            boolean isSynchronized = stravaSynchronizeService.synchronize();
            return new ResponseEntity(resultMap, HttpStatus.OK);
        } catch(StraviewException e) {
            log.error(e.getMessage());
            resultMap.put("message", e.getErrorCode().getDescription());
            return new ResponseEntity(resultMap, e.getErrorCode().getHttpStatus());
        }
    }
}
