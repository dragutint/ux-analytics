package com.dragutin.uxanalytics.dto.actions;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class KeyboardActionDto extends ActionDto {
    private String keyId;
    private String key;
    private Long elementX;
    private Long elementY;
    private String elementId;
    private String elementClass;
}
