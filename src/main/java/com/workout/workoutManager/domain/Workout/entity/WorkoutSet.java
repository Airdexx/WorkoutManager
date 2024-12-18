package com.workout.workoutManager.domain.Workout.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    public void updateSet(BigDecimal weight, Integer reps) {
        if (weight != null) this.weight = weight;
        if (reps != null) this.reps = reps;
    }
    // 연관관계 메서드
    public void setWorkoutHistory(WorkoutHistory workoutHistory) {
        this.workoutHistory = workoutHistory;
    }

    public void setSetNumber(Integer setNumber) {
        this.setNumber = setNumber;
    }
}