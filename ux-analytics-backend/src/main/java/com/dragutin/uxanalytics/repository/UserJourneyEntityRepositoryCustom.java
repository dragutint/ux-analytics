package com.dragutin.uxanalytics.repository;

import com.dragutin.uxanalytics.dto.requests.UserJourneyEventsRequest;

public interface UserJourneyEntityRepositoryCustom {

    void appendEvents(String email, UserJourneyEventsRequest userJourneyEventsRequest);
}
