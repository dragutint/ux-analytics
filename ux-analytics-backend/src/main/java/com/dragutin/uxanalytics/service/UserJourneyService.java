package com.dragutin.uxanalytics.service;

import com.dragutin.uxanalytics.dto.requests.CreateUserJourneyRequest;
import com.dragutin.uxanalytics.dto.requests.UserJourneyEventsRequest;
import com.dragutin.uxanalytics.dto.responses.CreateUserJourneyResponse;
import com.dragutin.uxanalytics.entity.jpa.UserJourneyJpaEntity;
import com.dragutin.uxanalytics.entity.mongo.UserJourneyEntity;
import com.dragutin.uxanalytics.entity.mongo.UserJourneyStatus;
import com.dragutin.uxanalytics.repository.jpa.UserJourneyJpaEntityRepository;
import com.dragutin.uxanalytics.repository.mongo.UserJourneyEntityRepository;
import com.dragutin.uxanalytics.service.mapper.UserJourneyServiceMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserJourneyService {

    private final UserJourneyServiceMapper mapper;
    private final UserJourneyEntityRepository mongoRepository;
    private final UserJourneyJpaEntityRepository jpaRepository;
    private final UXQuantificationService uxQuantificationService;

    private static final int TOKEN_LENGTH = 6;
    private static final String ALPHANUMERIC_CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";


    public CreateUserJourneyResponse createUserJourney(CreateUserJourneyRequest userJourneyDto) {

        log.debug("Creating user journey, request: {}", userJourneyDto);

        if(mongoRepository.existsById(userJourneyDto.getUser().getEmail())) {
            throw new IllegalArgumentException("User journey already exists for user: " + userJourneyDto.getUser().getEmail());
        }

        final UserJourneyEntity entity = mapper.mapToEntity(userJourneyDto);
        entity.setStatus(UserJourneyStatus.CREATED);
        Instant now = Instant.now();
        entity.setStartedAt(now);
        entity.setToken(RandomStringUtils.random(TOKEN_LENGTH, ALPHANUMERIC_CHARACTERS.toCharArray()));

        mongoRepository.save(entity);

        final UserJourneyJpaEntity jpaEntity = mapper.mapToJpaEntity(entity);
        jpaEntity.setStartedAt(LocalDateTime.ofInstant(now, ZoneOffset.UTC));
        final Integer currentUserFormsCount = jpaRepository.countByEmail(userJourneyDto.getUser().getEmail());
        jpaEntity.setFormNumber(currentUserFormsCount + 1);
        jpaRepository.save(jpaEntity);

        log.info("User journey created for user: {}", userJourneyDto.getUser());

        return new CreateUserJourneyResponse(
                entity.getToken(),
                userJourneyDto.getUser().getEmail()
        );
    }

    public void appendEvents(String token, UserJourneyEventsRequest userJourneyEventsRequest) {

        log.debug("Appending events for token: {}", token);

        if(!mongoRepository.existsById(token)) {
            throw new IllegalArgumentException("User journey does not exist for token: " + token);
        }

        mongoRepository.appendEvents(token, userJourneyEventsRequest);

        log.info("Events appended for user: {}", token);
    }

    public void terminate(String token) {

        log.debug("Terminating user journey for token: {}", token);

        final UserJourneyEntity entity = mongoRepository.findById(token)
                .orElseThrow(() -> new IllegalArgumentException("User journey does not exist for token: " + token));

        entity.setStatus(UserJourneyStatus.COMPLETED);
        entity.setEndedAt(Instant.now());

        mongoRepository.save(entity);

        final UserJourneyJpaEntity jpaEntity = jpaRepository.lockByToken(token);
        jpaEntity.setEndedAt(LocalDateTime.ofInstant(entity.getEndedAt(), ZoneOffset.UTC));
        jpaRepository.save(jpaEntity);

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

        final List<UserJourneyEntity> staleUserJourneys = mongoRepository.findStaleUserJourneys(thresholdInMinutes);

        staleUserJourneys.forEach(userJourney -> {
            userJourney.setStatus(UserJourneyStatus.EXPIRED);
            mongoRepository.save(userJourney);
        });

        log.info("Stale user journeys expired");
    }

    public void quantifyAll() {

        log.debug("Quantifying all user journeys");

        final List<UserJourneyEntity> userJourneys = mongoRepository.findAll();

        userJourneys.forEach(userJourney -> {
            CompletableFuture.runAsync(() -> uxQuantificationService.quantify(userJourney.getToken()));
        });

        log.info("All user journeys quantified");
    }
}
