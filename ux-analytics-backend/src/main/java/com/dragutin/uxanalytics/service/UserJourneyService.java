package com.dragutin.uxanalytics.service;

import com.dragutin.uxanalytics.dto.requests.CreateUserJourneyRequest;
import com.dragutin.uxanalytics.dto.requests.UserJourneyEventsRequest;
import com.dragutin.uxanalytics.dto.responses.CreateUserJourneyResponse;
import com.dragutin.uxanalytics.entity.UserJourneyEntity;
import com.dragutin.uxanalytics.repository.UserJourneyEntityRepository;
import com.dragutin.uxanalytics.service.mapper.UserJourneyServiceMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserJourneyService {

    private final UserJourneyServiceMapper mapper;
    private final UserJourneyEntityRepository repository;

    public CreateUserJourneyResponse createUserJourney(CreateUserJourneyRequest userJourneyDto) {

        log.info("Creating user journey, request: {}", userJourneyDto);

        final UserJourneyEntity entity = mapper.mapToEntity(userJourneyDto);

        repository.save(entity);

        log.info("User journey created for user: {}", userJourneyDto.getUser());

        return new CreateUserJourneyResponse(
                DigestUtils.md5Hex(entity.getEmail()),
                userJourneyDto.getUser().getEmail()
        );
    }

    public void appendEvents(String email, UserJourneyEventsRequest userJourneyEventsRequest) {

        log.info("Appending events for user: {}", email);

        repository.appendEvents(email, userJourneyEventsRequest.getMouseActions(), userJourneyEventsRequest.getKeyboardActions());

        log.info("Events appended for user: {}", email);
    }
}
