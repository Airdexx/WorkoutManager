package com.workout.workoutManager.dto.Workout.response;

import com.workout.workoutManager.domain.Workout.entity.WorkoutSet;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class WorkoutSetResponse {
    private Long id;              // 세트 ID
    private Integer setNumber;    // 세트 번호
    private BigDecimal weight;    // 무게
    private Integer reps;         // 반복 횟수

    public static WorkoutSetResponse from(WorkoutSet set) {
        WorkoutSetResponse response = new WorkoutSetResponse();
        response.id = set.getId();
        response.setNumber = set.getSetNumber();
        response.weight = set.getWeight();
        response.reps = set.getReps();
        return response;
    }
}