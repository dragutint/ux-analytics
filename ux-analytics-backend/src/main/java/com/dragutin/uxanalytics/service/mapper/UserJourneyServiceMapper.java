package com.dragutin.uxanalytics.service.mapper;

import com.dragutin.uxanalytics.dto.requests.CreateUserJourneyRequest;
import com.dragutin.uxanalytics.entity.jpa.UserJourneyJpaEntity;
import com.dragutin.uxanalytics.entity.mongo.UserJourneyEntity;
import org.springframework.stereotype.Component;


@Component
public class UserJourneyServiceMapper {

    public UserJourneyEntity mapToEntity(CreateUserJourneyRequest dto) {

        return UserJourneyEntity.builder()
                .email(dto.getUser().getEmail())
                .screenInfo(dto.getScreenInfo())
                .build();
    }

    public UserJourneyJpaEntity mapToJpaEntity(UserJourneyEntity entity) {

        return UserJourneyJpaEntity.builder()
                .email(entity.getEmail())
                .token(entity.getToken())
                .build();
    }
}
