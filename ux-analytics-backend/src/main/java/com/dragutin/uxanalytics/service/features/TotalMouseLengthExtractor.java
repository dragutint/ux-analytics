package com.dragutin.uxanalytics.service.features;

import com.dragutin.uxanalytics.dto.actions.ActionDto;
import com.dragutin.uxanalytics.dto.actions.MouseActionDto;
import com.dragutin.uxanalytics.dto.features.FeatureDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TotalMouseLengthExtractor implements FeatureExtractor {

    public FeatureDto extract(List<ActionDto> actions) {

        final List<MouseActionDto> mouseActions = actions.stream()
                .filter(action -> action instanceof MouseActionDto)
                .map(action -> (MouseActionDto) action)
                .toList();

        // calculate between each two mouse actions the distance and sum them up
        double totalMouseLength = 0;

        for(int i = 0; i < mouseActions.size() - 1; i++) {

            if(i == mouseActions.size() - 1) {
                break;
            }

            final MouseActionDto first = mouseActions.get(i);
            final MouseActionDto second = mouseActions.get(i + 1);

            final double distance = Math.sqrt(
                    Math.pow(first.getX() - second.getX(), 2) +
                            Math.pow(first.getY() - second.getY(), 2)
            );

            totalMouseLength += distance;
        }

        return FeatureDto.builder()
                .name("totalMouseLength")
                .value(totalMouseLength + "")
                .build();
    }
}
