package com.workout.workoutManager.domain.Workout.entity;

import com.workout.workoutManager.domain.User.entity.User;
import com.workout.workoutManager.domain.Workout.WorkoutDetail;
import com.workout.workoutManager.domain.Workout.WorkoutType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "workout_history")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WorkoutHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workout_type_id", nullable = false)
    private WorkoutType workoutType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workout_detail_id", nullable = false)
    private WorkoutDetail workoutDetail;

    @Column(name = "workout_date", nullable = false)
    private LocalDateTime workoutDate;

    @OneToMany(mappedBy = "workoutHistory", cascade = CascadeType.ALL)
    private List<WorkoutSet> workoutSets = new ArrayList<>();

    @Builder
    public WorkoutHistory(User user, WorkoutType workoutType, WorkoutDetail workoutDetail,
                          LocalDateTime workoutDate) {
        this.user = user;
        this.workoutType = workoutType;
        this.workoutDetail = workoutDetail;
        this.workoutDate = workoutDate;
    }

    // 연관관계 메서드
    public void setUser(User user) {
        this.user = user;
    }

    public void setWorkoutDate(LocalDateTime workoutDate) {
        this.workoutDate = workoutDate;
    }

    public void addWorkoutSet(WorkoutSet workoutSet) {
        this.workoutSets.add(workoutSet);
        workoutSet.setWorkoutHistory(this);
    }
}