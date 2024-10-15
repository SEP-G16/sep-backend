package com.devx.order_service.controller;

import com.devx.order_service.dto.OrderDto;
import com.devx.order_service.dto.request.UpdateOrderItemStatusRequestBody;
import com.devx.order_service.exception.*;
import com.devx.order_service.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/order")
public class OrderController {
    private final OrderService orderService;

    public static Logger LOG = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/add")
    public ResponseEntity<Mono<OrderDto>> createOrder(@RequestBody OrderDto orderDto) {
        try {
            if(orderDto.hasNullFields())
            {
                throw new NullFieldException("Required fields cannot be null");
            }
            return ResponseEntity.created(null).body(orderService.createOrder(orderDto));
        } catch (NullFieldException | EmptyOrderItemListException e) {
            return ResponseEntity.badRequest().body(Mono.error(() -> new Exception(e.getMessage())));
        } catch (Exception e) {
            LOG.error("Error occurred while creating order"+e.getMessage());
            return ResponseEntity.internalServerError().body(Mono.error(() -> new Exception(e.getMessage())));
        }
    }

    @GetMapping("/all")
    public ResponseEntity<Flux<OrderDto>> getOrders() {
        try {
            return ResponseEntity.ok().body(orderService.getOrders());
        } catch (Exception e) {
            LOG.error("Error occurred while fetching orders"+e.getMessage());
            return ResponseEntity.internalServerError().body(Flux.error(e));
        }
    }

    @DeleteMapping("/cancel/{orderId}")
    public ResponseEntity<Mono<Void>> cancelOrder(@PathVariable Long orderId) {
        try {
            if(orderId == null)
            {
                throw new BadRequestException("Order Id cannot be null");
            }
            return ResponseEntity.ok().body(orderService.cancelOrder(orderId));
        } catch (OrderNotFoundException | BadRequestException e) {
            return ResponseEntity.badRequest().body(Mono.error(e));
        }
    }

    @PutMapping("/update-item-status/")
    public ResponseEntity<Mono<OrderDto>> updateOrderItemStatus(@RequestBody UpdateOrderItemStatusRequestBody reqBody) {
        try {
            if(reqBody.hasNullFields())
            {
                throw new NullFieldException("Order Id, Order Item Id and Order Item Status cannot be null");
            }
            return ResponseEntity.ok().body(orderService.updateOrderItemStatus(reqBody.getOrderId(), reqBody.getOrderItemId(), reqBody.getStatus()));
        } catch (NullFieldException | OrderNotFoundException | OrderItemNotFoundException e) {
            return ResponseEntity.badRequest().body(Mono.error(e));
        } catch (Exception e)
        {
            LOG.error("Error occurred while updating order item status"+e.getMessage());
            return ResponseEntity.internalServerError().body(Mono.error(e));
        }
    }

    @PutMapping("/complete-order/{orderId}")
    public ResponseEntity<Mono<OrderDto>> completeOrder(@PathVariable Long orderId) {
        try {
            if(orderId == null)
            {
                throw new BadRequestException("Order Id cannot be null");
            }
            return ResponseEntity.ok().body(orderService.completeOrder(orderId));
        } catch (OrderNotFoundException | OrderAlreadyCompletedException | OrderNotYetCompleteException e) {
            return ResponseEntity.badRequest().body(Mono.error(new Exception(e.getMessage())));
        } catch (Exception e)
        {
            LOG.error("Error occurred while completing order"+e.getMessage());
            return ResponseEntity.internalServerError().body(Mono.error(new Exception(e.getMessage())));
        }
    }

}
