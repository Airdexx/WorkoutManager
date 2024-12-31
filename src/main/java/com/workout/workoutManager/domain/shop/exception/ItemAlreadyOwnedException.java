package com.workout.workoutManager.domain.shop.exception;

/**
 * 이미 보유하고 있는 아이템을 다시 구매하려 할 때 발생하는 예외
 */
public class ItemAlreadyOwnedException extends RuntimeException {
    public ItemAlreadyOwnedException(String message) {
        super(message);
    }
}