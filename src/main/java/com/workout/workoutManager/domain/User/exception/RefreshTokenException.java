package com.workout.workoutManager.domain.User.exception;

public class RefreshTokenException extends RuntimeException {
    public RefreshTokenException(String message) {
        super(message);
    }
}