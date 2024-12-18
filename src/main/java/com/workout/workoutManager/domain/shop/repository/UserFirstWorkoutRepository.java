package com.workout.workoutManager.domain.shop.repository;

import com.workout.workoutManager.domain.User.entity.User;
import com.workout.workoutManager.domain.Workout.WorkoutType;
import com.workout.workoutManager.domain.shop.entity.UserFirstWorkout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 사용자의 운동 타입별 최초 수행 기록 관리를 위한 Repository
 */
@Repository
public interface UserFirstWorkoutRepository extends JpaRepository<UserFirstWorkout, Long> {
    /**
     * 특정 사용자의 특정 운동 타입 최초 수행 기록을 조회합니다.
     *
     * @param user 조회할 사용자
     * @param workoutType 조회할 운동 타입
     * @return 해당하는 최초 수행 기록 Optional
     */
    Optional<UserFirstWorkout> findByUserAndWorkoutType(User user, WorkoutType workoutType);

    /**
     * 특정 사용자의 특정 운동 타입 최초 수행 여부를 확인합니다.
     *
     * @param user 확인할 사용자
     * @param workoutType 확인할 운동 타입
     * @return 최초 수행 여부 (true/false)
     */
    boolean existsByUserAndWorkoutType(User user, WorkoutType workoutType);
}