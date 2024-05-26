package com.dragutin.uxanalytics.service.features;

import com.dragutin.uxanalytics.dto.actions.ActionDto;
import com.dragutin.uxanalytics.dto.actions.MouseActionDto;
import com.dragutin.uxanalytics.dto.features.FeatureDto;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Component
public class IdleTimeExtractor implements FeatureExtractor {

    public FeatureDto extract(List<ActionDto> actions) {

        final long idleTimeThreshold = 2L;
        LocalDateTime lastEventTime = null;
        long totalIdleTime = 0L;

        for (ActionDto action : actions) {

            if(lastEventTime != null) {

                long idleTimeInSeconds = action.getTimestamp().toEpochSecond(ZoneOffset.UTC) - lastEventTime.toEpochSecond(ZoneOffset.UTC);

                if(idleTimeInSeconds >= idleTimeThreshold) {
                    totalIdleTime += idleTimeInSeconds;
                }
            }

            lastEventTime = action.getTimestamp();
        }

        return FeatureDto.builder()
                .name("idleTime")
                .unit("seconds")
                .value(totalIdleTime + "")
                .build();
    }
}