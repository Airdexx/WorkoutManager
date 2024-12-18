package com.workout.workoutManager.dto.User.response;

import com.workout.workoutManager.domain.User.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResponse {
    private Long id;
    private String email;
    private String role;
    private Integer streak;
    private Integer point;
    private String accessToken;
    private String refreshToken;
    private String tokenType;

    public static LoginResponse of(User user, String accessToken, String refreshToken) {
        return LoginResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .role(user.getRole().name())
                .streak(user.getStreak())
                .point(user.getPoint())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .build();
    }
}