package com.learning.storeservice.model;

import com.learning.storeservice.enums.StoreStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "stores")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Store {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private UUID ownerId;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String address;

    @Column(length = 20)
    private String phoneNumber;

    @Column(length = 255)
    private String openHours;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StoreStatus status = StoreStatus.ACTIVE;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}

