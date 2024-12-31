package com.workout.workoutManager.domain.shop.repository;

import com.workout.workoutManager.domain.User.entity.User;
import com.workout.workoutManager.domain.shop.entity.ItemCondition;
import com.workout.workoutManager.domain.shop.entity.ShopItem;
import com.workout.workoutManager.domain.shop.entity.UserItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 사용자의 아이템 보유/장착 상태 관리를 위한 Repository
 */
@Repository
public interface UserItemRepository extends JpaRepository<UserItem, Long> {
    /**
     * 특정 사용자가 보유한 모든 아이템을 조회합니다.
     *
     * @param user 조회할 사용자
     * @return 사용자가 보유한 아이템 목록
     */
    List<UserItem> findByUser(User user);

    /**
     * 사용자가 현재 장착 중인 아이템을 조회합니다.
     *
     * @param user 조회할 사용자
     * @return 사용자가 장착 중인 아이템 목록
     */
    List<UserItem> findByUserAndEquippedTrue(User user);

    /**
     * 특정 사용자의 특정 아이템 보유 상태를 조회합니다.
     *
     * @param user 조회할 사용자
     * @param item 조회할 아이템
     * @return 해당하는 UserItem Optional
     */
    Optional<UserItem> findByUserAndItem(User user, ShopItem item);

    /**
     * 특정 사용자의 특정 아이템 보유 여부를 확인합니다.
     *
     * @param user 확인할 사용자
     * @param item 확인할 아이템
     * @return 보유 여부 (true/false)
     */
    boolean existsByUserAndItem(User user, ShopItem item);

    /**
     * 특정 사용자 ID와 아이템 ID로 보유 아이템을 조회합니다.
     *
     * @param userId 조회할 사용자의 ID
     * @param itemId 조회할 아이템의 ID
     * @return 해당하는 UserItem Optional
     */
    Optional<UserItem> findByUser_IdAndItem_Id(Long userId, Long itemId);


    /**
     * 특정 사용자가 특정 조건의 아이템을 보유하고 있는지 확인합니다.
     *
     * @param user 확인할 사용자
     * @param condition 확인할 아이템 조건
     * @return 보유 여부 (true/false)
     */
    boolean existsByUserAndItem_Condition(User user, ItemCondition condition);
}