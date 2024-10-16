package com.devx.order_service.service;

import com.devx.order_service.dto.OrderDto;
import com.devx.order_service.enums.OrderItemStatus;
import com.devx.order_service.model.Order;
import com.devx.order_service.utils.AppUtils;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class OrderService {

    public static Logger LOG = LoggerFactory.getLogger(OrderService.class);

    private final OrderServiceIntegration orderServiceIntegration;

    @Autowired
    public OrderService(OrderServiceIntegration orderServiceIntegration) {
        this.orderServiceIntegration = orderServiceIntegration;
    }

    @Transactional
    public Mono<OrderDto> createOrder(OrderDto orderDto) {
        Order order = AppUtils.OrderUtils.dtoToEntity(orderDto);
        return orderServiceIntegration.createOrder(order);
    }

    public Flux<OrderDto> getOrders() {
        return orderServiceIntegration.getAllOrders();
    }

    public Mono<Void> cancelOrder(Long orderId) {
        return orderServiceIntegration.cancelOrder(orderId);
    }

    public Mono<OrderDto> updateOrderItemStatus(Long orderId, Long orderItemId, OrderItemStatus orderItemStatus) {
        return orderServiceIntegration.updateOrderItemStatus(orderId, orderItemId, orderItemStatus);
    }

    public Mono<OrderDto> completeOrder(Long orderId) {
        return orderServiceIntegration.completeOrder(orderId);
    }

    public Flux<OrderDto> getIncompleteOrders(UUID uuid) {
        return orderServiceIntegration.getIncompleteOrders(uuid);
    }
}
