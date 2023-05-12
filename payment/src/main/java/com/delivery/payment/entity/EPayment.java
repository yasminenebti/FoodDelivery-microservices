package com.delivery.payment.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EPayment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Double total;

    private String payerId;

    @CreationTimestamp
    private Instant createdAt;

    @Enumerated(EnumType.STRING)
    private ECurrency currency;

    @Enumerated(EnumType.STRING)
    private Method method;

    private String intent;
    private String description;
}
