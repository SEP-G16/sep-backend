package com.devx.order_service.model;

import com.devx.order_service.enums.OrderStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

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

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems;

    @CreationTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "order_time")
    private LocalDateTime orderTime;

    @Enumerated(EnumType.ORDINAL)
    private OrderStatus status;

    @UpdateTimestamp
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    private UUID sessionId;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private RestaurantTable restaurantTable;
}
