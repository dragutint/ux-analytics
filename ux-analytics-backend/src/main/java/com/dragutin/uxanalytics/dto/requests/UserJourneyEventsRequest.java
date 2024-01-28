package com.dragutin.uxanalytics.dto.requests;

import com.dragutin.uxanalytics.dto.actions.KeyboardActionDto;
import com.dragutin.uxanalytics.dto.actions.MouseActionDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserJourneyEventsRequest {

    private List<MouseActionDto> mouseActions;
    private List<KeyboardActionDto> keyboardActions;

}
