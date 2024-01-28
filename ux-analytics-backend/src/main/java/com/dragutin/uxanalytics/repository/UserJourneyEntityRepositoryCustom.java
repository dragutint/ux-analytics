package com.dragutin.uxanalytics.repository;

import com.dragutin.uxanalytics.dto.actions.KeyboardActionDto;
import com.dragutin.uxanalytics.dto.actions.MouseActionDto;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface UserJourneyEntityRepositoryCustom {

    void appendEvents(String email, List<MouseActionDto> mouseActions, List<KeyboardActionDto> keyboardActions);
}
