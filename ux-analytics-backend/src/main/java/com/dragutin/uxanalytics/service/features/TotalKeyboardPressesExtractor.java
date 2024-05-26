package com.dragutin.uxanalytics.service.features;

import com.dragutin.uxanalytics.dto.actions.ActionDto;
import com.dragutin.uxanalytics.dto.actions.KeyboardActionDto;
import com.dragutin.uxanalytics.dto.features.FeatureDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TotalKeyboardPressesExtractor implements FeatureExtractor {

    public FeatureDto extract(List<ActionDto> actions) {

        final List<KeyboardActionDto> keyboardActions = actions.stream()
                .filter(action -> action instanceof KeyboardActionDto)
                .map(action -> (KeyboardActionDto) action)
                .toList();

        return FeatureDto.builder()
                .name("totalKeyboardPresses")
                .unit("count")
                .value(keyboardActions.size() + "")
                .build();
    }
}
