package com.devx.order_service.service.helper;

import com.devx.order_service.enums.OrderItemStatus;
import com.devx.order_service.enums.OrderStatus;
import com.devx.order_service.exception.BadRequestException;
import com.devx.order_service.exception.NullFieldException;
import com.devx.order_service.exception.TableNotFoundException;
import com.devx.order_service.model.Order;
import com.devx.order_service.model.RestaurantTable;
import com.devx.order_service.repository.MenuItemRepository;
import com.devx.order_service.repository.RestaurantTableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderServiceHelperImpl implements OrderServiceHelper{

    private final RestaurantTableRepository tableRepository;

    private final MenuItemRepository menuItemRepository;

    @Autowired
    public OrderServiceHelperImpl(RestaurantTableRepository tableRepository, MenuItemRepository menuItemRepository) {
        this.tableRepository = tableRepository;
        this.menuItemRepository = menuItemRepository;
    }

    @Override
    public Order findAndSetTable(Order order) {
        RestaurantTable table = tableRepository.findById(order.getRestaurantTable().getId()).orElseThrow(() -> new TableNotFoundException("Table not found"));
        order.setRestaurantTable(table);
        return order;
    }

    @Override
    public Order handleOrderStatusChangeAfterOrderItemStatusChange(Order order)
    {
        boolean allItemsCompleted = order.getOrderItems().stream().allMatch(orderItem -> orderItem.getStatus().equals(OrderItemStatus.Complete));
        if(allItemsCompleted)
        {
            order.setStatus(OrderStatus.Complete);
        }

        boolean allItemsRejected = order.getOrderItems().stream().allMatch(orderItem -> orderItem.getStatus().equals(OrderItemStatus.Rejected));
        if(allItemsRejected)
        {
            order.setStatus(OrderStatus.Cancelled);
        }

        boolean allItemsPending = order.getOrderItems().stream().allMatch(orderItem -> orderItem.getStatus().equals(OrderItemStatus.Pending));
        if(allItemsPending)
        {
            order.setStatus(OrderStatus.Pending);
        }

        boolean atLeastOneIsProcessing = order.getOrderItems().stream().anyMatch(orderItem -> orderItem.getStatus().equals(OrderItemStatus.Processing));
        if(atLeastOneIsProcessing)
        {
            order.setStatus(OrderStatus.Processing);
        }

        boolean nonIsProcessingOrComplete = order.getOrderItems().stream().noneMatch(orderItem -> orderItem.getStatus().equals(OrderItemStatus.Processing) || orderItem.getStatus().equals(OrderItemStatus.Complete));
        if(nonIsProcessingOrComplete)
        {
            order.setStatus(OrderStatus.Pending);
        }
        return order;
    }


    @Override
    public Order findAndSetMenuItems(Order order){
        order.getOrderItems().forEach(orderItem -> {
            orderItem.setMenuItem(menuItemRepository.findById(orderItem.getMenuItem().getId()).orElseThrow(() -> new BadRequestException("MenuItem not found")));
        });
        return order;
    }
}
