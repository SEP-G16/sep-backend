package com.devx.order_service.service;

import com.devx.order_service.dto.OrderDto;
import com.devx.order_service.enums.OrderItemStatus;
import com.devx.order_service.enums.OrderStatus;
import com.devx.order_service.exception.*;
import com.devx.order_service.message.MessageSender;
import com.devx.order_service.model.Order;
import com.devx.order_service.model.OrderItem;
import com.devx.order_service.repository.OrderRepository;
import com.devx.order_service.service.helper.OrderServiceHelper;
import com.devx.order_service.utils.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class OrderServiceIntegration {
    private final OrderRepository orderRepository;

    private final Scheduler jdbcScheduler;

    private final OrderServiceHelper orderServiceHelper;

    private final MessageSender messageSender;

    @Autowired
    public OrderServiceIntegration(OrderRepository orderRepository, @Qualifier("jdbcScheduler") Scheduler jdbcScheduler, OrderServiceHelper orderServiceHelper, MessageSender messageSender) {
        this.orderRepository = orderRepository;
        this.jdbcScheduler = jdbcScheduler;
        this.orderServiceHelper = orderServiceHelper;
        this.messageSender = messageSender;
    }

    private Order createOrderInternal(Order order) {
        order.setStatus(OrderStatus.Pending);
        order.setOrderItems(order.getOrderItems().stream().peek(orderItem -> orderItem.setStatus(OrderItemStatus.Pending)).toList());
        Order tableSettedOrder = orderServiceHelper.findAndSetMenuItems(order);
        Order menuItemsSettedOrder = orderServiceHelper.findAndSetTable(tableSettedOrder);
        Order saved =  orderRepository.save(menuItemsSettedOrder);
        messageSender.sendOrderAddedMessage();
        return saved;
    }

    public Mono<OrderDto> createOrder(Order order) {
        return Mono.fromCallable(() -> {
            Order savedOrder = createOrderInternal(order);
            return AppUtils.OrderUtils.entityToDto(savedOrder);
        }).subscribeOn(jdbcScheduler);
    }

    private List<Order> getAllOrdersInternal() {
        return orderRepository.findAll();
    }

    public Flux<OrderDto> getAllOrders() {
        return Mono.fromCallable(this::getAllOrdersInternal)
                .flatMapMany(Flux::fromIterable)
                .map(AppUtils.OrderUtils::entityToDto)
                .subscribeOn(jdbcScheduler);
    }

    private void cancelOrderInternal(Long orderId) {
        Optional<Order> existingOrderOptional = orderRepository.findById(orderId);
        if (existingOrderOptional.isPresent()) {
            Order existingOrder = existingOrderOptional.get();
            existingOrder.setStatus(OrderStatus.Cancelled);
            for(OrderItem item : existingOrder.getOrderItems()) {
                item.setStatus(OrderItemStatus.Rejected);
            }
            orderRepository.save(existingOrder);
        } else {
            throw new OrderNotFoundException("Order " + orderId + " not found");
        }
    }

    public Mono<Void> cancelOrder(Long orderId) {
        cancelOrderInternal(orderId);
        return Mono.empty();

    }

    private Order updateOrderItemStatusInternal(Long orderId, Long orderItemId, OrderItemStatus orderItemStatus)
    {
        Optional<Order> existingOrderOptional = orderRepository.findById(orderId);
        if (existingOrderOptional.isPresent()) {
            Order existingOrder = existingOrderOptional.get();

            boolean itemExists = existingOrder.getOrderItems().stream().anyMatch(orderItem -> orderItem.getId().equals(orderItemId));
            if(itemExists)
            {

                List<OrderItem> updatedOrderItems = existingOrder.getOrderItems().stream().peek(orderItem -> {
                    if(orderItem.getId().equals(orderItemId))
                    {
                        orderItem.setStatus(orderItemStatus);
                    }
                }).toList();
                existingOrder.setOrderItems(updatedOrderItems);
                Order statusUpdatedOrder = orderServiceHelper.handleOrderStatusChangeAfterOrderItemStatusChange(existingOrder);
                messageSender.sendOrderStatusUpdateMessage(statusUpdatedOrder);
                return orderRepository.save(statusUpdatedOrder);
            }
            else{
                throw new OrderItemNotFoundException("Order item "+orderItemId+" was not found in order "+orderId);
            }
        } else {
            throw new OrderNotFoundException("Order " + orderId + " not found");
        }
    }

    public Mono<OrderDto> updateOrderItemStatus(Long orderId, Long orderItemId, OrderItemStatus orderItemStatus) {
        return Mono.fromCallable(() -> {
            Order updatedOrder = updateOrderItemStatusInternal(orderId, orderItemId, orderItemStatus);
            return AppUtils.OrderUtils.entityToDto(updatedOrder);
        }).subscribeOn(jdbcScheduler);
    }

    private Order completeOrderInternal(Long orderId) {
        Optional<Order> existingOrderOptional = orderRepository.findById(orderId);
        if (existingOrderOptional.isPresent()) {
            Order existingOrder = existingOrderOptional.get();
            if(existingOrder.getStatus().equals(OrderStatus.Pending_Payment))
            {
                existingOrder.setStatus(OrderStatus.Complete);
                Order saved = orderRepository.save(existingOrder);
                messageSender.sendOrderStatusUpdateMessage(saved);
                return saved;
            }
            else if(existingOrder.getStatus().equals(OrderStatus.Complete))
            {
                throw new OrderAlreadyCompletedException("Order "+orderId+" is already completed");
            }
            else if(existingOrder.getStatus().equals(OrderStatus.Cancelled))
            {
                throw new OrderAlreadyCancelledException("Order "+orderId+" is already cancelled");
            }
            else
            {
                throw new OrderNotYetCompleteException("Order "+orderId+" is not yet complete");
            }
        } else {
            throw new OrderNotFoundException("Order " + orderId + " not found");
        }
    }

    public Mono<OrderDto> completeOrder(Long orderId) {
        return Mono.fromCallable(() -> {
            Order completedOrder = completeOrderInternal(orderId);
            return AppUtils.OrderUtils.entityToDto(completedOrder);
        }).subscribeOn(jdbcScheduler);
    }

    private List<Order> getIncompleteOrdersInternal(UUID uuid) {
        return orderRepository.findBySessionId(uuid);
    }

    public Flux<OrderDto> getIncompleteOrders(UUID uuid) {
        return Mono.fromCallable(() -> getIncompleteOrdersInternal(uuid))
                .flatMapMany(Flux::fromIterable)
                .map(AppUtils.OrderUtils::entityToDto)
                .subscribeOn(jdbcScheduler);
    }
}
