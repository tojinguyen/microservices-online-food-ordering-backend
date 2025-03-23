package com.learning.foodorderingservice.stores.model;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import com.learning.foodorderingservice.stores.enums.StoreStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "stores")
@Getter
@Setter
public class Store {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id; // Mã định danh của cửa hàng

    @Column(name = "owner_id", nullable = false)
    private UUID ownerId; // Liên kết với User Service

    @Column(name = "name", length = 255, nullable = false)
    private String name; // Tên cửa hàng

    @Column(name = "description", columnDefinition = "TEXT")
    private String description; // Mô tả cửa hàng

    @Column(name = "address", columnDefinition = "TEXT")
    private String address; // Địa chỉ cửa hàng

    @Column(name = "phone_number", length = 20)
    private String phoneNumber; // Số điện thoại cửa hàng

    @Column(name = "open_hours", length = 255)
    private String openHours; // Thời gian mở cửa

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20)
    private StoreStatus status; // Trạng thái hoạt động của cửa hàng

    @Column(name = "created_at", nullable = false)
    private Instant createdAt; // Thời gian tạo cửa hàng

    @Column(name = "updated_at")
    private Instant updatedAt; // Thời gian cập nhật gần nhất

    @Column(name = "profile_image_url", columnDefinition = "TEXT")
    private String profileImageUrl; // Ảnh đại diện của cửa hàng

    @Column(name = "cover_image_url", columnDefinition = "TEXT")
    private String coverImageUrl; // Ảnh bìa của cửa hàng

    @Column(name = "total_products")
    private int totalProducts; // Tổng số sản phẩm của cửa hàng

    @Column(name = "total_orders")
    private int totalOrders; // Tổng số đơn hàng của cửa hàng

    @Column(name = "revenue", precision = 10, scale = 2)
    private double revenue; // Tổng doanh thu của cửa hàng

    @Column(name = "average_rating", precision = 2, scale = 1)
    private double averageRating; // Điểm đánh giá trung bình

    @OneToMany(mappedBy = "store", fetch = FetchType.LAZY)
    private List<Rating> ratings;
}
