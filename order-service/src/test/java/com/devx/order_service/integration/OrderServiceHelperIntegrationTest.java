package com.devx.order_service.integration;


import com.devx.order_service.BaseIntegrationTestConfiguration;
import com.devx.order_service.enums.OrderItemStatus;
import com.devx.order_service.enums.OrderStatus;
import com.devx.order_service.model.Order;
import com.devx.order_service.model.OrderItem;
import com.devx.order_service.service.helper.OrderServiceHelper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;


@SpringBootTest
@ExtendWith(SpringExtension.class)
@ContextConfiguration
@ActiveProfiles(value = "test")
public class OrderServiceHelperIntegrationTest{

    private final OrderServiceHelper orderServiceHelper;

    @Autowired
    public OrderServiceHelperIntegrationTest(OrderServiceHelper orderServiceHelper)
    {
        this.orderServiceHelper = orderServiceHelper;
    }

@Test
void testReturnsCorrectOrderStatus() {
    assertOrderStatus(List.of(OrderItemStatus.Pending, OrderItemStatus.Pending), OrderStatus.Pending);
    assertOrderStatus(List.of(OrderItemStatus.Pending, OrderItemStatus.Pending, OrderItemStatus.Processing), OrderStatus.Processing);
    assertOrderStatus(List.of(OrderItemStatus.Rejected), OrderStatus.Cancelled);
    assertOrderStatus(List.of(OrderItemStatus.Pending, OrderItemStatus.Rejected), OrderStatus.Pending);
    assertOrderStatus(List.of(OrderItemStatus.Complete), OrderStatus.Pending_Payment);
    assertOrderStatus(List.of(OrderItemStatus.Pending, OrderItemStatus.Complete), OrderStatus.Pending);
    assertOrderStatus(List.of(OrderItemStatus.Processing, OrderItemStatus.Complete), OrderStatus.Processing);
    assertOrderStatus(List.of(OrderItemStatus.Rejected, OrderItemStatus.Complete), OrderStatus.Pending_Payment);
}

private void assertOrderStatus(List<OrderItemStatus> itemStatuses, OrderStatus expectedStatus) {
    List<OrderItem> orderItems = itemStatuses.stream()
            .map(status -> {
                OrderItem item = new OrderItem();
                item.setStatus(status);
                return item;
            })
            .toList();

    Order order = new Order();
    order.setStatus(OrderStatus.Pending);
    order.setOrderItems(orderItems);

    Order updatedOrder = orderServiceHelper.handleOrderStatusChangeAfterOrderItemStatusChange(order);
    assert updatedOrder.getStatus().equals(expectedStatus);
}
}
