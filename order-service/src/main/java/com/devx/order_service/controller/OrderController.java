package com.devx.order_service.controller;

import com.devx.order_service.model.Order;
import com.devx.order_service.service.OrderService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/order")
public class OrderController {
    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService)
    {
        this.orderService = orderService;
    }

    @PostMapping("/create")
    public ResponseEntity<Mono<Order>> createOrder(@RequestBody Order order)
    {
        return orderService.createOrder(order);
    }

    @GetMapping("/all")
    public ResponseEntity<Flux<Order>> getOrders(){
        return orderService.getOrders();
    }

    //TODO:update order status
}
