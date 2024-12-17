package com.workout.workoutManager.domain.User.repository;

import com.workout.workoutManager.domain.User.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Find user by email (gonna use at login)
    Optional<User> findByEmail(String email);

    // Find user by nickname
    Optional<User> findByNickname(String nickname);

    // Find user by phone number
    Optional<User> findByPhoneNumber(String phoneNumber);

    // Check if email exists(useful at duplication check)
    boolean existsByEmail(String email);

    // Check if nickname exists
    boolean existsByNickname(String nickname);

    // Check if phone number exists
    boolean existsByPhoneNumber(String phoneNumber);

    // Soft delete by setting deletedAt
    @Modifying
    @Query("UPDATE User u SET u.deletedAt = CURRENT_TIMESTAMP WHERE u.id = :id")
    void softDelete(@Param("id") Long id);

    // Find unDeleted users
    @Query("SELECT u FROM User u WHERE u.deletedAt IS NULL")
    List<User> findAllActive();

    // Update last login time
    @Modifying
    @Query("UPDATE User u SET u.lastLoginedAt = CURRENT_TIMESTAMP WHERE u.id = :id")
    void updateLastLoginTime(@Param("id") Long id);

    //modify streak
    @Modifying
    @Query("UPDATE User u SET u.streak = :streak WHERE u.id = :userId")
    void updateStreak(@Param("userId") Long userId, @Param("streak") int streak);

    // modify points
    @Modifying
    @Query("UPDATE User u SET u.point = u.point + :point WHERE u.id = :userId")
    void addPoints(@Param("userId") Long userId, @Param("point") int point);
}