package com.devx.order_service.repository;

import com.devx.order_service.enums.OrderStatus;
import com.devx.order_service.model.Order;
import com.devx.order_service.model.OrderItem;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findBySessionId(UUID uuid);
}

