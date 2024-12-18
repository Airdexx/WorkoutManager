package com.workout.workoutManager.dto.shop.request;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
/**
 * 아이템 구매 요청을 위한 DTO
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PurchaseItemRequest {
    /**
     * 구매하고자 하는 상점 아이템의 ID
     */
    @NotNull(message = "아이템 ID는 필수입니다.")
    private Long itemId;

    @Builder
    public PurchaseItemRequest(Long itemId) {
        this.itemId = itemId;
    }
}