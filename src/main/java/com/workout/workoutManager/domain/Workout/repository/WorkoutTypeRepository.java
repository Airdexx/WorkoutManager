package com.workout.workoutManager.domain.Workout.repository;

import com.workout.workoutManager.domain.Workout.WorkoutType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 운동 타입 관리를 위한 Repository
 */
@Repository
public interface WorkoutTypeRepository extends JpaRepository<WorkoutType, Long> {
    /**
     * 특정 부위의 운동 타입을 조회합니다.
     *
     * @param bodyPart 운동 부위명
     * @return 해당하는 운동 타입 Optional
     */
    Optional<WorkoutType> findByBodyPart(String bodyPart);

    /**
     * 특정 부위의 운동 타입 존재 여부를 확인합니다.
     *
     * @param bodyPart 운동 부위명
     * @return 존재 여부 (true/false)
     */
    boolean existsByBodyPart(String bodyPart);
}