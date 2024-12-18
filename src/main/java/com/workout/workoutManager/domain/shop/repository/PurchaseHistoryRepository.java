package com.workout.workoutManager.domain.shop.repository;

import com.workout.workoutManager.domain.User.entity.User;
import com.workout.workoutManager.domain.shop.entity.PurchaseHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 아이템 구매 이력 관리를 위한 Repository
 */
@Repository
public interface PurchaseHistoryRepository extends JpaRepository<PurchaseHistory, Long> {
    /**
     * 특정 사용자의 모든 구매 이력을 조회합니다.
     *
     * @param user 조회할 사용자
     * @return 사용자의 구매 이력 목록
     */
    List<PurchaseHistory> findByUser(User user);

    /**
     * 특정 사용자의 구매 이력을 최신순으로 조회합니다.
     *
     * @param user 조회할 사용자
     * @return 사용자의 구매 이력 목록 (최신순 정렬)
     */
    List<PurchaseHistory> findByUserOrderByPurchasedAtDesc(User user);
}
