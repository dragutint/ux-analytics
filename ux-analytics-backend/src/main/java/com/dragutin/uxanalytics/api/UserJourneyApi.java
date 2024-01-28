package com.dragutin.uxanalytics.api;

import com.dragutin.uxanalytics.dto.requests.CreateUserJourneyRequest;
import com.dragutin.uxanalytics.dto.requests.UserJourneyEventsRequest;
import com.dragutin.uxanalytics.dto.responses.CreateUserJourneyResponse;
import com.dragutin.uxanalytics.service.UserJourneyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@CrossOrigin
public class UserJourneyApi {

    private final UserJourneyService userJourneyService;

    @PostMapping("/user-journeys")
    public ResponseEntity<CreateUserJourneyResponse> create(@RequestBody CreateUserJourneyRequest userJourneyDto) {

        final CreateUserJourneyResponse response = userJourneyService.createUserJourney(userJourneyDto);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/user-journeys/{email}")
    public ResponseEntity<Void> append(@PathVariable String email, @RequestBody UserJourneyEventsRequest userJourneyEventsRequest) {

        userJourneyService.appendEvents(email, userJourneyEventsRequest);

        return ResponseEntity.ok().build();
    }

}
