package com.workout.workoutManager.dto.Workout.request;

import com.workout.workoutManager.domain.Workout.WorkoutDetail;
import com.workout.workoutManager.domain.Workout.WorkoutType;
import lombok.Getter;

import java.util.List;

@Getter
public class WorkoutRecordRequest {
    private Long userId;           // 사용자 ID
    private WorkoutType workoutType;    // 운동 종류 (등, 가슴 등)
    private WorkoutDetail workoutDetail; // 세부 운동 (벤치프레스, 데드리프트 등)
    private List<WorkoutSetRequest> sets; // 세트 정보 목록
}