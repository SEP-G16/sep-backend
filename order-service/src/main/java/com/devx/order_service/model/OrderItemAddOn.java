package com.devx.order_service.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "order_item_add_on")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class OrderItemAddOn {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long addOnId;
    private int quantity;
}
