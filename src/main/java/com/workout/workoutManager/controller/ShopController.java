package com.workout.workoutManager.controller;

import com.workout.workoutManager.config.JwtConfig.JwtTokenProvider;
import com.workout.workoutManager.domain.shop.enumerate.AchievementType;
import com.workout.workoutManager.domain.shop.enumerate.ItemTypeEnum;
import com.workout.workoutManager.dto.shop.request.EquipItemRequest;
import com.workout.workoutManager.dto.shop.request.ItemSearchRequest;
import com.workout.workoutManager.dto.shop.request.PurchaseItemRequest;
import com.workout.workoutManager.dto.shop.response.PurchaseHistoryResponse;
import com.workout.workoutManager.dto.shop.response.ShopItemResponse;
import com.workout.workoutManager.dto.shop.response.UserItemResponse;
import com.workout.workoutManager.service.shop.ShopService;
import lombok.extern.slf4j.Slf4j;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Tag(name = "상점", description = "상점 시스템 API")
@RestController
@RequestMapping("/api/shop")
@RequiredArgsConstructor
public class ShopController {

    private final ShopService shopService;
    private final JwtTokenProvider jwtTokenProvider;

    @Operation(summary = "상점 아이템 목록 조회", description = "판매 중인 아이템 목록을 조회합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "조회 성공"),
        @ApiResponse(responseCode = "401", description = "인증되지 않은 요청"),
        @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음")
    })
    @GetMapping("/items")
    public ResponseEntity<List<ShopItemResponse>> getShopItems(
            @Parameter(description = "검색 조건")
            @ModelAttribute ItemSearchRequest searchRequest,
            HttpServletRequest request) {
        log.info("상점 아이템 목록 조회 요청");
        log.info("검색 조건: {}", searchRequest);

        String token = extractToken(request);
        log.info("토큰 추출 완료: {}", token.substring(0, Math.min(token.length(), 10)) + "...");

        Long userId = jwtTokenProvider.getUserId(token);
        log.info("사용자 ID 추출: {}", userId);

        List<ShopItemResponse> items = shopService.getShopItems(searchRequest, userId);
        log.info("조회된 아이템 수: {}", items.size());

        return ResponseEntity.ok(items);
    }

    @Operation(summary = "보유 아이템 목록 조회", description = "사용자가 보유한 아이템 목록을 조회합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "조회 성공"),
        @ApiResponse(responseCode = "401", description = "인증되지 않은 요청"),
        @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음")
    })
    @GetMapping("/users/items")
    public ResponseEntity<List<UserItemResponse>> getUserItems(
            @Parameter(description = "아이템 타입 필터")
            @RequestParam(required = false) ItemTypeEnum type,
            @Parameter(description = "장착한 아이템만 조회")
            @RequestParam(required = false) Boolean equippedOnly,
            HttpServletRequest request) {
        String token = extractToken(request);
        Long userId = jwtTokenProvider.getUserId(token);
        return ResponseEntity.ok(shopService.getUserItems(userId, type, equippedOnly));
    }

    @Operation(summary = "구매 이력 조회", description = "사용자의 아이템 구매 이력을 조회합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "조회 성공"),
        @ApiResponse(responseCode = "401", description = "인증되지 않은 요청"),
        @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음")
    })
    @GetMapping("/users/purchases")
    public ResponseEntity<List<PurchaseHistoryResponse>> getPurchaseHistory(HttpServletRequest request) {
        String token = extractToken(request);
        Long userId = jwtTokenProvider.getUserId(token);
        return ResponseEntity.ok(shopService.getPurchaseHistory(userId));
    }

    @Operation(summary = "아이템 구매", description = "상점에서 아이템을 구매합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "구매 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청"),
        @ApiResponse(responseCode = "401", description = "인증되지 않은 요청"),
        @ApiResponse(responseCode = "404", description = "사용자 또는 아이템을 찾을 수 없음"),
        @ApiResponse(responseCode = "409", description = "이미 보유 중인 아이템")
    })
    @PostMapping("/purchase")
    public ResponseEntity<UserItemResponse> purchaseItem(
            @Parameter(description = "구매 요청 정보", required = true)
            @Valid @RequestBody PurchaseItemRequest request,
            HttpServletRequest httpRequest) {
        String token = extractToken(httpRequest);
        Long userId = jwtTokenProvider.getUserId(token);
        return ResponseEntity.ok(shopService.purchaseItem(userId, request));
    }

    @Operation(summary = "아이템 장착/해제", description = "보유한 아이템을 장착하거나 해제합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "장착/해제 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청"),
        @ApiResponse(responseCode = "401", description = "인증되지 않은 요청"),
        @ApiResponse(responseCode = "404", description = "사용자 또는 아이템을 찾을 수 없음"),
        @ApiResponse(responseCode = "409", description = "최대 장착 개수 초과")
    })
    @PutMapping("/users/items/equip")
    public ResponseEntity<UserItemResponse> equipItem(
            @Parameter(description = "장착/해제 요청 정보", required = true)
            @Valid @RequestBody EquipItemRequest request,
            HttpServletRequest httpRequest) {
        String token = extractToken(httpRequest);
        Long userId = jwtTokenProvider.getUserId(token);
        return ResponseEntity.ok(shopService.equipItem(userId, request));
    }

    @Operation(summary = "업적 달성 확인", description = "특정 업적 달성 여부를 확인하고 보상을 지급합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "확인 완료"),
        @ApiResponse(responseCode = "401", description = "인증되지 않은 요청"),
        @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음")
    })
    @PostMapping("/achievements/check")
    public ResponseEntity<Void> checkAchievement(
            @Parameter(description = "업적 타입", required = true)
            @RequestParam AchievementType achievementType,
            @Parameter(description = "운동 타입 ID")
            @RequestParam(required = false) Long workoutTypeId,
            HttpServletRequest request) {
        String token = extractToken(request);
        Long userId = jwtTokenProvider.getUserId(token);
        shopService.checkAndRewardAchievement(userId, achievementType, workoutTypeId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "스트릭 업적 확인", description = "연속 달성 업적을 확인하고 보상을 지급합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "확인 완료"),
        @ApiResponse(responseCode = "401", description = "인증되지 않은 요청"),
        @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음")
    })
    @PostMapping("/achievements/streak")
    public ResponseEntity<Void> checkStreakAchievement(
            @Parameter(description = "연속 달성 일수", required = true)
            @RequestParam int streakCount,
            HttpServletRequest request) {
        String token = extractToken(request);
        Long userId = jwtTokenProvider.getUserId(token);
        shopService.checkAndRewardStreakAchievement(userId, streakCount);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "최초 운동 업적 확인", description = "최초 운동 수행 업적을 확인하고 보상을 지급합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "확인 완료"),
        @ApiResponse(responseCode = "401", description = "인증되지 않은 요청"),
        @ApiResponse(responseCode = "404", description = "사용자 또는 운동 타입을 찾을 수 없음")
    })
    @PostMapping("/achievements/first-workout")
    public ResponseEntity<Void> checkFirstWorkout(
            @Parameter(description = "운동 타입 ID", required = true)
            @RequestParam Long workoutTypeId,
            HttpServletRequest request) {
        String token = extractToken(request);
        Long userId = jwtTokenProvider.getUserId(token);
        shopService.checkAndRewardFirstWorkout(userId, workoutTypeId);
        return ResponseEntity.ok().build();
    }

    // JWT 토큰 추출 메서드
    private String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        throw new IllegalArgumentException("올바른 JWT 토큰이 없습니다.");
    }
}