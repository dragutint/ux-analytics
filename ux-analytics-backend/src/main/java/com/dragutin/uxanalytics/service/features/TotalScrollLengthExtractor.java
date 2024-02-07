package com.dragutin.uxanalytics.service.features;

import com.dragutin.uxanalytics.dto.actions.ActionDto;
import com.dragutin.uxanalytics.dto.actions.ScrollActionDto;
import com.dragutin.uxanalytics.dto.features.FeatureDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TotalScrollLengthExtractor implements FeatureExtractor {

    public FeatureDto extract(List<ActionDto> actions) {

        final List<ScrollActionDto> scrollActions = actions.stream()
                .filter(action -> action instanceof ScrollActionDto)
                .map(action -> (ScrollActionDto) action)
                .toList();

        // sum all scroll lengths from X
        final Long totalScrollLengthX = scrollActions.stream()
                .map(sa -> Math.abs(sa.getDeltaX()))
                .reduce(0L, Long::sum);

        // sum all scroll lengths from Y
        final Long totalScrollLengthY = scrollActions.stream()
                .map(sa -> Math.abs(sa.getDeltaY()))
                .reduce(0L, Long::sum);

        return new FeatureDto("totalScrollLength", (totalScrollLengthX + totalScrollLengthY) + "px");
    }
}
