package com.workout.workoutManager.domain.Workout.entity;

import com.workout.workoutManager.domain.Workout.entity.WorkoutHistory;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "workout_sets")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WorkoutSet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workout_history_id", nullable = false)
    private WorkoutHistory workoutHistory;

    @Column(name = "set_number", nullable = false)
    private Integer setNumber;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal weight;

    @Column(nullable = false)
    private Integer reps;

    @Builder
    public WorkoutSet(WorkoutHistory workoutHistory, Integer setNumber, BigDecimal weight, Integer reps) {
        this.workoutHistory = workoutHistory;
        this.setNumber = setNumber;
        this.weight = weight;
        this.reps = reps;
    }

    // 연관관계 메서드
    public void setWorkoutHistory(WorkoutHistory workoutHistory) {
        this.workoutHistory = workoutHistory;
    }

    public void setSetNumber(Integer setNumber) {
        this.setNumber = setNumber;
    }
}