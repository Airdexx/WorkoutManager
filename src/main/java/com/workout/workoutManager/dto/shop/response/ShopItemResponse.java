package com.workout.workoutManager.dto.shop.response;

import com.workout.workoutManager.domain.shop.entity.ShopItem;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 상점 아이템 정보 응답을 위한 DTO
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ShopItemResponse {
    private Long id;                    // 아이템 ID
    private String type;                // 아이템 타입 (TITLE, EMBLEM)
    private String name;                // 아이템 이름
    private String description;         // 아이템 설명
    private int price;                  // 아이템 가격
    private boolean limited;            // 한정판 여부
    private LocalDateTime startDate;    // 판매 시작일 (한정판의 경우)
    private LocalDateTime endDate;      // 판매 종료일 (한정판의 경우)
    private String achievementCondition;// 획득 조건 설명
    private boolean owned;              // 사용자 보유 여부

    @Builder
    public ShopItemResponse(ShopItem item, boolean owned) {
        this.id = item.getId();
        this.type = item.getType().getType().name();
        this.name = item.getName();
        this.description = item.getDescription();
        this.price = item.getPrice();
        this.limited = item.isLimited();
        this.startDate = item.getStartDate();
        this.endDate = item.getEndDate();
        this.achievementCondition = item.getCondition().getDescription();
        this.owned = owned;
    }
}