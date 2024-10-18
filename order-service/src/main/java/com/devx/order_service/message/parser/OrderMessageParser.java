package com.devx.order_service.message.parser;

import com.devx.order_service.enums.MenuItemStatus;
import com.devx.order_service.enums.OrderItemStatus;
import com.devx.order_service.message.MessageSender;
import com.devx.order_service.model.Order;
import com.devx.order_service.repository.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderMessageParser {
    private final OrderRepository orderRepository;
    private final MessageSender messageSender;

    @Autowired
    public OrderMessageParser(OrderRepository orderRepository, MessageSender messageSender) {
        this.orderRepository = orderRepository;
        this.messageSender = messageSender;
    }

    @Transactional
    public void updateOrdersByMenuItemStatus(Long menuItemId, MenuItemStatus status) {
        List<Order> affectedOrders = orderRepository.findOrdersByMenuItemIdAndStatus(menuItemId, OrderItemStatus.Pending);
        for (Order order : affectedOrders) {
            order.getOrderItems().stream()
                    .filter(orderItem -> orderItem.getMenuItem().getId().equals(menuItemId))
                    .forEach(orderItem -> {
                        if (status == MenuItemStatus.OutOfStock) {
                            orderItem.setStatus(OrderItemStatus.Rejected);
                        } else {
                            orderItem.setStatus(OrderItemStatus.Pending);
                        }
                    });
            messageSender.sendOrderStatusUpdateMessage(order);
        }
        orderRepository.saveAll(affectedOrders);
    }
}
