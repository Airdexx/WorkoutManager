package com.workout.workoutManager.domain.shop.entity;

import com.workout.workoutManager.domain.User.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_item")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserItem extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private ShopItem item;

    @Column(name = "is_equipped", nullable = false)
    private boolean equipped;

    @Column(name = "acquired_at", nullable = false)
    private LocalDateTime acquiredAt;

    @Builder
    public UserItem(User user, ShopItem item) {
        this.user = user;
        this.item = item;
        this.equipped = false;
        this.acquiredAt = LocalDateTime.now();
    }

    public void equip() {
        this.equipped = true;
    }

    public void unequip() {
        this.equipped = false;
    }
}