package com.dragutin.uxanalytics.service.mapper;

import com.dragutin.uxanalytics.dto.requests.CreateUserJourneyRequest;
import com.dragutin.uxanalytics.entity.UserJourneyEntity;
import org.springframework.stereotype.Component;


@Component
public class UserJourneyServiceMapper {

    public UserJourneyEntity mapToEntity(CreateUserJourneyRequest dto) {

        return UserJourneyEntity.builder()
                .email(dto.getUser().getEmail())
                .screenInfo(dto.getScreenInfo())
                .build();
    }
}
