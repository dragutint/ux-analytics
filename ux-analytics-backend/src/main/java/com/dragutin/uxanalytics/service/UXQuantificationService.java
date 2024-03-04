package com.dragutin.uxanalytics.service;

import com.dragutin.uxanalytics.dto.actions.ActionDto;
import com.dragutin.uxanalytics.dto.features.FeatureDto;
import com.dragutin.uxanalytics.entity.UserJourneyEntity;
import com.dragutin.uxanalytics.repository.UserJourneyEntityRepository;
import com.dragutin.uxanalytics.service.features.FeatureExtractor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UXQuantificationService {

    private final UserJourneyEntityRepository repository;
    private final List<FeatureExtractor> featureExtractors;

    public void quantify(String token) {

        log.debug("Quantifying user journey for token: {}", token);

        final UserJourneyEntity userJourney = repository.findById(token)
                .orElseThrow(() -> new IllegalArgumentException("User journey does not exist for token: " + token));

        final List<ActionDto> actions = extractActions(userJourney);
        final List<FeatureDto> features = new ArrayList<>();

        for(FeatureExtractor featureExtractor : featureExtractors) {

            try {
                final FeatureDto feature = featureExtractor.extract(actions);
                features.add(feature);
            } catch (Exception e) {
                log.error("Error while extracting feature: {}", featureExtractor.getClass().getSimpleName(), e);
            }
        }

        userJourney.setFeatures(features);

        repository.save(userJourney);

        log.info("User journey quantified for user: {}", token);
    }

    private List<ActionDto> extractActions(UserJourneyEntity userJourney) {

        final List<ActionDto> actions = new ArrayList<>();

        if(userJourney.getMouseActions() != null) {
            actions.addAll(userJourney.getMouseActions());
        }
        if(userJourney.getKeyboardActions() != null) {
            actions.addAll(userJourney.getKeyboardActions());
        }
        if(userJourney.getScrollActions() != null) {
            actions.addAll(userJourney.getScrollActions());
        }

        actions.sort(Comparator.comparing(ActionDto::getTimestamp));

        return actions;
    }
}
