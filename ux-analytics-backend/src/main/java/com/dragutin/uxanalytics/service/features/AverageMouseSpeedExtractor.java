package com.dragutin.uxanalytics.service.features;

import com.dragutin.uxanalytics.dto.actions.ActionDto;
import com.dragutin.uxanalytics.dto.actions.MouseActionDto;
import com.dragutin.uxanalytics.dto.features.FeatureDto;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class AverageMouseSpeedExtractor implements FeatureExtractor {

    public FeatureDto extract(List<ActionDto> actions) {

        final List<MouseActionDto> mouseActions = actions.stream()
                .filter(action -> action instanceof MouseActionDto)
                .map(action -> (MouseActionDto) action)
                .toList();

        final double averageSpeed = calculateAverageSpeed(mouseActions);

        // round on 4 decimal places
        final String averageSpeedString = String.format("%.4f", averageSpeed);

        return FeatureDto.builder()
                .name("averageMouseSpeed")
                .unit("px/ms")
                .value(averageSpeedString)
                .build();
    }

    public double calculateAverageSpeed(List<MouseActionDto> mouseEvents) {

        if (mouseEvents == null || mouseEvents.size() < 2) {
            throw new IllegalArgumentException("At least two mouse events are required for speed calculation");
        }

        double totalSpeed = 0.0;

        for (int i = 1; i < mouseEvents.size(); i++) {
            MouseActionDto currentEvent = mouseEvents.get(i);
            MouseActionDto previousEvent = mouseEvents.get(i - 1);

            // Calculate distance
            double distance = calculateDistance(previousEvent.getX(), previousEvent.getY(),
                    currentEvent.getX(), currentEvent.getY());

            // Calculate time difference
            long timeDifference = calculateTimeDifference(previousEvent.getTimestamp(), currentEvent.getTimestamp());

            // Calculate speed (distance / time)
            double speed = distance / timeDifference;

            if(!Double.isNaN(speed)) {
                totalSpeed += speed;
            }
        }

        // Calculate average speed
        return totalSpeed / (mouseEvents.size() - 1);
    }

    private double calculateDistance(Long x1, Long y1, Long x2, Long y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    private long calculateTimeDifference(LocalDateTime timestamp1, LocalDateTime timestamp2) {
        return java.time.Duration.between(timestamp1, timestamp2).toMillis();
    }
}
