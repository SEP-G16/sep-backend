package com.devx.order_service.utils;

import com.devx.order_service.dto.OrderDto;
import com.devx.order_service.dto.OrderItemAddOnDto;
import com.devx.order_service.dto.OrderItemDto;
import com.devx.order_service.exception.NullFieldException;
import com.devx.order_service.model.Order;
import com.devx.order_service.model.OrderItem;
import com.devx.order_service.model.OrderItemAddOn;

public class AppUtils {
    private AppUtils() {
    }

    public static OrderDto convertOrderToOrderDto(Order order) {
        OrderDto orderDto = new OrderDto();
        orderDto.setId(order.getId());
        orderDto.setTableId(order.getTableId());
        orderDto.setTotalAmount(order.getTotalAmount());
        orderDto.setOrderItems(order.getOrderItems().stream().map(orderItem -> {
            OrderItemDto orderItemDto = new OrderItemDto();
            orderItemDto.setId(orderItem.getId());
            orderItemDto.setMenuItemId(orderItem.getMenuItemId());
            orderItemDto.setQuantity(orderItem.getQuantity());
            orderItemDto.setAdditionalNotes(orderItem.getAdditionalNotes());
            orderItemDto.setAddOns(orderItem.getAddOns().stream().map(addOn -> {
                OrderItemAddOnDto addOnDto = new OrderItemAddOnDto();
                addOnDto.setId(addOn.getId());
                addOnDto.setQuantity(addOn.getQuantity());
                return addOnDto;
            }).toList());
            return orderItemDto;
        }).toList());
        orderDto.setOrderTime(order.getOrderTime());
        orderDto.setStatus(order.getStatus());
        return orderDto;
    }

    public static Order convertOrderDtoToOrder(OrderDto orderDto) {
        if (orderDto.hasNullFields()) {
            throw new NullFieldException("OrderDto has null fields");
        }

        Order order = new Order();
        order.setId(orderDto.getId());
        order.setTableId(orderDto.getTableId());
        order.setTotalAmount(orderDto.getTotalAmount());
        order.setOrderItems(orderDto.getOrderItems().stream().map(orderItemDto -> {
            if (orderItemDto.hasNullFields()) {
                throw new NullFieldException("OrderItemDto has null fields");
            }
            OrderItem orderItem = new OrderItem();
            orderItem.setId(orderItemDto.getId());
            orderItem.setMenuItemId(orderItemDto.getMenuItemId());
            orderItem.setQuantity(orderItemDto.getQuantity());
            orderItem.setAdditionalNotes(orderItemDto.getAdditionalNotes());
            orderItem.setAddOns(orderItemDto.getAddOns().stream().map(addOnDto -> {
                if (addOnDto.hasNullFields()) {
                    throw new NullFieldException("OrderItemAddOnDto has null fields");
                }
                OrderItemAddOn addOn = new OrderItemAddOn();
                addOn.setId(addOnDto.getId());
                addOn.setQuantity(addOnDto.getQuantity());
                return addOn;
            }).toList());
            return orderItem;
        }).toList());
        order.setOrderTime(orderDto.getOrderTime());
        order.setStatus(orderDto.getStatus());
        return order;
    }
}
