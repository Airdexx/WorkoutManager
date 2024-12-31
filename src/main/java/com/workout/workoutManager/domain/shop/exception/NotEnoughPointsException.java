package com.workout.workoutManager.domain.shop.exception;

/**
 * 사용자의 포인트가 부족할 때 발생하는 예외
 * 아이템 구매 시 사용자의 보유 포인트가 아이템 가격보다 적을 때 발생
 */
public class NotEnoughPointsException extends RuntimeException {
    public NotEnoughPointsException(String message) {
        super(message);
    }
}