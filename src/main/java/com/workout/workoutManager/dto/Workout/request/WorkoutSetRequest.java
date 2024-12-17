package com.workout.workoutManager.dto.Workout.request;

import lombok.Getter;
import java.math.BigDecimal;

@Getter
public class WorkoutSetRequest {
    private BigDecimal weight;  // 무게 (kg)
    private Integer reps;       // 반복 횟수
}