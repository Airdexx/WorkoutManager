package com.workout.workoutManager.domain.User.exception;

public class UserDuplicateException extends RuntimeException {
    public UserDuplicateException(String field, String value) {
        super(String.format("%s already exists: %s", field, value));
    }
}