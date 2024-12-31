package com.workout.workoutManager.domain.shop.exception;

/**
 * 이미 달성한 업적을 다시 달성하려 할 때 발생하는 예외
 */
public class DuplicateAchievementException extends RuntimeException {
    public DuplicateAchievementException(String message) {
        super(message);
    }
}