package com.dragutin.uxanalytics.service;

import com.dragutin.uxanalytics.dto.requests.CreateUserJourneyRequest;
import com.dragutin.uxanalytics.dto.requests.UserJourneyEventsRequest;
import com.dragutin.uxanalytics.dto.responses.CreateUserJourneyResponse;
import com.dragutin.uxanalytics.entity.UserJourneyEntity;
import com.dragutin.uxanalytics.entity.UserJourneyStatus;
import com.dragutin.uxanalytics.repository.UserJourneyEntityRepository;
import com.dragutin.uxanalytics.service.mapper.UserJourneyServiceMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserJourneyService {

    private final UserJourneyServiceMapper mapper;
    private final UserJourneyEntityRepository repository;
    private final UXQuantificationService uxQuantificationService;

    private static final int TOKEN_LENGTH = 6;
    private static final String ALPHANUMERIC_CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";


    public CreateUserJourneyResponse createUserJourney(CreateUserJourneyRequest userJourneyDto) {

        log.debug("Creating user journey, request: {}", userJourneyDto);

        if(repository.existsById(userJourneyDto.getUser().getEmail())) {
            throw new IllegalArgumentException("User journey already exists for user: " + userJourneyDto.getUser().getEmail());
        }

        final UserJourneyEntity entity = mapper.mapToEntity(userJourneyDto);
        entity.setStatus(UserJourneyStatus.CREATED);
        Instant now = Instant.now();
        entity.setStartedAt(now);
        entity.setToken(RandomStringUtils.random(TOKEN_LENGTH, ALPHANUMERIC_CHARACTERS.toCharArray()));

        repository.save(entity);

        log.info("User journey created for user: {}", userJourneyDto.getUser());

        return new CreateUserJourneyResponse(
                entity.getToken(),
                userJourneyDto.getUser().getEmail()
        );
    }

    public void appendEvents(String token, UserJourneyEventsRequest userJourneyEventsRequest) {

        log.debug("Appending events for token: {}", token);

        if(!repository.existsById(token)) {
            throw new IllegalArgumentException("User journey does not exist for token: " + token);
        }

        repository.appendEvents(token, userJourneyEventsRequest);

        log.info("Events appended for user: {}", token);
    }

    public void terminate(String token) {

        log.debug("Terminating user journey for token: {}", token);

        final UserJourneyEntity entity = repository.findById(token)
                .orElseThrow(() -> new IllegalArgumentException("User journey does not exist for token: " + token));

        entity.setStatus(UserJourneyStatus.COMPLETED);
        entity.setEndedAt(Instant.now());

        repository.save(entity);

        CompletableFuture.runAsync(() -> uxQuantificationService.quantify(token));

        log.info("User journey terminated for token: {}", token);
    }

    public void quantify(String token) {

        log.debug("Quantifying user journey for token: {}", token);

        uxQuantificationService.quantify(token);

        log.info("User journey quantified for token: {}", token);

    }

    public void expireStale(int thresholdInMinutes) {

        log.debug("Expiring stale user journeys");

        final List<UserJourneyEntity> staleUserJourneys = repository.findStaleUserJourneys(thresholdInMinutes);

        staleUserJourneys.forEach(userJourney -> {
            userJourney.setStatus(UserJourneyStatus.EXPIRED);
            repository.save(userJourney);
        });

        log.info("Stale user journeys expired");
    }
}
