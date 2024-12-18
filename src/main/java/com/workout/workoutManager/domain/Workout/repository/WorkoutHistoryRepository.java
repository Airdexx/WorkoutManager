package com.workout.workoutManager.domain.Workout.repository;

import com.workout.workoutManager.domain.User.entity.User;
import com.workout.workoutManager.domain.Workout.entity.WorkoutHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface WorkoutHistoryRepository extends JpaRepository<WorkoutHistory, Long> {
    // 특정 사용자의 운동 기록 조회 (기존)
    List<WorkoutHistory> findByUserIdOrderByWorkoutDateDesc(Long userId);

    // 특정 사용자의 운동 기록 조회 (엔티티 관계)
    List<WorkoutHistory> findByUserOrderByWorkoutDateDesc(User user);

    // 특정 기간 동안의 운동 기록 조회 (기존)
    List<WorkoutHistory> findByUserIdAndWorkoutDateBetween(
            Long userId,
            LocalDateTime startDate,
            LocalDateTime endDate
    );

    // 특정 기간 동안의 운동 기록 조회 (엔티티 관계)
    List<WorkoutHistory> findByUserAndWorkoutDateBetween(
            User user,
            LocalDateTime startDate,
            LocalDateTime endDate
    );

    // 특정 날짜의 운동 여부 확인 (기존)
    boolean existsByUserIdAndWorkoutDateBetween(
            Long userId,
            LocalDateTime startOfDay,
            LocalDateTime endOfDay
    );

    // 특정 날짜의 운동 여부 확인 (엔티티 관계)
    boolean existsByUserAndWorkoutDateBetween(
            User user,
            LocalDateTime startOfDay,
            LocalDateTime endOfDay
    );

    // 특정 부위의 운동 기록 조회 (기존)
    List<WorkoutHistory> findByUserIdAndWorkoutType_BodyPart(
            Long userId,
            String bodyPart
    );

    // 특정 부위의 운동 기록 조회 (엔티티 관계)
    List<WorkoutHistory> findByUserAndWorkoutType_BodyPart(
            User user,
            String bodyPart
    );

    // 가장 최근 운동 기록 조회 (기존)
    Optional<WorkoutHistory> findFirstByUserIdOrderByWorkoutDateDesc(Long userId);

    // 가장 최근 운동 기록 조회 (엔티티 관계)
    Optional<WorkoutHistory> findFirstByUserOrderByWorkoutDateDesc(User user);

    // 특정 기간 동안의 운동 기록 수 계산 (기존)
    long countByUserIdAndWorkoutDateBetween(
            Long userId,
            LocalDateTime startDate,
            LocalDateTime endDate
    );

    // 특정 기간 동안의 운동 기록 수 계산 (엔티티 관계)
    long countByUserAndWorkoutDateBetween(
            User user,
            LocalDateTime startDate,
            LocalDateTime endDate
    );
}