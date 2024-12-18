package com.workout.workoutManager.domain.shop.enumerate;

import lombok.Getter;

public enum ItemTypeEnum {
    TITLE("칭호"),
    EMBLEM("엠블럼");

    @Getter  // lombok annotation 추가
    private final String description;

    ItemTypeEnum(String description) {
        this.description = description;
    }
}