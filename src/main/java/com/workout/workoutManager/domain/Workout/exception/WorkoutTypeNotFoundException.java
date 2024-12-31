package com.workout.workoutManager.domain.Workout.exception;

/**
 * 요청한 운동 타입을 찾을 수 없을 때 발생하는 예외
 */
public class WorkoutTypeNotFoundException extends RuntimeException {
    public WorkoutTypeNotFoundException(String message) {
        super(message);
    }
}