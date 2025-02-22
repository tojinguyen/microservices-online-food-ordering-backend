package com.learning.storeservice.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "store_images")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreImage {
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String imageUrl;

    @CreationTimestamp
    private LocalDateTime createdAt;
}