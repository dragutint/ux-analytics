package com.dragutin.uxanalytics.repository;

import com.dragutin.uxanalytics.dto.requests.UserJourneyEventsRequest;
import com.dragutin.uxanalytics.entity.UserJourneyEntity;

import java.util.List;

public interface UserJourneyEntityRepositoryCustom {

    void appendEvents(String email, UserJourneyEventsRequest userJourneyEventsRequest);

    List<UserJourneyEntity> findStaleUserJourneys(int thresholdInMinutes);

}
