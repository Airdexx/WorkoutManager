package com.workout.workoutManager.controller;

import com.workout.workoutManager.domain.User.entity.User;
import com.workout.workoutManager.dto.User.request.UserCreateRequest;
import com.workout.workoutManager.dto.User.request.UserUpdateRequest;
import com.workout.workoutManager.service.User.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "User API", description = "사용자 관리 API")
public class UserController {
    private final UserService userService;

    // 회원가입
    @Operation(
            summary = "회원가입",
            description = "Bcrypt를 이용한 비밀번호 암호화, 이메일 중복체크, 닉네임 중복체크, 폰번호 중복체크를 곁들인 회원가입을 진행한다"
    )
    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody UserCreateRequest request) {
        User createdUser = userService.createUser(request);
        return ResponseEntity.ok(createdUser);
    }

    // 단일 회원 조회
    @Operation(
            summary = "특정 회원 조회",
            description = "마이페이지에 사용할 특정 회원 정보를 조회한다."
    )
    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    // 전체 활성 회원 조회
    @Operation(
            summary = "탈퇴하지 않은 회원조회",
            description = "softDelete되지 않은 회원들의 정보만 로드한다."
    )
    @GetMapping
    public ResponseEntity<List<User>> getAllActiveUsers() {
        List<User> users = userService.getAllActiveUsers();
        return ResponseEntity.ok(users);
    }

    // 회원 정보 수정
    @Operation(
            summary = "회원정보 수정",
            description = "PutMapping으로 일부 정보들만 수정한다"
    )
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserUpdateRequest request) {
        User updatedUser = userService.updateUser(id, request);
        return ResponseEntity.ok(updatedUser);
    }

    // 회원 탈퇴
    @Operation(
            summary = "회원탈퇴(softDelete)",
            description = "deleted_at에 시간을 찍음으로서 softDelete 구현"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }
}