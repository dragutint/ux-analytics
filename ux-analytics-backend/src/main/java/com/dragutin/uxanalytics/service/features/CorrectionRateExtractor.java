package com.dragutin.uxanalytics.service.features;

import com.dragutin.uxanalytics.dto.actions.ActionDto;
import com.dragutin.uxanalytics.dto.actions.KeyboardActionDto;
import com.dragutin.uxanalytics.dto.features.FeatureDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
public class CorrectionRateExtractor implements FeatureExtractor {

    // 8 - backspace
    // 46 - delete
    final Set<String> keyIds = Set.of("8", "46");

    public FeatureDto extract(List<ActionDto> actions) {

        // Correction Rate: Number of backspaces or corrections per sentence or word.
        final List<KeyboardActionDto> keyboardActionDtos = actions.stream()
                .filter(action -> action instanceof KeyboardActionDto)
                .filter(action -> keyIds.contains(((KeyboardActionDto) action).getKeyId()))
                .map(action -> (KeyboardActionDto) action)
                .toList();

        return FeatureDto.builder()
                .name("correctionRate")
                .unit("count")
                .value(keyboardActionDtos.size() + "")
                .build();
    }
}