package com.workout.workoutManager.service.User;

import com.workout.workoutManager.config.JwtConfig.JwtTokenProvider;
import com.workout.workoutManager.domain.User.entity.RefreshToken;
import com.workout.workoutManager.domain.User.entity.User;
import com.workout.workoutManager.domain.User.exception.RefreshTokenException;
import com.workout.workoutManager.domain.User.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public RefreshToken createRefreshToken(User user) {
        // 기존 토큰이 있다면 삭제
        refreshTokenRepository.findByUser(user)
                .ifPresent(token -> refreshTokenRepository.delete(token));

        // 새로운 리프레시 토큰 생성
        String token = jwtTokenProvider.createRefreshToken(user.getId(), user.getEmail());
        LocalDateTime expiryDate = LocalDateTime.now().plusDays(14); // 2주

        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .token(token)
                .expiryDate(expiryDate)
                .build();

        return refreshTokenRepository.save(refreshToken);
    }

    @Transactional
    public RefreshToken updateRefreshToken(User user, String newToken) {
        RefreshToken refreshToken = refreshTokenRepository.findByUser(user)
                .orElseThrow(() -> new RefreshTokenException("Refresh token not found"));

        refreshToken.updateToken(newToken, LocalDateTime.now().plusDays(14));
        return refreshToken;
    }

    public void validateRefreshToken(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new RefreshTokenException("Refresh token not found"));

        if (refreshToken.isExpired()) {
            throw new RefreshTokenException("Refresh token expired");
        }

        if (!jwtTokenProvider.validateToken(token)) {
            throw new RefreshTokenException("Invalid refresh token");
        }
    }

    @Transactional
    public void deleteByUser(User user) {
        refreshTokenRepository.deleteByUser(user);
    }
}