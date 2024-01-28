package com.dragutin.uxanalytics.dto.requests;

import com.dragutin.uxanalytics.dto.ScreenInfoDto;
import com.dragutin.uxanalytics.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateUserJourneyRequest {

    private UserDto user;
    private ScreenInfoDto screenInfo;

}
