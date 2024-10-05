package com.devx.order_service;

import com.devx.order_service.dto.OrderDto;
import com.devx.order_service.dto.request.RejectOrderItemRequestBody;
import com.devx.order_service.model.Order;
import com.devx.order_service.model.OrderItem;
import com.devx.order_service.repository.OrderRepository;
import com.devx.order_service.service.OrderService;
import com.devx.order_service.service.OrderServiceIntegration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

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

    @Test
    void testCreateAndRejectOrderItem() {
        // Step 1: Create order items
        OrderItem orderItem1 = new OrderItem();
        orderItem1.setQuantity(1);
        orderItem1.setAddOns(List.of()); // Initialize addOns if needed

        OrderItem orderItem2 = new OrderItem();
        orderItem2.setQuantity(2);
        orderItem2.setAddOns(List.of()); // Initialize addOns if needed

        List<OrderItem> orderItems = List.of(orderItem1, orderItem2);

        // Step 2: Create order
        Order order = new Order();
        order.setOrderItems(orderItems);

        // Step 3: Insert the order
        Mono<OrderDto> res1 = orderServiceIntegration.createOrder(order);
        OrderDto savedOrder = Objects.requireNonNull(res1.block());

        // Step 4: Assertions for order creation
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

        // Step 5: Reject the first order item
        Long orderId = savedOrder.getId();
        Long orderItemId = savedOrder.getOrderItems().get(0).getId();  // Get the ID of the first item

        RejectOrderItemRequestBody rejectOrderItemRequestBody = new RejectOrderItemRequestBody(orderId, orderItemId);
        Mono<OrderDto> rejectRes = orderServiceIntegration.rejectOrderItem(orderId, orderItemId);
        OrderDto updatedOrder = Objects.requireNonNull(rejectRes.block());

        // Step 6: Assertions for order item rejection
        assertNotNull(updatedOrder);
        assertEquals(1, updatedOrder.getOrderItems().size(), "There should be only 1 item after rejection");
        assertNotEquals(orderItemId, updatedOrder.getOrderItems().get(0).getId(), "Rejected item should not exist in the updated order");

        // Verify that the rejected order item is removed from the repository
        Optional<Order> updatedOrderFromRepoOpt = orderRepository.findById(orderId);
        assertTrue(updatedOrderFromRepoOpt.isPresent(), "Updated order should exist in the repository");

        Order updatedOrderFromRepo = updatedOrderFromRepoOpt.get();
        assertEquals(1, updatedOrderFromRepo.getOrderItems().size(), "There should be only 1 item in the order after rejection");

        // Step 7: Fetch all orders and verify that the rejected item is removed
        Mono<List<OrderDto>> allOrdersMono = orderServiceIntegration.getAllOrders().collectList();
        List<OrderDto> allOrders = Objects.requireNonNull(allOrdersMono.block());

        Optional<OrderDto> updatedOrderInList = allOrders.stream()
                .filter(orderDto -> orderDto.getId().equals(orderId))
                .findFirst();

        assertTrue(updatedOrderInList.isPresent(), "Updated order should be present in the list of all orders");
        assertEquals(1, updatedOrderInList.get().getOrderItems().size(), "Rejected item should not be present in the order");
    }

    @Test
    void testCreateAndGetAllOrders() {
        // Step 1: Create three orders with items

        // Create first order with 2 items
        OrderItem orderItem1A = new OrderItem();
        orderItem1A.setQuantity(1);
        orderItem1A.setAddOns(List.of());

        OrderItem orderItem1B = new OrderItem();
        orderItem1B.setQuantity(2);
        orderItem1B.setAddOns(List.of());

        List<OrderItem> orderItems1 = List.of(orderItem1A, orderItem1B);

        Order order1 = new Order();
        order1.setOrderItems(orderItems1);

        // Create second order with 1 item
        OrderItem orderItem2A = new OrderItem();
        orderItem2A.setQuantity(3);
        orderItem2A.setAddOns(List.of());

        List<OrderItem> orderItems2 = List.of(orderItem2A);

        Order order2 = new Order();
        order2.setOrderItems(orderItems2);

        // Create third order with 3 items
        OrderItem orderItem3A = new OrderItem();
        orderItem3A.setQuantity(1);
        orderItem3A.setAddOns(List.of());

        OrderItem orderItem3B = new OrderItem();
        orderItem3B.setQuantity(4);
        orderItem3B.setAddOns(List.of());

        OrderItem orderItem3C = new OrderItem();
        orderItem3C.setQuantity(5);
        orderItem3C.setAddOns(List.of());

        List<OrderItem> orderItems3 = List.of(orderItem3A, orderItem3B, orderItem3C);

        Order order3 = new Order();
        order3.setOrderItems(orderItems3);

        // Step 2: Save the orders using createOrder
        Mono<OrderDto> res1 = orderServiceIntegration.createOrder(order1);
        Mono<OrderDto> res2 = orderServiceIntegration.createOrder(order2);
        Mono<OrderDto> res3 = orderServiceIntegration.createOrder(order3);

        // Block to save orders and ensure completion
        OrderDto savedOrder1 = Objects.requireNonNull(res1.block());
        OrderDto savedOrder2 = Objects.requireNonNull(res2.block());
        OrderDto savedOrder3 = Objects.requireNonNull(res3.block());

        // Step 3: Assertions to ensure orders are created and saved correctly
        assertNotNull(savedOrder1);
        assertNotNull(savedOrder2);
        assertNotNull(savedOrder3);

        assertNotNull(savedOrder1.getId());
        assertNotNull(savedOrder2.getId());
        assertNotNull(savedOrder3.getId());

        assertEquals(2, savedOrder1.getOrderItems().size());
        assertEquals(1, savedOrder2.getOrderItems().size());
        assertEquals(3, savedOrder3.getOrderItems().size());

        // Step 4: Retrieve all orders using getAllOrders
        Mono<List<OrderDto>> allOrdersMono = orderServiceIntegration.getAllOrders().collectList();
        List<OrderDto> allOrders = Objects.requireNonNull(allOrdersMono.block());

        // Step 5: Assertions to verify all three orders are fetched correctly
        assertNotNull(allOrders);
        assertEquals(3, allOrders.size(), "There should be 3 orders in the repository");

        // Verify that the orders fetched from the repository match the created orders
        assertTrue(allOrders.stream().anyMatch(orderDto -> orderDto.getId().equals(savedOrder1.getId())), "Order 1 should be present in the repository");
        assertTrue(allOrders.stream().anyMatch(orderDto -> orderDto.getId().equals(savedOrder2.getId())), "Order 2 should be present in the repository");
        assertTrue(allOrders.stream().anyMatch(orderDto -> orderDto.getId().equals(savedOrder3.getId())), "Order 3 should be present in the repository");

        // Optional: Check the items for each order
        OrderDto retrievedOrder1 = allOrders.stream().filter(orderDto -> orderDto.getId().equals(savedOrder1.getId())).findFirst().orElseThrow();
        OrderDto retrievedOrder2 = allOrders.stream().filter(orderDto -> orderDto.getId().equals(savedOrder2.getId())).findFirst().orElseThrow();
        OrderDto retrievedOrder3 = allOrders.stream().filter(orderDto -> orderDto.getId().equals(savedOrder3.getId())).findFirst().orElseThrow();

        assertEquals(2, retrievedOrder1.getOrderItems().size(), "Order 1 should have 2 items");
        assertEquals(1, retrievedOrder2.getOrderItems().size(), "Order 2 should have 1 item");
        assertEquals(3, retrievedOrder3.getOrderItems().size(), "Order 3 should have 3 items");
    }



}
