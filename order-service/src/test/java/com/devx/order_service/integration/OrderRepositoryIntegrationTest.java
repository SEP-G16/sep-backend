package com.devx.order_service.integration;

import com.devx.order_service.BaseIntegrationTestConfiguration;
import com.devx.order_service.enums.OrderItemStatus;
import com.devx.order_service.model.MenuItem;
import com.devx.order_service.model.Order;
import com.devx.order_service.model.OrderItem;
import com.devx.order_service.repository.MenuItemRepository;
import com.devx.order_service.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class OrderRepositoryIntegrationTest extends BaseIntegrationTestConfiguration {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private MenuItemRepository menuItemRepository;

    private MenuItem testMenuItem;
    private Order testOrder;

    @BeforeEach
    public void setUp() {
        // Create a MenuItem
        testMenuItem = new MenuItem();
        testMenuItem.setId(1L);
        testMenuItem.setName("Test Menu Item");
        // Save MenuItem to the database (assuming you have a MenuItemRepository)
        menuItemRepository.save(testMenuItem);

        // Create an OrderItem
        OrderItem orderItem = new OrderItem();
        orderItem.setMenuItem(testMenuItem);
        orderItem.setQuantity(1);
        orderItem.setStatus(OrderItemStatus.Pending);

        // Create an Order
        testOrder = new Order();
        testOrder.setSessionId(UUID.randomUUID());
        List<OrderItem> orderItems = new ArrayList<>();
        orderItems.add(orderItem);
        testOrder.setOrderItems(orderItems);

        // Save Order to the database
        orderRepository.save(testOrder);
    }

    @Test
    public void testFindOrdersByMenuItemId() {
        List<Order> orders = orderRepository.findOrdersByMenuItemIdAndStatus(testMenuItem.getId(), OrderItemStatus.Pending);
        assertThat(orders).isNotEmpty();
        assertThat(orders.get(0).getOrderItems().get(0).getMenuItem().getId()).isEqualTo(testMenuItem.getId());
    }
}