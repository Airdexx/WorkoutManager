package com.workout.workoutManager.controller;

import com.workout.workoutManager.config.JwtConfig.JwtTokenProvider;
import com.workout.workoutManager.domain.User.entity.User;
import com.workout.workoutManager.domain.User.exception.UserNotFoundException;
import com.workout.workoutManager.dto.User.request.LoginRequest;
import com.workout.workoutManager.dto.User.response.LoginResponse;
import com.workout.workoutManager.service.User.RefreshTokenService;
import com.workout.workoutManager.service.User.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Auth API", description = "로그인")
public class AuthController {
    
    private final UserService userService;
    private final RefreshTokenService refreshTokenService;
    private final JwtTokenProvider jwtTokenProvider;
    private final BCryptPasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        // 1. 이메일로 사용자 찾기
        User user = userService.getUserByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException("Invalid email or password"));

        // 2. 비밀번호 검증
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new UserNotFoundException("Invalid email or password");
        }

        // 3. Access Token 생성
        String accessToken = jwtTokenProvider.createAccessToken(user.getId(), user.getEmail());

        // 4. Refresh Token 생성 및 저장
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getId(), user.getEmail());
        refreshTokenService.createRefreshToken(user);

        // 5. 쿠키 생성
        ResponseCookie accessTokenCookie = ResponseCookie.from("accessToken", accessToken)
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .path("/")
                .maxAge(3600) // 1시간
                .build();

        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .path("/api/auth/refresh")
                .maxAge(1209600) // 2주
                .build();

        // 6. 마지막 로그인 시간 업데이트
        userService.updateLastLoginTime(user.getId());

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, accessTokenCookie.toString())
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                .body(LoginResponse.of(user, accessToken, refreshToken));
    }
}