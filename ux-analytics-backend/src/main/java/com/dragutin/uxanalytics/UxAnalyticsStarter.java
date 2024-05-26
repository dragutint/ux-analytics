package com.dragutin.uxanalytics;

import com.dragutin.uxanalytics.service.UserJourneyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UxAnalyticsStarter {

    private final UserJourneyService userJourneyService;

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {

        log.info("Application ready event received");

        userJourneyService.quantifyAll();
    }
}
