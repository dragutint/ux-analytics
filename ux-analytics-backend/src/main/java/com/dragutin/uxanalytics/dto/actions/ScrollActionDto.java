package com.dragutin.uxanalytics.dto.actions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class ScrollActionDto extends ActionDto {
    private LocalDateTime startTimestamp;
    private LocalDateTime endTimestamp;
    private Long deltaX;
    private Long deltaY;
}
