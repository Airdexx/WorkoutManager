package com.workout.workoutManager.exception.handler;

import com.workout.workoutManager.domain.shop.exception.*;
import com.workout.workoutManager.exception.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 상점 관련 예외를 처리하는 전역 예외 핸들러
 */
@RestControllerAdvice(annotations = {RestController.class}, basePackages = {"com.workout.workoutManager"})
@Slf4j
public class ShopExceptionHandler {

    /**
     * 아이템을 찾을 수 없을 때의 예외 처리
     */
    @ExceptionHandler(ItemNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleItemNotFound(ItemNotFoundException e) {
        log.error("Item not found: {}", e.getMessage());
        return new ErrorResponse("ITEM_NOT_FOUND", e.getMessage());
    }

    /**
     * 포인트 부족 예외 처리
     */
    @ExceptionHandler(NotEnoughPointsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleNotEnoughPoints(NotEnoughPointsException e) {
        return new ErrorResponse("NOT_ENOUGH_POINTS", e.getMessage());
    }

    /**
     * 아이템 구매 불가 예외 처리
     */
    @ExceptionHandler(ItemNotAvailableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleItemNotAvailable(ItemNotAvailableException e) {
        return new ErrorResponse("ITEM_NOT_AVAILABLE", e.getMessage());
    }

    /**
     * 아이템 중복 보유 예외 처리
     */
    @ExceptionHandler(ItemAlreadyOwnedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleItemAlreadyOwned(ItemAlreadyOwnedException e) {
        return new ErrorResponse("ITEM_ALREADY_OWNED", e.getMessage());
    }

    /**
     * 아이템 장착 초과 예외 처리
     */
    @ExceptionHandler(TooManyEquippedItemsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleTooManyEquippedItems(TooManyEquippedItemsException e) {
        return new ErrorResponse("TOO_MANY_EQUIPPED_ITEMS", e.getMessage());
    }

    /**
     * 기타 상점 관련 예외 처리
     */
    @ExceptionHandler({
            ConditionNotFoundException.class,
            InvalidOperationException.class,
            DuplicateAchievementException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleShopException(RuntimeException e) {
        String errorCode = e.getClass().getSimpleName().replace("Exception", "").toUpperCase();
        return new ErrorResponse(errorCode, e.getMessage());
    }
}