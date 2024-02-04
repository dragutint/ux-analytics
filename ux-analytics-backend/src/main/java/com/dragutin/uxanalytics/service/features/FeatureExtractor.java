package com.dragutin.uxanalytics.service.features;

import com.dragutin.uxanalytics.dto.actions.ActionDto;
import com.dragutin.uxanalytics.dto.features.FeatureDto;

import java.util.List;

public interface FeatureExtractor {

    FeatureDto extract(List<ActionDto> actions);
}
