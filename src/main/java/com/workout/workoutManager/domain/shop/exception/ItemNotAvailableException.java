package com.workout.workoutManager.domain.shop.exception;

/**
 * 아이템을 현재 구매할 수 없을 때 발생하는 예외
 * 한정판 아이템의 판매 기간이 아니거나, 
 * 업적 달성을 통해서만 획득 가능한 아이템을 구매하려 할 때 발생
 */
public class ItemNotAvailableException extends RuntimeException {
    public ItemNotAvailableException(String message) {
        super(message);
    }
}