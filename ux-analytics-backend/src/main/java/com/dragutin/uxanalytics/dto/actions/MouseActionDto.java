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
public class MouseActionDto extends ActionDto {
    private MouseActionType action;
    private Long x;
    private Long y;
    private String overElementId;
    private String overElementClass;
}
