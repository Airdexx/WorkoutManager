package com.workout.workoutManager.domain.Workout.repository;

import com.workout.workoutManager.domain.User.entity.User;
import com.workout.workoutManager.domain.Workout.WorkoutDetail;
import com.workout.workoutManager.domain.Workout.entity.WorkoutSet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkoutSetRepository extends JpaRepository<WorkoutSet, Long> {
    // 특정 운동 기록의 세트 정보 조회
    List<WorkoutSet> findByWorkoutHistoryIdOrderBySetNumberAsc(Long workoutHistoryId);

    // 특정 운동의 최근 세트 정보 조회 (기존)
    List<WorkoutSet> findByWorkoutHistory_UserIdAndWorkoutHistory_WorkoutDetailIdOrderByWorkoutHistory_WorkoutDateDesc(
            Long userId,
            Long workoutDetailId
    );

    // 특정 운동의 최근 세트 정보 조회 (엔티티 관계)
    List<WorkoutSet> findByWorkoutHistory_UserAndWorkoutHistory_WorkoutDetailOrderByWorkoutHistory_WorkoutDateDesc(
            User user,
            WorkoutDetail workoutDetail
    );
}