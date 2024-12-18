package com.workout.workoutManager.domain.shop.entity;

import com.workout.workoutManager.domain.User.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "purchase_history")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PurchaseHistory extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private ShopItem item;

    @Column(nullable = false)
    private int price;

    @Column(name = "purchased_at", nullable = false)
    private LocalDateTime purchasedAt;

    @Builder
    public PurchaseHistory(User user, ShopItem item, int price) {
        this.user = user;
        this.item = item;
        this.price = price;
        this.purchasedAt = LocalDateTime.now();
    }
}