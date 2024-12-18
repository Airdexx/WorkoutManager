package com.workout.workoutManager.domain.shop.entity;

import com.workout.workoutManager.domain.User.entity.User;
import com.workout.workoutManager.domain.Workout.WorkoutType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_first_workout")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserFirstWorkout extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workout_type_id", nullable = false)
    private WorkoutType workoutType;

    @Column(name = "achieved_at", nullable = false)
    private LocalDateTime achievedAt;

    @Builder
    public UserFirstWorkout(User user, WorkoutType workoutType) {
        this.user = user;
        this.workoutType = workoutType;
        this.achievedAt = LocalDateTime.now();
    }
}