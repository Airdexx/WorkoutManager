package com.workout.workoutManager.dto.Workout.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Schema(description = "운동 세트 수정 요청")
public class WorkoutSetUpdateRequest {
    @Schema(description = "무게 (kg)")
    private BigDecimal weight;
    
    @Schema(description = "반복 횟수")
    private Integer reps;
}