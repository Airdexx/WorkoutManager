package com.workout.workoutManager.domain.shop.exception;

/**
 * 잘못된 작업을 수행하려 할 때 발생하는 예외
 * 예: 장착되지 않은 아이템을 해제하려 할 때
 */
public class InvalidOperationException extends RuntimeException {
    public InvalidOperationException(String message) {
        super(message);
    }
}