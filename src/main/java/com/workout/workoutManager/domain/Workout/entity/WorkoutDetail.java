package com.workout.workoutManager.domain.Workout;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "workout_detail")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WorkoutDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 45)
    private String name;

    @Column(length = 255)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workout_type_id")
    private WorkoutType workoutType;

    @Builder
    public WorkoutDetail(String name, String description, WorkoutType workoutType) {
        this.name = name;
        this.description = description;
        this.workoutType = workoutType;
    }
}