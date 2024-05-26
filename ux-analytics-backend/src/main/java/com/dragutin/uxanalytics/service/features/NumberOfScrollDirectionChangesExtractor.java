package com.dragutin.uxanalytics.service.features;

import com.dragutin.uxanalytics.dto.actions.ActionDto;
import com.dragutin.uxanalytics.dto.actions.ScrollActionDto;
import com.dragutin.uxanalytics.dto.features.FeatureDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NumberOfScrollDirectionChangesExtractor implements FeatureExtractor {

    public FeatureDto extract(List<ActionDto> actions) {

        final List<ScrollActionDto> scrollActions = actions.stream()
                .filter(action -> action instanceof ScrollActionDto)
                .map(action -> (ScrollActionDto) action)
                .toList();

        int numberOfScrollDirectionChanges = 0;

        for(int i = 0; i < scrollActions.size() - 1; i++) {

            if(i == scrollActions.size() - 1) {
                break;
            }

            final ScrollActionDto first = scrollActions.get(i);
            final ScrollActionDto second = scrollActions.get(i + 1);
            if(first.getDeltaX() * second.getDeltaX() < 0 || first.getDeltaY() * second.getDeltaY() < 0) {
                numberOfScrollDirectionChanges++;
            }
        }

        return FeatureDto.builder()
                .name("numberOfScrollDirectionChanges")
                .unit("count")
                .value(numberOfScrollDirectionChanges + "")
                .build();
    }
}
