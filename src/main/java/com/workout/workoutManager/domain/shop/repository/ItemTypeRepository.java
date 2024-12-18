package com.workout.workoutManager.domain.shop.repository;

import com.workout.workoutManager.domain.shop.entity.ItemType;
import com.workout.workoutManager.domain.shop.enumerate.ItemTypeEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 아이템 타입(칭호, 엠블럼 등) 관리를 위한 Repository
 */
@Repository
public interface ItemTypeRepository extends JpaRepository<ItemType, Long> {
    /**
     * 아이템 타입 Enum으로 아이템 타입을 조회합니다.
     *
     * @param type 조회할 아이템 타입 (TITLE, EMBLEM)
     * @return 해당하는 아이템 타입 Optional
     */
    Optional<ItemType> findByType(ItemTypeEnum type);
}