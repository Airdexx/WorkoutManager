package com.workout.workoutManager.domain.shop.exception;

/**
 * 해당 타입의 아이템을 더 이상 장착할 수 없을 때 발생하는 예외
 * 칭호는 1개, 엠블럼은 3개까지만 장착 가능할 때 제한을 초과하면 발생
 */
public class TooManyEquippedItemsException extends RuntimeException {
    public TooManyEquippedItemsException(String message) {
        super(message);
    }
}
