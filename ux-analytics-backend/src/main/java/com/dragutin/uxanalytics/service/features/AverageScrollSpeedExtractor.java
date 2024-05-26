package com.dragutin.uxanalytics.service.features;

import com.dragutin.uxanalytics.dto.actions.ActionDto;
import com.dragutin.uxanalytics.dto.actions.ScrollActionDto;
import com.dragutin.uxanalytics.dto.features.FeatureDto;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class AverageScrollSpeedExtractor implements FeatureExtractor {

    public FeatureDto extract(List<ActionDto> actions) {

        final List<ScrollActionDto> scrollActions = actions.stream()
                .filter(action -> action instanceof ScrollActionDto)
                .map(action -> (ScrollActionDto) action)
                .toList();

        final double averageSpeed = calculateAverageSpeed(scrollActions);

        // round on 4 decimal places
        final String averageSpeedString = String.format("%.4f", averageSpeed);

        return FeatureDto.builder()
                .name("averageScrollSpeed")
                .unit("px/ms")
                .value(averageSpeedString)
                .build();
    }

    public double calculateAverageSpeed(List<ScrollActionDto> mouseEvents) {

        if (mouseEvents == null || mouseEvents.size() < 2) {
            throw new IllegalArgumentException("At least two scroll events are required for speed calculation");
        }

        double totalSpeed = 0.0;

        for (int i = 0; i < mouseEvents.size(); i++) {
            ScrollActionDto currentEvent = mouseEvents.get(i);

            // Calculate distance
            double distance = currentEvent.getDeltaX() + currentEvent.getDeltaY();

            // Calculate time difference
            long timeDifference = calculateTimeDifference(currentEvent.getStartTimestamp(), currentEvent.getEndTimestamp());

            // Calculate speed (distance / time)
            double speed = distance / timeDifference;

            if(!Double.isNaN(speed)) {
                totalSpeed += speed;
            }
        }

        // Calculate average speed
        return totalSpeed / (mouseEvents.size() - 1);
    }

    private long calculateTimeDifference(LocalDateTime timestamp1, LocalDateTime timestamp2) {
        return java.time.Duration.between(timestamp1, timestamp2).toMillis();
    }
}
