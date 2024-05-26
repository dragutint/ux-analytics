package com.dragutin.uxanalytics.dto.features;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeatureDto {

    private String name;
    private String unit;
    private String value;
}
