package com.workout.workoutManager.domain.User.repository;

import com.workout.workoutManager.domain.User.entity.RefreshToken;
import com.workout.workoutManager.domain.User.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByUser(User user);
    Optional<RefreshToken> findByToken(String token);
    boolean existsByUser(User user);
    void deleteByUser(User user);

    @Query("SELECT rt FROM RefreshToken rt WHERE rt.expiryDate < :now")
    List<RefreshToken> findAllExpired(@Param("now") LocalDateTime now);
}