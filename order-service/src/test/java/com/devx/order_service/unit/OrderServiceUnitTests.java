package com.devx.order_service.unit;

import com.devx.order_service.dto.OrderDto;
import com.devx.order_service.dto.OrderItemDto;
import com.devx.order_service.dto.request.RejectOrderItemRequestBody;
import com.devx.order_service.enums.OrderItemStatus;
import com.devx.order_service.enums.OrderStatus;
import com.devx.order_service.exception.OrderItemNotFoundException;
import com.devx.order_service.exception.OrderNotFoundException;
import com.devx.order_service.model.Order;
import com.devx.order_service.service.OrderService;
import com.devx.order_service.service.OrderServiceIntegration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
@ContextConfiguration
@ActiveProfiles(value = "test")
public class OrderServiceUnitTests {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderServiceIntegration orderServiceIntegration;

    private OrderDto orderDto;
    private List<OrderDto> orderList;

    @BeforeEach
    public void setup() {

        orderDto = new OrderDto();
        orderDto.setId(1L);
        orderDto.setTableId(1L);
        orderDto.setOrderItems(List.of(new OrderItemDto(1L, 1L, new ArrayList<>(), "", 1, OrderItemStatus.Pending)));
        orderDto.setOrderTime(LocalDateTime.now());
        orderDto.setTotalAmount(100.0);
        orderDto.setStatus(OrderStatus.Pending);

        orderList = new ArrayList<>();
        orderList.add(orderDto);

        when(orderServiceIntegration.createOrder(any(Order.class))).thenReturn(Mono.just(orderDto));
        when(orderServiceIntegration.getAllOrders()).thenReturn(Mono.just(orderList).flatMapMany(Flux::fromIterable));
        when(orderServiceIntegration.cancelOrder(1L)).thenReturn(Mono.empty());
        when(orderServiceIntegration.cancelOrder(2L)).thenThrow(OrderNotFoundException.class);
        when(orderServiceIntegration.rejectOrderItem(1L, 1L)).thenReturn(Mono.just(orderDto));
        when(orderServiceIntegration.rejectOrderItem(2L, 1L)).thenThrow(OrderNotFoundException.class);
        when(orderServiceIntegration.rejectOrderItem(1L, 2L)).thenThrow(OrderItemNotFoundException.class);
    }

    @Test
    void createOrderTes() {
        Mono<OrderDto> savedOrderMono = orderService.createOrder(orderDto);
        StepVerifier.create(savedOrderMono).consumeNextWith(savedOrder -> {
            Assertions.assertEquals(orderDto.getId(), savedOrder.getId(), "Order ID should be the same");
        }).verifyComplete();
    }

    @Test
    void getAllOrdersTest() throws Exception {
        Flux<OrderDto> allOrders = orderService.getOrders();
        StepVerifier.create(allOrders).expectNextCount(1).verifyComplete();
    }

    @Test
    void cancelOrderTest() {
        Mono<Void> cancelOrderMono = orderService.cancelOrder(1L);
        StepVerifier.create(cancelOrderMono).verifyComplete();
    }

    @Test
    void cancelOrderOrderNotFoundExceptionTest() {
        Assertions.assertThrows(OrderNotFoundException.class, () -> {
            orderService.cancelOrder(2L);
        });
    }

    @Test
    void rejectOrderItemTest() {
        Mono<OrderDto> rejectedOrderItemMono = orderService.rejectOrderItem(new RejectOrderItemRequestBody(1L, 1L));
        StepVerifier.create(rejectedOrderItemMono).consumeNextWith(rejectedOrderItem -> {
            Assertions.assertEquals(orderDto.getId(), rejectedOrderItem.getId(), "Order ID should be the same");
        }).verifyComplete();
    }

    @Test
    void rejectOrderItemOrderNotFoundExceptionTest() {
        Assertions.assertThrows(OrderNotFoundException.class, () -> {
            orderService.rejectOrderItem(new RejectOrderItemRequestBody(2L, 1L));
        });
    }

    @Test
    void rejectOrderItemOrderItemNotFoundExceptionTest() {
        Assertions.assertThrows(OrderItemNotFoundException.class, () -> {
            orderService.rejectOrderItem(new RejectOrderItemRequestBody(1L, 2L));
        });
    }

}
