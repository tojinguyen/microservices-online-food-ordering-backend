package com.learning.storeservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "store_statistics")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreStatistics {
    @Id
    @JoinColumn(name = "store_id")
    private UUID storeId;

    @OneToOne
    @MapsId
    private Store store;

    @Column(nullable = false, columnDefinition = "INT DEFAULT 0")
    private int totalProducts;

    @Column(nullable = false, columnDefinition = "INT DEFAULT 0")
    private int totalOrders;

    @Column(nullable = false, precision = 10, scale = 2, columnDefinition = "DECIMAL(10,2) DEFAULT 0.0")
    private BigDecimal revenue;
}
