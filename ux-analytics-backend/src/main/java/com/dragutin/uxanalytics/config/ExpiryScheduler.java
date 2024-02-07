package com.dragutin.uxanalytics.config;

import com.dragutin.uxanalytics.service.UserJourneyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class ExpiryScheduler {

    private final UserJourneyService userJourneyService;
    private final Integer staleUserJourneyThresholdInMinutes;

    public ExpiryScheduler(UserJourneyService userJourneyService,
                           @Value("${expiry.user-journeys.stale-threshold-in-minutes}") Integer staleUserJourneyThresholdInMinutes) {

        this.userJourneyService = userJourneyService;
        this.staleUserJourneyThresholdInMinutes = staleUserJourneyThresholdInMinutes;
    }

    @Scheduled(fixedDelay = 1, initialDelay = 1, timeUnit = TimeUnit.MINUTES)
    public void expireStaleUserJourneys() {

        log.info("START - Scheduler - expiring stale journeys");
        userJourneyService.expireStale(staleUserJourneyThresholdInMinutes);
        log.info("END - Scheduler - expiring stale journeys");
    }

}
