package com.devx.order_service.model;

import com.devx.order_service.enums.OrderStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

//@NamedEntityGraph(
//        name = "order-entity-graph",
//        attributeNodes = {
//                @NamedAttributeNode(value = "orderItems", subgraph = "order-item-subgraph")
//        },
//        subgraphs = {
//                @NamedSubgraph(name = "order-item-subgraph",
//                        attributeNodes = {
//                                @NamedAttributeNode("addOns")
//                        }
//                ),
//        }
//)

@Entity
@Table(name = "`order`")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long tableId;
    private double totalAmount;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "order_time")
    private LocalDateTime orderTime;

    @Enumerated(EnumType.ORDINAL)
    private OrderStatus status;
}
