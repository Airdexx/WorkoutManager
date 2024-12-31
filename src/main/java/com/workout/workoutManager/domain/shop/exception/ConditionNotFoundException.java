package com.workout.workoutManager.domain.shop.exception;

/**
 * 요청한 업적 조건을 찾을 수 없을 때 발생하는 예외
 * 업적 보상 지급 시 해당하는 조건이 없을 때 발생
 */
public class ConditionNotFoundException extends RuntimeException {
    public ConditionNotFoundException(String message) {
        super(message);
    }
}