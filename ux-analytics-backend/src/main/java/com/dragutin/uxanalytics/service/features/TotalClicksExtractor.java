package com.dragutin.uxanalytics.service.features;

import com.dragutin.uxanalytics.dto.actions.ActionDto;
import com.dragutin.uxanalytics.dto.actions.MouseActionDto;
import com.dragutin.uxanalytics.dto.features.FeatureDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TotalClicksExtractor implements FeatureExtractor {

    public FeatureDto extract(List<ActionDto> actions) {

        final List<MouseActionDto> mouseActions = actions.stream()
                .filter(action -> action instanceof MouseActionDto)
                .map(action -> (MouseActionDto) action)
                .toList();

        final long totalClicks = mouseActions.stream()
                .filter(action -> action.getAction().equals("click"))
                .count();

        return FeatureDto.builder()
                .name("totalClicks")
                .unit("count")
                .value(totalClicks + "")
                .build();
    }
}
