package com.workout.workoutManager.dto.shop.response;

import com.workout.workoutManager.domain.shop.entity.PurchaseHistory;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 구매 이력 정보 응답을 위한 DTO
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PurchaseHistoryResponse {
    private Long id;                // 구매 이력 ID
    private Long itemId;            // 구매한 아이템 ID
    private String itemName;        // 아이템 이름
    private String itemType;        // 아이템 타입
    private int price;              // 구매 가격
    private LocalDateTime purchasedAt; // 구매 일시

    @Builder
    public PurchaseHistoryResponse(PurchaseHistory history) {
        this.id = history.getId();
        this.itemId = history.getItem().getId();
        this.itemName = history.getItem().getName();
        this.itemType = history.getItem().getType().getType().name();
        this.price = history.getPrice();
        this.purchasedAt = history.getPurchasedAt();
    }
}