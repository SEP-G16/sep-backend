package com.devx.order_service.controller;

import com.devx.order_service.dto.OrderDto;
import com.devx.order_service.dto.request.RejectOrderItemRequestBody;
import com.devx.order_service.exception.*;
import com.devx.order_service.model.Order;
import com.devx.order_service.service.OrderService;
import jakarta.transaction.Transactional;
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
            return ResponseEntity.created(null).body(orderService.createOrder(orderDto));
        } catch (NullFieldException | EmptyOrderItemListException e) {
            return ResponseEntity.badRequest().body(Mono.error(e));
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

    @PostMapping("/reject-order-item/")
    public ResponseEntity<Mono<OrderDto>> rejectOrderItem(@RequestBody RejectOrderItemRequestBody rejectOrderItemRequestBody) {
        try {
            return ResponseEntity.ok().body(orderService.rejectOrderItem(rejectOrderItemRequestBody));
        } catch (NullFieldException | OrderNotFoundException | OrderItemNotFoundException e) {
            return ResponseEntity.badRequest().body(Mono.error(e));
        }
    }

}
