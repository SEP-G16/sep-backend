package com.devx.order_service.service.helper;

import com.devx.order_service.model.Order;

public interface OrderServiceHelper {
    Order findAndSetMenuItems(Order order);

    Order findAndSetTable(Order order);

    Order handleOrderStatusChangeAfterOrderItemStatusChange(Order order);
}
