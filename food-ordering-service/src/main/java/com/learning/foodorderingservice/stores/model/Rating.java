package com.learning.foodorderingservice.stores.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "ratings")
@Getter
@Setter
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id; // Mã định danh của đánh giá

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store; // Liên kết với cửa hàng

    @Column(name = "user_id", nullable = false)
    private UUID userId; // Liên kết với người dùng đánh giá

    @Column(name = "rating", nullable = false)
    private double rating; // Điểm đánh giá từ 1 đến 5

    @Column(name = "comment", columnDefinition = "TEXT")
    private String comment; // Bình luận (nếu có)

    @Column(name = "created_at", nullable = false)
    private Instant createdAt; // Thời gian đánh giá
}
