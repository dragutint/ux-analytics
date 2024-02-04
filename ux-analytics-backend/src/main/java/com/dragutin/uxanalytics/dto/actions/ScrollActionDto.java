package com.dragutin.uxanalytics.dto.actions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class ScrollActionDto extends ActionDto {
    private Long deltaX;
    private Long deltaY;
}
