package com.devx.order_service.service.helper;

import com.devx.order_service.enums.OrderItemStatus;
import com.devx.order_service.enums.OrderStatus;
import com.devx.order_service.exception.BadRequestException;
import com.devx.order_service.exception.NullFieldException;
import com.devx.order_service.exception.TableNotFoundException;
import com.devx.order_service.model.Order;
import com.devx.order_service.model.OrderItem;
import com.devx.order_service.model.RestaurantTable;
import com.devx.order_service.repository.MenuItemRepository;
import com.devx.order_service.repository.RestaurantTableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

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
        if(order.getRestaurantTable() == null)
        {
            throw new NullFieldException("Table cannot be null");
        }
        if(order.getRestaurantTable().getId() == null && order.getRestaurantTable().getTableNo() == null)
        {
            throw new NullFieldException("Table ID and Table Number cannot be null");
        }

        RestaurantTable table;
        if(order.getRestaurantTable().getId() != null)
        {
            table = tableRepository.findById(order.getRestaurantTable().getId()).orElseThrow(() -> new TableNotFoundException("Table not found"));
        }
        else
        {
            table = tableRepository.findByTableNo(order.getRestaurantTable().getTableNo()).orElseThrow(() -> new TableNotFoundException("Table not found"));
        }
        order.setRestaurantTable(table);
        return order;
    }

    @Override
    public Order handleOrderStatusChangeAfterOrderItemStatusChange(Order order)
    {
        List<OrderItem> orderItems = order.getOrderItems();
        assert orderItems != null;
        boolean noPendingAndNoProcessingOrderItemsExists = orderItems.stream().noneMatch(orderItem -> orderItem.getStatus().equals(OrderItemStatus.Pending) || orderItem.getStatus().equals(OrderItemStatus.Processing));
        boolean anyOrderItemIsProcessing = orderItems.stream().anyMatch(orderItem -> orderItem.getStatus().equals(OrderItemStatus.Processing));
        if(noPendingAndNoProcessingOrderItemsExists)
        {
            boolean allOrderItemsAreRejected = orderItems.stream().allMatch(orderItem -> orderItem.getStatus().equals(OrderItemStatus.Rejected));
            if(allOrderItemsAreRejected)
            {
                order.setStatus(OrderStatus.Cancelled);
            }
            else
            {
                order.setStatus(OrderStatus.Pending_Payment);
            }
        }
        else if(anyOrderItemIsProcessing)
        {
            order.setStatus(OrderStatus.Processing);
        }
        else
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
