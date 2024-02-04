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
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserJourneyService {

    private final UserJourneyServiceMapper mapper;
    private final UserJourneyEntityRepository repository;

    public CreateUserJourneyResponse createUserJourney(CreateUserJourneyRequest userJourneyDto) {

        log.info("Creating user journey, request: {}", userJourneyDto);

        if(repository.existsById(userJourneyDto.getUser().getEmail())) {
            throw new IllegalArgumentException("User journey already exists for user: " + userJourneyDto.getUser().getEmail());
        }

        final UserJourneyEntity entity = mapper.mapToEntity(userJourneyDto);
        entity.setStatus(UserJourneyStatus.CREATED);
        entity.setStartedAt(Instant.now());

        repository.save(entity);

        log.info("User journey created for user: {}", userJourneyDto.getUser());

        return new CreateUserJourneyResponse(
                DigestUtils.md5Hex(entity.getEmail()),
                userJourneyDto.getUser().getEmail()
        );
    }

    public void appendEvents(String email, UserJourneyEventsRequest userJourneyEventsRequest) {

        log.info("Appending events for user: {}", email);

        if(!repository.existsById(email)) {
            throw new IllegalArgumentException("User journey does not exist for user: " + email);
        }

        repository.appendEvents(email, userJourneyEventsRequest);

        log.info("Events appended for user: {}", email);
    }

    public void terminate(String email) {

        log.info("Terminating user journey for user: {}", email);

        final UserJourneyEntity entity = repository.findById(email)
                .orElseThrow(() -> new IllegalArgumentException("User journey does not exist for user: " + email));

        entity.setStatus(UserJourneyStatus.COMPLETED);
        entity.setEndedAt(Instant.now());

        repository.save(entity);

        log.info("User journey terminated for user: {}", email);
    }
}
