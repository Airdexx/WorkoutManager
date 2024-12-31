package com.workout.workoutManager.service.shop;

import com.workout.workoutManager.domain.User.entity.User;
import com.workout.workoutManager.domain.User.exception.UserNotFoundException;
import com.workout.workoutManager.domain.User.repository.UserRepository;
import com.workout.workoutManager.domain.Workout.WorkoutType;
import com.workout.workoutManager.domain.Workout.exception.WorkoutTypeNotFoundException;
import com.workout.workoutManager.domain.Workout.repository.WorkoutTypeRepository;
import com.workout.workoutManager.domain.shop.entity.*;
import com.workout.workoutManager.domain.shop.enumerate.AchievementType;
import com.workout.workoutManager.domain.shop.enumerate.ItemTypeEnum;
import com.workout.workoutManager.domain.shop.exception.*;
import com.workout.workoutManager.domain.shop.repository.*;
import com.workout.workoutManager.dto.shop.request.EquipItemRequest;
import com.workout.workoutManager.dto.shop.request.ItemSearchRequest;
import com.workout.workoutManager.dto.shop.request.PurchaseItemRequest;
import com.workout.workoutManager.dto.shop.response.PurchaseHistoryResponse;
import com.workout.workoutManager.dto.shop.response.ShopItemResponse;
import com.workout.workoutManager.dto.shop.response.UserItemResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 상점 시스템 관련 서비스
 * 아이템 조회, 구매, 장착 및 업적 달성 관련 기능을 제공합니다.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ShopService {
    private final ShopItemRepository shopItemRepository;
    private final UserItemRepository userItemRepository;
    private final PurchaseHistoryRepository purchaseHistoryRepository;
    private final ItemConditionRepository itemConditionRepository;
    private final UserFirstWorkoutRepository userFirstWorkoutRepository;
    private final UserRepository userRepository;
    private final WorkoutTypeRepository workoutTypeRepository;
    /**
     * 상점 아이템 목록을 조회합니다.
     * 검색 조건에 따라 필터링된 결과를 반환합니다.
     *
     * @param searchRequest 검색 조건
     * @param userId 조회하는 사용자 ID
     * @return 필터링된 상점 아이템 목록
     */
    public List<ShopItemResponse> getShopItems(ItemSearchRequest searchRequest, Long userId) {
        // 현재 시간 기준으로 판매 가능한 아이템 필터링
        List<ShopItem> items = shopItemRepository.findAll().stream()
                .filter(item -> isItemAvailable(item, searchRequest))
                .collect(Collectors.toList());

        return items.stream()
                .map(item -> {
                    boolean owned = userItemRepository.existsByUserAndItem(
                            userRepository.findById(userId).orElseThrow(() ->
                                    new UserNotFoundException("사용자를 찾을 수 없습니다: " + userId)),
                            item
                    );
                    return new ShopItemResponse(item, owned);
                })
                .collect(Collectors.toList());
    }

    /**
     * 아이템이 현재 판매 가능한 상태인지 확인합니다.
     */
    private boolean isItemAvailable(ShopItem item, ItemSearchRequest searchRequest) {
        // 타입 필터링
        if (searchRequest.getType() != null &&
                !item.getType().getType().name().equals(searchRequest.getType())) {
            return false;
        }

        // 한정판 필터링
        if (searchRequest.getOnlyLimited() != null &&
                searchRequest.getOnlyLimited() && !item.isLimited()) {
            return false;
        }

        // 판매 가능 여부 확인
        if (searchRequest.getOnlyAvailable() != null && searchRequest.getOnlyAvailable()) {
            LocalDateTime now = LocalDateTime.now();
            if (item.isLimited()) {
                return now.isAfter(item.getStartDate()) && now.isBefore(item.getEndDate());
            }
        }

        return true;
    }

    /**
     * 사용자의 보유 아이템 목록을 조회합니다.
     *
     * @param userId 사용자 ID
     * @param type 아이템 타입 (선택적)
     * @param equippedOnly 장착된 아이템만 조회할지 여부
     * @return 사용자의 보유 아이템 목록
     */
    public List<UserItemResponse> getUserItems(Long userId, ItemTypeEnum type, Boolean equippedOnly) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다: " + userId));

        Stream<UserItem> itemStream = userItemRepository.findByUser(user).stream();

        // 타입 필터링
        if (type != null) {
            itemStream = itemStream.filter(userItem ->
                    userItem.getItem().getType().getType() == type);
        }

        // 장착 상태 필터링
        if (equippedOnly != null && equippedOnly) {
            itemStream = itemStream.filter(UserItem::isEquipped);
        }

        return itemStream
                .map(UserItemResponse::new)
                .collect(Collectors.toList());
    }

    /**
     * 사용자의 구매 이력을 조회합니다.
     *
     * @param userId 사용자 ID
     * @return 구매 이력 목록
     */
    public List<PurchaseHistoryResponse> getPurchaseHistory(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다: " + userId));

        return purchaseHistoryRepository.findByUserOrderByPurchasedAtDesc(user).stream()
                .map(PurchaseHistoryResponse::new)
                .collect(Collectors.toList());
    }

    /**
     * 아이템을 구매합니다.
     *
     * @param userId 사용자 ID
     * @param request 구매 요청 정보
     * @return 구매한 아이템 정보
     * @throws NotEnoughPointsException 포인트가 부족한 경우
     * @throws ItemNotAvailableException 아이템을 구매할 수 없는 경우
     */
    @Transactional
    public UserItemResponse purchaseItem(Long userId, PurchaseItemRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다: " + userId));

        ShopItem item = shopItemRepository.findById(request.getItemId())
                .orElseThrow(() -> new ItemNotFoundException("아이템을 찾을 수 없습니다: " + request.getItemId()));

        // 구매 가능 여부 확인
        validatePurchase(user, item);

        // 포인트 차감
        user.subtractPoints(item.getPrice());
        userRepository.save(user);

        // 구매 이력 생성
        PurchaseHistory purchaseHistory = PurchaseHistory.builder()
                .user(user)
                .item(item)
                .price(item.getPrice())
                .build();
        purchaseHistoryRepository.save(purchaseHistory);

        // UserItem 생성
        UserItem userItem = UserItem.builder()
                .user(user)
                .item(item)
                .build();
        userItemRepository.save(userItem);

        return new UserItemResponse(userItem);
    }

    /**
     * 아이템 구매 가능 여부를 확인합니다.
     */
    private void validatePurchase(User user, ShopItem item) {
        // 이미 보유 중인지 확인
        if (userItemRepository.existsByUserAndItem(user, item)) {
            throw new ItemAlreadyOwnedException("이미 보유 중인 아이템입니다: " + item.getName());
        }

        // 포인트 확인
        if (user.getPoint() < item.getPrice()) {
            throw new NotEnoughPointsException("포인트가 부족합니다.");
        }

        // 한정판 아이템 판매 기간 확인
        if (item.isLimited()) {
            LocalDateTime now = LocalDateTime.now();
            if (now.isBefore(item.getStartDate()) || now.isAfter(item.getEndDate())) {
                throw new ItemNotAvailableException("현재 구매할 수 없는 아이템입니다.");
            }
        }
    }

    /**
     * 아이템을 장착하거나 해제합니다.
     *
     * @param userId 사용자 ID
     * @param request 장착/해제 요청 정보
     * @return 변경된 아이템 정보
     */
    @Transactional
    public UserItemResponse equipItem(Long userId, EquipItemRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다: " + userId));

        UserItem userItem = userItemRepository.findByUser_IdAndItem_Id(userId, request.getItemId())
                .orElseThrow(() -> new ItemNotFoundException("보유하지 않은 아이템입니다."));

        if (request.getEquip()) {
            // 장착 처리
            validateAndEquipItem(user, userItem);
        } else {
            // 해제 처리
            userItem.unequip();
        }

        userItemRepository.save(userItem);
        return new UserItemResponse(userItem);
    }

    /**
     * 아이템 장착 가능 여부를 확인하고 장착 처리를 합니다.
     */
    private void validateAndEquipItem(User user, UserItem userItem) {
        ItemType itemType = userItem.getItem().getType();
        List<UserItem> equippedItems = userItemRepository.findByUserAndEquippedTrue(user).stream()
                .filter(item -> item.getItem().getType().equals(itemType))
                .collect(Collectors.toList());

        // 최대 장착 가능 개수 확인
        if (equippedItems.size() >= itemType.getMaxEquipCount() && !equippedItems.contains(userItem)) {
            if (itemType.getType() == ItemTypeEnum.TITLE) {
                // 칭호의 경우 기존 장착된 것을 해제
                equippedItems.forEach(UserItem::unequip);
            } else {
                throw new TooManyEquippedItemsException("더 이상 장착할 수 없습니다.");
            }
        }

        userItem.equip();
    }

    /**
     * 업적 달성을 확인하고 보상을 지급합니다.
     */
    @Transactional
    public void checkAndRewardAchievement(Long userId, AchievementType achievementType, Long workoutTypeId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다: " + userId));

        List<ItemCondition> conditions = itemConditionRepository
                .findByAchievementTypeAndWorkoutType(achievementType,
                        workoutTypeId != null ? new WorkoutType(workoutTypeId) : null);

        for (ItemCondition condition : conditions) {
            // 조건 달성 여부 확인 로직
            if (isAchievementComplete(user, condition)) {
                rewardAchievement(user, condition);
            }
        }
    }

    /**
     * 스트릭(연속 달성) 업적을 확인하고 보상을 지급합니다.
     */
    @Transactional
    public void checkAndRewardStreakAchievement(Long userId, int streakCount) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다: " + userId));

        List<ItemCondition> streakConditions = itemConditionRepository.findByAchievementType(AchievementType.ACHIEVEMENT)
                .stream()
                .filter(condition -> condition.getRequiredCount() <= streakCount)
                .collect(Collectors.toList());

        for (ItemCondition condition : streakConditions) {
            if (!userHasAchievement(user, condition)) {
                rewardAchievement(user, condition);
            }
        }
    }

    /**
     * 최초 운동 수행을 확인하고 보상을 지급합니다.
     */
    @Transactional
    public void checkAndRewardFirstWorkout(Long userId, Long workoutTypeId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다: " + userId));

        WorkoutType workoutType = workoutTypeRepository.findById(workoutTypeId)
                .orElseThrow(() -> new WorkoutTypeNotFoundException("운동 타입을 찾을 수 없습니다: " + workoutTypeId));

        if (!userFirstWorkoutRepository.existsByUserAndWorkoutType(user, workoutType)) {
            // 최초 수행 기록 생성
            UserFirstWorkout firstWorkout = UserFirstWorkout.builder()
                    .user(user)
                    .workoutType(workoutType)
                    .build();
            userFirstWorkoutRepository.save(firstWorkout);

            // 보상 지급
            List<ItemCondition> conditions = itemConditionRepository
                    .findByAchievementTypeAndWorkoutType(AchievementType.ACHIEVEMENT, workoutType);

            if (conditions.isEmpty()) {
                throw new ConditionNotFoundException("해당하는 업적 조건을 찾을 수 없습니다.");
            }

            // 모든 해당하는 조건에 대해 보상 지급
            for (ItemCondition condition : conditions) {
                rewardAchievement(user, condition);
            }
        }
    }

    /**
     * 업적 보상을 지급합니다.
     */
    private void rewardAchievement(User user, ItemCondition condition) {
        // 아이템 지급
        ShopItem item = shopItemRepository.findByCondition(condition)
                .orElseThrow(() -> new ItemNotFoundException("보상 아이템을 찾을 수 없습니다."));

        UserItem userItem = UserItem.builder()
                .user(user)
                .item(item)
                .build();
        userItemRepository.save(userItem);

        // 포인트 보상 지급
        user.addPoint(500); // 기본 보상 포인트
        userRepository.save(user);
    }

    /**
     * 사용자가 해당 업적을 이미 달성했는지 확인합니다.
     */
    private boolean userHasAchievement(User user, ItemCondition condition) {
        return userItemRepository.existsByUserAndItem_Condition(user, condition);
    }

    /**
     * 업적 조건이 달성되었는지 확인합니다.
     */
    private boolean isAchievementComplete(User user, ItemCondition condition) {
        // 각 업적 타입별 달성 여부 확인 로직 구현
        return false; // 실제 구현에서는 조건에 따른 달성 여부 반환
    }
}