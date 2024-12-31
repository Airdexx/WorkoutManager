package com.workout.workoutManager.domain.shop.exception;

/**
 * 요청한 아이템을 찾을 수 없을 때 발생하는 예외
 * 아이템 조회, 구매, 장착 시 존재하지 않는 아이템을 참조할 때 발생
 */
public class ItemNotFoundException extends RuntimeException {
    public ItemNotFoundException(String message) {
        super(message);
    }
}