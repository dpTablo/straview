package com.dptablo.straview.controller.rest;

import com.dptablo.straview.dto.entity.StravaSyncInfo;
import com.dptablo.straview.exception.StraviewException;
import com.dptablo.straview.service.StravaSynchronizeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/strava")
@RequiredArgsConstructor
@Slf4j
public class StravaSynchronizeController {
    private final StravaSynchronizeService stravaSynchronizeService;

    @GetMapping("/synchronize")
    public ResponseEntity<StravaSyncInfo> synchronize() {
        try {
            StravaSyncInfo syncInfo = stravaSynchronizeService.synchronize();
            return new ResponseEntity(syncInfo, HttpStatus.OK);
        } catch(StraviewException e) {
            log.error(e.getMessage());
            return new ResponseEntity(null, e.getErrorCode().getHttpStatus());
        }
    }
}
