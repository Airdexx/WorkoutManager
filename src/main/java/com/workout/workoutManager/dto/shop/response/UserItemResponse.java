package com.workout.workoutManager.dto.shop.response;

import com.workout.workoutManager.domain.shop.entity.UserItem;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 사용자가 보유한 아이템 정보 응답을 위한 DTO
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserItemResponse {
    private Long id;                // UserItem ID
    private Long itemId;            // 상점 아이템 ID
    private String type;            // 아이템 타입
    private String name;            // 아이템 이름
    private String description;     // 아이템 설명
    private boolean equipped;       // 장착 여부
    private LocalDateTime acquiredAt; // 획득 일시
    private String imagePath;       // 이미지 경로 추가

    @Builder
    public UserItemResponse(UserItem userItem) {
        this.id = userItem.getId();
        this.itemId = userItem.getItem().getId();
        this.type = userItem.getItem().getType().getType().name();
        this.name = userItem.getItem().getName();
        this.description = userItem.getItem().getDescription();
        this.equipped = userItem.isEquipped();
        this.acquiredAt = userItem.getAcquiredAt();
        this.imagePath = userItem.getItem().getImagePath();  // 이미지 경로 설정
    }
}