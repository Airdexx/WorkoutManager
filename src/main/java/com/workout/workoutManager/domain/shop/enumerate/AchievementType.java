package com.workout.workoutManager.domain.shop.enumerate;

public enum AchievementType {
    PURCHASE("구매"),
    ACHIEVEMENT("업적"),
    EVENT("이벤트");

    private final String description;

    AchievementType(String description) {
        this.description = description;
    }
}