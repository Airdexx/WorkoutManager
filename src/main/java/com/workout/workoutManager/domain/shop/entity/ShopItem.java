package com.workout.workoutManager.domain.shop.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Entity
@Table(name = "shop_item")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ShopItem extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id", nullable = false)
    private ItemType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "condition_id", nullable = false)
    private ItemCondition condition;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private int price;

    @Column(name = "is_limited", nullable = false)
    private boolean limited;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @OneToMany(mappedBy = "item")
    private List<UserItem> userItems = new ArrayList<>();

    @Column(name = "image_path")
    private String imagePath;

    @Builder
    public ShopItem(ItemType type, ItemCondition condition, String name, 
                   String description, int price, boolean limited,
                   LocalDateTime startDate, LocalDateTime endDate, String imagePath) {
        this.type = type;
        this.condition = condition;
        this.name = name;
        this.description = description;
        this.price = price;
        this.limited = limited;
        this.startDate = startDate;
        this.endDate = endDate;
        this.imagePath = imagePath;
    }
}