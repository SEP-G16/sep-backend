package com.devx.order_service.service;

import com.devx.order_service.dto.OrderDto;
import com.devx.order_service.dto.request.RejectOrderItemRequestBody;
import com.devx.order_service.exception.*;
import com.devx.order_service.model.Order;
import com.devx.order_service.repository.OrderRepository;
import com.devx.order_service.utils.AppUtils;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    public static Logger LOG = LoggerFactory.getLogger(OrderService.class);

    private final OrderServiceIntegration orderServiceIntegration;

    @Autowired
    public OrderService(OrderServiceIntegration orderServiceIntegration) {
        this.orderServiceIntegration = orderServiceIntegration;
    }

    @Transactional
    public Mono<OrderDto> createOrder(OrderDto orderDto) {
        Order order = AppUtils.convertOrderDtoToOrder(orderDto);
        return orderServiceIntegration.createOrder(order);
    }

    public Flux<OrderDto> getOrders() throws Exception {
        try {
            return orderServiceIntegration.getAllOrders();
        } catch (Exception e) {
            LOG.error("An unexpected error occurred while fetching temp reviews{}", e.getMessage());
            throw new Exception(e);
        }
    }

    public Mono<Void> cancelOrder(Long orderId) {
        return orderServiceIntegration.cancelOrder(orderId);
    }

    public Mono<OrderDto> rejectOrderItem(RejectOrderItemRequestBody rejectOrderItemRequestBody) {
        if (rejectOrderItemRequestBody.hasNullFields()) {
            throw new NullFieldException("Null fields found in request body");
        }
        Long orderId = rejectOrderItemRequestBody.getOrderId();
        Long orderItemId = rejectOrderItemRequestBody.getOrderItemId();
        return orderServiceIntegration.rejectOrderItem(orderId, orderItemId);

    }
}
