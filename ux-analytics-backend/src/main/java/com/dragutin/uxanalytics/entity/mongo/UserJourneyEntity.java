package com.dragutin.uxanalytics.entity.mongo;

import com.dragutin.uxanalytics.dto.ScreenInfoDto;
import com.dragutin.uxanalytics.dto.actions.KeyboardActionDto;
import com.dragutin.uxanalytics.dto.actions.MouseActionDto;
import com.dragutin.uxanalytics.dto.actions.ScrollActionDto;
import com.dragutin.uxanalytics.dto.features.FeatureDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.Instant;
import java.util.List;

@Data
@Document(collection = "userJourneys")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserJourneyEntity {

    @MongoId
    @Indexed(unique=true)
    private String token;
    private String email;
    private UserJourneyStatus status;
    private ScreenInfoDto screenInfo;
    private List<MouseActionDto> mouseActions;
    private List<KeyboardActionDto> keyboardActions;
    private List<ScrollActionDto> scrollActions;
    private List<FeatureDto> features;
    private Instant startedAt;
    private Instant endedAt;
    private Instant lastEventAt;
}
