package com.devx.order_service.repository;

import com.devx.order_service.enums.OrderItemStatus;
import com.devx.order_service.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findBySessionId(UUID uuid);

    @Query("SELECT o FROM Order o JOIN o.orderItems oi WHERE oi.status = :status AND oi.menuItem.id = :menuItemId")
    List<Order> findOrdersByMenuItemIdAndStatus(@Param("menuItemId") Long menuItemId, @Param("status") OrderItemStatus status);

}

