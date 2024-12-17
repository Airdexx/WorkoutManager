package com.workout.workoutManager.dto.Workout.response;

import com.workout.workoutManager.domain.Workout.entity.WorkoutHistory;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class WorkoutHistoryResponse {
    private Long id;              // 운동 기록 ID
    private String workoutType;   // 운동 종류
    private String workoutName;   // 세부 운동 이름
    private LocalDateTime workoutDate; // 운동 날짜

    public static WorkoutHistoryResponse from(WorkoutHistory history) {
        WorkoutHistoryResponse response = new WorkoutHistoryResponse();
        response.id = history.getId();
        response.workoutType = history.getWorkoutType().getBodyPart();
        response.workoutName = history.getWorkoutDetail().getName();
        response.workoutDate = history.getWorkoutDate();
        return response;
    }
}
