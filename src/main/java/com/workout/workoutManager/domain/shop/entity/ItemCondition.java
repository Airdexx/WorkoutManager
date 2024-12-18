package com.workout.workoutManager.domain.shop.entity;

import com.workout.workoutManager.domain.Workout.WorkoutType;
import com.workout.workoutManager.domain.shop.enumerate.AchievementType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
@Entity
@Table(name = "item_condition")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemCondition extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private AchievementType achievementType;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private int requiredCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workout_type_id")
    private WorkoutType workoutType;

    @OneToMany(mappedBy = "condition")
    private List<ShopItem> items = new ArrayList<>();

    @Builder
    public ItemCondition(AchievementType achievementType, String description, 
                        int requiredCount, WorkoutType workoutType) {
        this.achievementType = achievementType;
        this.description = description;
        this.requiredCount = requiredCount;
        this.workoutType = workoutType;
    }
}