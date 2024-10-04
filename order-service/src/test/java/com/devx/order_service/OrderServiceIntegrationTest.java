package com.devx.order_service;

import com.devx.order_service.dto.OrderDto;
import com.devx.order_service.model.Order;
import com.devx.order_service.model.OrderItem;
import com.devx.order_service.repository.OrderRepository;
import com.devx.order_service.service.OrderServiceIntegration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class OrderServiceIntegrationTest extends BaseIntegrationTestConfiguration {

    private final OrderRepository orderRepository;
    private final OrderServiceIntegration orderServiceIntegration;

    @Autowired
    public OrderServiceIntegrationTest(OrderRepository orderRepository, OrderServiceIntegration orderServiceIntegration) {
        this.orderRepository = orderRepository;
        this.orderServiceIntegration = orderServiceIntegration;
    }

    @Test
    void testCreateAndCancelOrder() {
        // Create order items
        OrderItem orderItem1 = new OrderItem();
        orderItem1.setQuantity(1);
        orderItem1.setAddOns(List.of()); // Initialize addOns if needed

        OrderItem orderItem2 = new OrderItem();
        orderItem2.setQuantity(2);
        orderItem2.setAddOns(List.of()); // Initialize addOns if needed

        List<OrderItem> orderItems = List.of(orderItem1, orderItem2);

        // Create order
        Order order = new Order();
        order.setOrderItems(orderItems);

        // Insert the order
        Mono<OrderDto> res1 = orderServiceIntegration.createOrder(order);
        OrderDto savedOrder = Objects.requireNonNull(res1.block());

        // Assertions for order creation
        assertNotNull(savedOrder);
        assertNotNull(savedOrder.getId());
        assertEquals(2, savedOrder.getOrderItems().size());
        assertNotNull(savedOrder.getOrderItems().get(0).getId());
        assertNotNull(savedOrder.getOrderItems().get(1).getId());

        // Verify that the order is saved in the repository
        Optional<Order> savedOrderFromRepoOpt = orderRepository.findById(savedOrder.getId());
        assertTrue(savedOrderFromRepoOpt.isPresent(), "Order should exist in the repository");

        Order savedOrderFromRepo = savedOrderFromRepoOpt.get();
        assertEquals(savedOrder.getId(), savedOrderFromRepo.getId(), "Order ID should match");

        // Cancel the order (assuming cancelOrder method exists)
        Mono<Void> cancelRes = orderServiceIntegration.cancelOrder(savedOrder.getId());
        cancelRes.block();  // Block until cancellation completes

        // Ensure the order is removed from the repository after cancellation
        Optional<Order> cancelledOrderFromRepoOpt = orderRepository.findById(savedOrder.getId());
        assertFalse(cancelledOrderFromRepoOpt.isPresent(), "Order should be removed from the repository after cancellation");

        // Fetch all orders and verify the canceled order is not present
        Mono<List<OrderDto>> allOrdersMono = orderServiceIntegration.getAllOrders().collectList();
        List<OrderDto> allOrders = Objects.requireNonNull(allOrdersMono.block());

        // Check that the canceled order does not exist in the list of orders
        Optional<OrderDto> cancelledOrder = allOrders.stream()
                .filter(orderDto -> orderDto.getId().equals(savedOrder.getId()))
                .findFirst();

        // Assertions for order removal
        assertFalse(cancelledOrder.isPresent(), "Canceled order should not be present in the list of all orders");
    }


}
