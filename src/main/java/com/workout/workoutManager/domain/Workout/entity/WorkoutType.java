package com.workout.workoutManager.domain.Workout;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "workout_type")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WorkoutType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "body_part", nullable = false, unique = true, length = 10)
    private String bodyPart;

    @Builder
    public WorkoutType(String bodyPart) {
        this.bodyPart = bodyPart;
    }
}