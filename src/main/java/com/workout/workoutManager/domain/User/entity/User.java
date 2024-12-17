package com.workout.workoutManager.domain.User.entity;

import com.workout.workoutManager.domain.User.Role;
import lombok.*;  //import lombok modules
import jakarta.persistence.*; //import jpa modules
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.*;

@Entity
@Table(name = "user")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 45)
    private String email;

    @Column(nullable = false, unique = true, length = 45)
    private String nickname;

    @Column(nullable = false)
    private String password;

    @Column(name = "phone_number", nullable = false, unique = true, length = 11)
    private String phoneNumber;

    @Column(nullable = false)
    private LocalDate birth;

    @Column(nullable = false)
    private Boolean gender; // false: FEMALE, true: MALE

    @Column(nullable = false)
    private Short height;

    @Column(nullable = false)
    private Short weight;

    @Column(name = "profile_image")
    private String profileImage;

    @Column(nullable = false, length = 10)
    @Enumerated(EnumType.STRING)
    private Role role = Role.USER;

    @Column(name = "last_logined_at")
    private LocalDateTime lastLoginedAt;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    //workout streak
    @Column(nullable = false, columnDefinition = "integer default 0")
    private Integer streak = 0;

    //users' point
    @Column(nullable = false, columnDefinition = "integer default 0")
    private Integer point = 0;

    @Builder
    public User(String email, String nickname, String password, String phoneNumber,
                LocalDate birth, Boolean gender, Short height, Short weight) {
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.birth = birth;
        this.gender = gender;
        this.height = height;
        this.weight = weight;
    }

    //about streak
    public void incrementStreak() {
        this.streak += 1;
        addPointByStreak(this.streak);
    }

    public void resetStreak() {
        this.streak = 0;
    }

    //about getting point
    private void addPointByStreak(int currentStreak) {
        if (currentStreak >= 30) this.point += 500;
        else if (currentStreak >= 15) this.point += 300;
        else if (currentStreak >= 7) this.point += 100;
        else this.point += 50;
    }

    public void addPoint(int amount) {
        this.point += amount;
    }

    //update method
    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    public void updatePassword(String password) {
        this.password = password;
    }

    public void updatePhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void updateHeight(Short height) {
        this.height = height;
    }

    public void updateWeight(Short weight) {
        this.weight = weight;
    }

    public void updateProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public void updateLastLoginTime() {
        this.lastLoginedAt = LocalDateTime.now();
    }
}