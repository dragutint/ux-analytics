package com.dragutin.uxanalytics.service.features;

import com.dragutin.uxanalytics.dto.actions.ActionDto;
import com.dragutin.uxanalytics.dto.actions.ScrollActionDto;
import com.dragutin.uxanalytics.dto.features.FeatureDto;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class FormCompletionTimeExtractor implements FeatureExtractor {

    public FeatureDto extract(List<ActionDto> actions) {

        final LocalDateTime formStart = actions.stream()
                .map(ActionDto::getTimestamp)
                .min(LocalDateTime::compareTo)
                .orElseThrow(() -> new IllegalArgumentException("No actions found"));

        final LocalDateTime formEnd = actions.stream()
                .filter(action -> action instanceof ScrollActionDto)
                .map(ActionDto::getTimestamp)
                .max(LocalDateTime::compareTo)
                .orElseThrow(() -> new IllegalArgumentException("No scroll actions found"));

        final long completionTime = calculateTimeDifference(formStart, formEnd);

        final long completionTimeInSeconds = completionTime / 1000;

        return FeatureDto.builder()
                .name("formCompletionTime")
                .unit("s")
                .value(completionTimeInSeconds + "")
                .build();
    }

    private long calculateTimeDifference(LocalDateTime timestamp1, LocalDateTime timestamp2) {
        return java.time.Duration.between(timestamp1, timestamp2).toMillis();
    }
}
