package com.devx.order_service.service;

import com.devx.order_service.dto.OrderDto;
import com.devx.order_service.exception.OrderItemNotFoundException;
import com.devx.order_service.exception.OrderNotFoundException;
import com.devx.order_service.model.Order;
import com.devx.order_service.model.OrderItem;
import com.devx.order_service.repository.OrderRepository;
import com.devx.order_service.utils.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

import java.util.List;
import java.util.Optional;

@Component
public class OrderServiceIntegration {
    private final OrderRepository orderRepository;

    private final Scheduler jdbcScheduler;

    @Autowired
    public OrderServiceIntegration(OrderRepository orderRepository, Scheduler jdbcScheduler) {
        this.orderRepository = orderRepository;
        this.jdbcScheduler = jdbcScheduler;
    }

    private Order createOrderInternal(Order order) {
        return orderRepository.save(order);
    }

    public Mono<OrderDto> createOrder(Order order) {
        return Mono.fromCallable(() -> {
            Order savedOrder = createOrderInternal(order);
            return AppUtils.convertOrderToOrderDto(savedOrder);
        }).subscribeOn(jdbcScheduler);
    }

    private List<Order> getAllOrdersInternal() {
        return orderRepository.findAll();
    }

    public Flux<OrderDto> getAllOrders() {
        return Mono.fromCallable(this::getAllOrdersInternal)
                .flatMapMany(Flux::fromIterable)
                .map(AppUtils::convertOrderToOrderDto)
                .subscribeOn(jdbcScheduler);
    }

    private void cancelOrderInternal(Long orderId) {
        Optional<Order> existingOrderOptional = orderRepository.findById(orderId);
        if (existingOrderOptional.isPresent()) {
            orderRepository.deleteById(orderId);
        } else {
            throw new OrderNotFoundException("Order " + orderId + " not found");
        }
    }

    public Mono<Void> cancelOrder(Long orderId) {
        cancelOrderInternal(orderId);
        return Mono.empty();

    }

    private Order rejectOrderItemInternal(Long orderId, Long orderItemId) {
        Optional<Order> existingOrderOptional = orderRepository.findById(orderId);
        if (existingOrderOptional.isPresent()) {
            Order existingOrder = existingOrderOptional.get();
            boolean itemExists = existingOrder.getOrderItems().stream().anyMatch(orderItem -> orderItem.getId().equals(orderItemId));
            if(itemExists)
            {
                List<OrderItem> updatedOrderItems = existingOrder.getOrderItems().stream().filter(orderItem -> !orderItem.getId().equals(orderItemId)).toList();
                existingOrder.setOrderItems(updatedOrderItems);
                return orderRepository.save(existingOrder);
            }
            else{
                throw new OrderItemNotFoundException("Order item "+orderItemId+" was not found in order "+orderId);
            }
        } else {
            throw new OrderNotFoundException("Order " + orderId + " not found");
        }
    }

    public Mono<OrderDto> rejectOrderItem(Long orderId, Long orderItemId) {
        return Mono.fromCallable(() -> {
            Order updatedOrder = rejectOrderItemInternal(orderId, orderItemId);
            return AppUtils.convertOrderToOrderDto(updatedOrder);
        }).subscribeOn(jdbcScheduler);
    }
}
