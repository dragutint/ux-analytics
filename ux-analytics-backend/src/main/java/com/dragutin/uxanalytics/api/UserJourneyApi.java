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

    @PutMapping("/user-journeys/{token}")
    public ResponseEntity<Void> append(@PathVariable String token, @RequestBody UserJourneyEventsRequest userJourneyEventsRequest) {

        userJourneyService.appendEvents(token, userJourneyEventsRequest);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/user-journeys/{token}/terminate")
    public ResponseEntity<Void> terminate(@PathVariable String token) {

        userJourneyService.terminate(token);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/user-journeys/{token}/quantify")
    public ResponseEntity<Void> quantify(@PathVariable String token) {

        userJourneyService.quantify(token);

        return ResponseEntity.ok().build();
    }

}
