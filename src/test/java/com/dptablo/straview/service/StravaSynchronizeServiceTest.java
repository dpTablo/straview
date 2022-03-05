package com.dptablo.straview.service;

import com.dptablo.straview.ApplicationProperty;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class StravaSynchronizeServiceTest {
    @InjectMocks
    private StravaSynchronizeService service;

    @Mock
    private StravaAthleteService athleteService;

    @Mock
    private ApplicationProperty applicationProperty;

    @Test
    public void synchronize() {
    }
}