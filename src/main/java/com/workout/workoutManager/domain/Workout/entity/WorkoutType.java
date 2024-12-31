package com.workout.workoutManager.domain.Workout;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "workout_type")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WorkoutType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 10)
    private String bodyPart;

    // 기존 생성자 외에 id로 생성하는 생성자 추가
    public WorkoutType(Long id) {
        this.id = id;
    }

    // 기존 생성자
    public WorkoutType(String bodyPart) {
        this.bodyPart = bodyPart;
    }
}