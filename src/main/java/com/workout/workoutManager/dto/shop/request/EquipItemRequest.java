package com.workout.workoutManager.dto.shop.request;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
/**
 * 아이템 장착/해제 요청을 위한 DTO
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EquipItemRequest {
    /**
     * 장착/해제하고자 하는 아이템의 ID
     */
    @NotNull(message = "아이템 ID는 필수입니다.")
    private Long itemId;

    /**
     * 장착 여부 (true: 장착, false: 해제)
     */
    @NotNull(message = "장착 여부는 필수입니다.")
    private Boolean equip;

    @Builder
    public EquipItemRequest(Long itemId, Boolean equip) {
        this.itemId = itemId;
        this.equip = equip;
    }
}

