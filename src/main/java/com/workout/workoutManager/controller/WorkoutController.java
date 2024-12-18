package com.workout.workoutManager.controller;

import com.workout.workoutManager.domain.Workout.entity.WorkoutHistory;
import com.workout.workoutManager.domain.Workout.entity.WorkoutSet;
import com.workout.workoutManager.dto.Workout.request.WorkoutRecordRequest;
import com.workout.workoutManager.dto.Workout.request.WorkoutSetUpdateRequest;
import com.workout.workoutManager.dto.Workout.response.WorkoutHistoryResponse;
import com.workout.workoutManager.dto.Workout.response.WorkoutSetResponse;
import com.workout.workoutManager.service.Workout.WorkoutService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "운동 기록 API", description = "운동 기록 생성 및 조회 관련 API")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class WorkoutController {
    private final WorkoutService workoutService;

    @Operation(
            summary = "운동 기록 생성",
            description = "새로운 운동 기록을 저장합니다. 운동 종류, 세부 운동, 세트 정보를 포함하여 저장하며, 스트릭과 포인트도 함께 업데이트됩니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "운동 기록 저장 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음")
    })
    @PostMapping("/users/{userId}/workouts")
    public ResponseEntity<Void> createWorkout(
            @Parameter(description = "사용자 ID", required = true)
            @PathVariable Long userId,
            @Parameter(description = "운동 기록 정보", required = true)
            @RequestBody WorkoutRecordRequest request) {
        WorkoutHistory workoutHistory = WorkoutHistory.builder()
                .workoutType(request.getWorkoutType())
                .workoutDetail(request.getWorkoutDetail())
                .build();

        List<WorkoutSet> workoutSets = request.getSets().stream()
                .map(setRequest -> WorkoutSet.builder()
                        .weight(setRequest.getWeight())
                        .reps(setRequest.getReps())
                        .build())
                .toList();

        workoutService.recordWorkout(userId, workoutHistory, workoutSets);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "사용자 운동 기록 조회",
            description = "특정 사용자의 전체 운동 기록을 조회합니다. 운동 날짜 기준 내림차순으로 정렬되어 반환됩니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = WorkoutHistoryResponse.class))
            ),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음")
    })
    @GetMapping("/users/{userId}/workouts")
    public ResponseEntity<List<WorkoutHistoryResponse>> getWorkoutHistory(
            @Parameter(description = "사용자 ID", required = true)
            @PathVariable Long userId) {
        List<WorkoutHistory> histories = workoutService.getUserWorkoutHistory(userId);
        List<WorkoutHistoryResponse> response = histories.stream()
                .map(WorkoutHistoryResponse::from)
                .toList();
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "운동 세트 정보 조회",
            description = "특정 운동 기록의 세트 정보를 조회합니다. 세트 번호 순으로 정렬되어 반환됩니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = WorkoutSetResponse.class))
            ),
            @ApiResponse(responseCode = "404", description = "운동 기록을 찾을 수 없음")
    })
    @GetMapping("/workouts/{workoutId}/sets")
    public ResponseEntity<List<WorkoutSetResponse>> getWorkoutSets(
            @Parameter(description = "운동 기록 ID", required = true)
            @PathVariable("workoutId") Long workoutHistoryId) {
        List<WorkoutSet> sets = workoutService.getWorkoutSets(workoutHistoryId);
        List<WorkoutSetResponse> response = sets.stream()
                .map(WorkoutSetResponse::from)
                .toList();
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "부위별 운동 기록 조회",
            description = "사용자의 특정 부위 운동 기록을 조회합니다. (예: 등, 가슴, 하체 등의 부위별 운동 기록 조회)"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = WorkoutHistoryResponse.class))
            ),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음")
    })
    @GetMapping("/users/{userId}/workouts/body-parts/{bodyPart}")
    public ResponseEntity<List<WorkoutHistoryResponse>> getWorkoutsByBodyPart(
            @Parameter(description = "사용자 ID", required = true)
            @PathVariable Long userId,
            @Parameter(description = "운동 부위 (예: 등, 가슴, 하체)", required = true)
            @PathVariable String bodyPart) {
        List<WorkoutHistory> histories = workoutService.getWorkoutsByBodyPart(userId, bodyPart);
        List<WorkoutHistoryResponse> response = histories.stream()
                .map(WorkoutHistoryResponse::from)
                .toList();
        return ResponseEntity.ok(response);
    }
    @Operation(
            summary = "운동 세트 정보 수정",
            description = "특정 운동 세트의 무게, 반복 횟수를 수정합니다."
    )
    @PutMapping("/workouts/{workoutId}/sets/{setId}")
    public ResponseEntity<WorkoutSetResponse> updateWorkoutSet(
            @PathVariable Long workoutId,
            @PathVariable Long setId,
            @RequestBody WorkoutSetUpdateRequest request) {
        WorkoutSet updatedSet = workoutService.updateWorkoutSet(workoutId, setId, request);
        return ResponseEntity.ok(WorkoutSetResponse.from(updatedSet));
    }

    @Operation(
            summary = "운동 세트 삭제",
            description = "특정 운동의 특정 세트를 삭제합니다."
    )
    @DeleteMapping("/workouts/{workoutId}/sets/{setId}")
    public ResponseEntity<Void> deleteWorkoutSet(
            @PathVariable Long workoutId,
            @PathVariable Long setId) {
        workoutService.deleteWorkoutSet(workoutId, setId);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "운동 기록 전체 삭제",
            description = "특정 운동 기록을 완전히 삭제하고 관련 포인트를 차감합니다."
    )
    @DeleteMapping("/workouts/{workoutId}")
    public ResponseEntity<Void> deleteWorkout(
            @PathVariable Long workoutId) {
        workoutService.deleteWorkout(workoutId);
        return ResponseEntity.ok().build();
    }
}