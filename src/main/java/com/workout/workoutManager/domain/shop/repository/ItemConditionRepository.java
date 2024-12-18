package com.workout.workoutManager.domain.shop.repository;

import com.workout.workoutManager.domain.Workout.WorkoutType;
import com.workout.workoutManager.domain.shop.entity.ItemCondition;
import com.workout.workoutManager.domain.shop.enumerate.AchievementType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 아이템 획득 조건 관리를 위한 Repository
 */
@Repository
public interface ItemConditionRepository extends JpaRepository<ItemCondition, Long> {
    /**
     * 달성 타입별로 조건 목록을 조회합니다.
     *
     * @param achievementType 달성 타입 (PURCHASE, ACHIEVEMENT, EVENT)
     * @return 해당하는 달성 조건 목록
     */
    List<ItemCondition> findByAchievementType(AchievementType achievementType);

    /**
     * 특정 운동 타입에 대한 달성 조건을 조회합니다.
     *
     * @param achievementType 달성 타입
     * @param workoutType 운동 타입
     * @return 해당하는 달성 조건 Optional
     */
    Optional<ItemCondition> findByAchievementTypeAndWorkoutType(AchievementType achievementType, WorkoutType workoutType);
}