package com.workout.workoutManager.domain.shop.entity;

import com.workout.workoutManager.domain.shop.enumerate.ItemTypeEnum;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
@Entity
@Table(name = "item_type")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemType extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private ItemTypeEnum type;

    @Column(name = "max_equip_count", nullable = false)
    private int maxEquipCount;

    @OneToMany(mappedBy = "type")
    private List<ShopItem> items = new ArrayList<>();

    @Builder
    public ItemType(ItemTypeEnum type, int maxEquipCount) {
        this.type = type;
        this.maxEquipCount = maxEquipCount;
    }
}