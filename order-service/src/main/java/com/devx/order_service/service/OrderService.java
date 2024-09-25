package com.devx.order_service.service;

import com.devx.order_service.model.Order;
import com.devx.order_service.repository.OrderRepository;
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

    private final OrderRepository orderRepository;

    private final Scheduler jdbcScheduler;

    @Autowired
    public OrderService(OrderRepository orderRepository, @Qualifier("jdbcScheduler") Scheduler jdbcScheduler)
    {
        this.orderRepository = orderRepository;
        this.jdbcScheduler = jdbcScheduler;
    }

    @Transactional
    public ResponseEntity<Mono<Order>> createOrder(Order order) {
        try {
            Mono<Order> saved = Mono.fromCallable(() -> orderRepository.save(order)).subscribeOn(jdbcScheduler);
            return new ResponseEntity<>(saved, HttpStatus.CREATED);
        } catch (Exception e) {
            LOG.error("An unexpected error occurred while saving review");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Flux<Order>> getOrders() {
        try {
            Flux<Order> tempReviews = Mono.fromCallable(orderRepository::findAll).flatMapMany(Flux::fromIterable).subscribeOn(jdbcScheduler);
            return new ResponseEntity<>(tempReviews, HttpStatus.OK);
        } catch (Exception e) {
            LOG.error("An unexpected error occurred while fetching temp reviews");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Mono<Void>> cancelOrder(Long orderId) {
        try {
            orderRepository.deleteById(orderId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            LOG.error("An unexpected error occurred while deleting review");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Mono<Order>> rejectOrderItem(Long orderId, Long orderItemId) {
        try {
            Optional<Order> order = orderRepository.findById(orderId);
            if (order.isPresent()) {
                Order orderObj = order.get();
                orderObj.getOrderItems().removeIf(orderItem -> orderItem.getId().equals(orderItemId));
                Mono<Order> saved = Mono.fromCallable(() -> orderRepository.save(orderObj)).subscribeOn(jdbcScheduler);
                return new ResponseEntity<>(saved, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            LOG.error("An unexpected error occurred while saving review");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
