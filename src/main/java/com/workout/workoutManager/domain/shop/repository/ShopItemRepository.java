package com.workout.workoutManager.domain.shop.repository;

import com.workout.workoutManager.domain.shop.entity.ItemCondition;
import com.workout.workoutManager.domain.shop.entity.ItemType;
import com.workout.workoutManager.domain.shop.entity.ShopItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 상점 아이템 관리를 위한 Repository
 */
@Repository
public interface ShopItemRepository extends JpaRepository<ShopItem, Long> {
    /**
     * 특정 조건으로 획득 가능한 아이템을 조회합니다.
     *
     * @param condition 아이템 획득 조건
     * @return 해당하는 상점 아이템 Optional
     */
    Optional<ShopItem> findByCondition(ItemCondition condition);

    /**
     * 아이템 타입별로 상점 아이템을 조회합니다.
     *
     * @param type 아이템 타입 (칭호, 엠블럼 등)
     * @return 해당 타입의 아이템 목록
     */
    List<ShopItem> findByType(ItemType type);

    /**
     * 모든 한정판 아이템을 조회합니다.
     *
     * @return 한정판 아이템 목록
     */
    List<ShopItem> findByLimitedTrue();

    /**
     * 현재 판매 중인 한정판 아이템을 조회합니다.
     * 현재 시간이 판매 시작일과 종료일 사이에 있는 아이템만 반환합니다.
     *
     * @param now 현재 시간
     * @param now2 현재 시간 (동일한 값이지만 JPA 메서드 시그니처를 위해 두 번 필요)
     * @return 현재 판매 중인 한정판 아이템 목록
     */
    List<ShopItem> findByLimitedTrueAndStartDateBeforeAndEndDateAfter(LocalDateTime now, LocalDateTime now2);

    /**
     * 아이템 이름으로 상점 아이템을 조회합니다.
     *
     * @param name 아이템 이름
     * @return 해당하는 상점 아이템 Optional
     */
    Optional<ShopItem> findByName(String name);
}