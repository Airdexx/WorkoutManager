package com.workout.workoutManager.dto.shop.request;
import lombok.*;
/**
 * 상점 아이템 검색 조건을 위한 DTO
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemSearchRequest {
    /**
     * 아이템 타입 필터 (TITLE, EMBLEM)
     */
    private String type;

    /**
     * 한정판 아이템만 조회할지 여부
     */
    private Boolean onlyLimited;

    /**
     * 현재 구매 가능한 아이템만 조회할지 여부
     */
    private Boolean onlyAvailable;

    @Builder
    public ItemSearchRequest(String type, Boolean onlyLimited, Boolean onlyAvailable) {
        this.type = type;
        this.onlyLimited = onlyLimited != null ? onlyLimited : false;
        this.onlyAvailable = onlyAvailable != null ? onlyAvailable : true;
    }
}