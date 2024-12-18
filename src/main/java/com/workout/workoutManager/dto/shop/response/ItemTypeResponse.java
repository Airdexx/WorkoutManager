package com.workout.workoutManager.dto.shop.response;

import com.workout.workoutManager.domain.shop.entity.ItemType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 아이템 타입 정보 응답을 위한 DTO
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemTypeResponse {
    private Long id;            // 아이템 타입 ID
    private String type;        // 아이템 타입 (TITLE, EMBLEM)
    private String description; // 아이템 타입 설명

    @Builder
    public ItemTypeResponse(ItemType itemType) {
        this.id = itemType.getId();
        this.type = itemType.getType().name();
        this.description = itemType.getType().getDescription();
    }
}