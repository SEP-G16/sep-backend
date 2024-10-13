package com.devx.order_service.utils;

import com.devx.order_service.dto.*;
import com.devx.order_service.exception.EmptyOrderItemListException;
import com.devx.order_service.exception.NullFieldException;
import com.devx.order_service.model.*;

public class AppUtils {
    private AppUtils() {
    }

    public static class OrderItemAddOnUtils{
        public static OrderItemAddOnDto entityToDto(OrderItemAddOn addOn) {
            OrderItemAddOnDto addOnDto = new OrderItemAddOnDto();
            addOnDto.setAddOnId(addOn.getAddOnId());
            addOnDto.setId(addOn.getId());
            addOnDto.setQuantity(addOn.getQuantity());
            return addOnDto;
        }

        public static OrderItemAddOn dtoToEntity(OrderItemAddOnDto addOnDto) {
            OrderItemAddOn addOn = new OrderItemAddOn();
            addOn.setId(addOnDto.getId());
            addOn.setAddOnId(addOnDto.getAddOnId());
            addOn.setQuantity(addOnDto.getQuantity());
            return addOn;
        }
    }

    public static class OrderItemUtils{
        public static OrderItemDto entityToDto(OrderItem orderItem) {
            OrderItemDto orderItemDto = new OrderItemDto();
            orderItemDto.setId(orderItem.getId());
            orderItemDto.setMenuItem(MenuItemUtils.entityToDto(orderItem.getMenuItem()));
            orderItemDto.setQuantity(orderItem.getQuantity());
            orderItemDto.setAdditionalNotes(orderItem.getAdditionalNotes());
            orderItemDto.setAddOns(orderItem.getAddOns().stream().map(OrderItemAddOnUtils::entityToDto).toList());
            orderItemDto.setStatus(orderItem.getStatus());
            return orderItemDto;
        }

        public static OrderItem dtoToEntity(OrderItemDto orderItemDto) {
            OrderItem orderItem = new OrderItem();
            orderItem.setId(orderItemDto.getId());
            orderItem.setQuantity(orderItemDto.getQuantity());
            orderItem.setAdditionalNotes(orderItemDto.getAdditionalNotes());
            orderItem.setMenuItem(MenuItemUtils.dtoToEntity(orderItemDto.getMenuItem()));
            orderItem.setAddOns(orderItemDto.getAddOns().stream().map(OrderItemAddOnUtils::dtoToEntity).toList());
            orderItem.setStatus(orderItemDto.getStatus());
            return orderItem;
        }
    }

    public static class TableUtils{
        public static RestaurantTableDto entityToDto(RestaurantTable restaurantTable) {
            RestaurantTableDto restaurantTableDto = new RestaurantTableDto();
            restaurantTableDto.setId(restaurantTable.getId());
            restaurantTableDto.setTableNo(restaurantTable.getTableNo());
            return restaurantTableDto;
        }

        public static RestaurantTable dtoToEntity(RestaurantTableDto restaurantTableDto) {
            RestaurantTable restaurantTable = new RestaurantTable();
            restaurantTable.setId(restaurantTableDto.getId());
            restaurantTable.setTableNo(restaurantTableDto.getTableNo());
            return restaurantTable;
        }
    }

    public static class OrderUtils{
        public static OrderDto entityToDto(Order order) {
            OrderDto orderDto = new OrderDto();
            orderDto.setId(order.getId());
            orderDto.setTotalAmount(order.getTotalAmount());
            orderDto.setOrderItems(order.getOrderItems().stream().map(OrderItemUtils::entityToDto).toList());
            orderDto.setOrderTime(order.getOrderTime());
            orderDto.setStatus(order.getStatus());
            orderDto.setTable(TableUtils.entityToDto(order.getRestaurantTable()));
            return orderDto;
        }

        public static Order dtoToEntity(OrderDto orderDto) {
            Order order = new Order();
            order.setId(orderDto.getId());
            order.setTotalAmount(orderDto.getTotalAmount());
            order.setOrderItems(orderDto.getOrderItems().stream().map(OrderItemUtils::dtoToEntity).toList());
            order.setOrderTime(orderDto.getOrderTime());
            order.setStatus(orderDto.getStatus());
            order.setRestaurantTable(TableUtils.dtoToEntity(orderDto.getTable()));
            return order;
        }
    }

    public static class AddOnUtils {
        private AddOnUtils() {
        }

        public static AddOn dtoToEntity(AddOnDto addOnDto) {
            AddOn addOn = new AddOn();
            addOn.setId(addOnDto.getId());
            addOn.setName(addOnDto.getName());
            addOn.setPrice(addOnDto.getPrice());
            return addOn;
        }

        public static AddOnDto entityToDto(AddOn addOn) {
            AddOnDto addOnDto = new AddOnDto();
            addOnDto.setId(addOn.getId());
            addOnDto.setName(addOn.getName());
            addOnDto.setPrice(addOn.getPrice());
            return addOnDto;
        }
    }

    public static class MenuItemUtils {
        public static MenuItem dtoToEntity(MenuItemDto menuItemDto) {
            MenuItem menuItem = new MenuItem();
            menuItem.setId(menuItemDto.getId());
            menuItem.setName(menuItemDto.getName());
            menuItem.setAddOns(menuItemDto.getAddOns() != null && (long) menuItemDto.getAddOns().size() > 0 ? menuItemDto.getAddOns().stream().map(AddOnUtils::dtoToEntity).toList() : null);
            return menuItem;
        }

        public static MenuItemDto entityToDto(MenuItem menuItem) {
            MenuItemDto menuItemDto = new MenuItemDto();
            menuItemDto.setId(menuItem.getId());
            menuItemDto.setName(menuItem.getName());
            menuItemDto.setAddOns(menuItem.getAddOns().stream().map(AddOnUtils::entityToDto).toList());
            return menuItemDto;
        }
    }
}
