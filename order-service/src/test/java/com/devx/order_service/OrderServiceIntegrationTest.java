package com.devx.order_service;

import com.devx.order_service.dto.*;
import com.devx.order_service.enums.OrderItemStatus;
import com.devx.order_service.enums.OrderStatus;
import com.devx.order_service.model.*;
import com.devx.order_service.repository.AddOnRepository;
import com.devx.order_service.repository.MenuItemRepository;
import com.devx.order_service.repository.OrderRepository;
import com.devx.order_service.repository.RestaurantTableRepository;
import com.devx.order_service.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class OrderServiceIntegrationTest extends BaseIntegrationTestConfiguration {

    private final OrderRepository orderRepository;
    private final OrderService orderService;

    private final MenuItemRepository menuItemRepository;

    private final AddOnRepository addOnRepository;

    private final RestaurantTableRepository tableRepository;

    private OrderDto testOrderDto;

    @Autowired
    public OrderServiceIntegrationTest(OrderRepository orderRepository, OrderService orderService, MenuItemRepository menuItemRepository, AddOnRepository addOnRepository, RestaurantTableRepository tableRepository) {
        this.orderRepository = orderRepository;
        this.orderService = orderService;
        this.menuItemRepository = menuItemRepository;
        this.addOnRepository = addOnRepository;
        this.tableRepository = tableRepository;
    }

    @BeforeEach
    void setup() {
        orderRepository.deleteAll();
        tableRepository.deleteAll();
        menuItemRepository.deleteAll();
        addOnRepository.deleteAll();

        tableRepository.saveAll(List.of(
                new RestaurantTable(1L, 1),
                new RestaurantTable(2L, 2),
                new RestaurantTable(3L, 3),
                new RestaurantTable(4L, 4)
        ));

        menuItemRepository.save(
                new MenuItem(1L, "Burger", List.of(new AddOn(1L, "Extra Sauce", 1.50)))
        );

        RestaurantTableDto restaurantTableDto = new RestaurantTableDto();
        restaurantTableDto.setTableNo(1);

        MenuItemDto menuItemDto = new MenuItemDto();
        menuItemDto.setId(1L);

        OrderItemAddOnDto orderItemAddOnDto = new OrderItemAddOnDto();
        orderItemAddOnDto.setAddOnId(1L);
        orderItemAddOnDto.setQuantity(2);

        OrderItemDto orderItemDto = new OrderItemDto();
        orderItemDto.setMenuItem(menuItemDto);
        orderItemDto.setQuantity(2);
        orderItemDto.setAddOns(List.of(orderItemAddOnDto));
        orderItemDto.setAdditionalNotes("No Onions");


        testOrderDto = new OrderDto();
        testOrderDto.setTable(restaurantTableDto);
        testOrderDto.setOrderItems(List.of(orderItemDto));
    }

    @Test
    void test_AddOrder() {

        Mono<OrderDto> orderDtoMono = orderService.createOrder(testOrderDto);

        StepVerifier.create(orderDtoMono)
                .assertNext(savedOrderDto -> {
                    assertNotNull(savedOrderDto);
                    assertNotNull(savedOrderDto.getId());
                    assertEquals(1, savedOrderDto.getOrderItems().size());
                    assertEquals(1, savedOrderDto.getOrderItems().get(0).getAddOns().size());
                    assertEquals(OrderItemStatus.Pending, savedOrderDto.getOrderItems().get(0).getStatus());
                })
                .verifyComplete();
    }

    @Test
    void test_GetAllOrderItems() {
        Mono<OrderDto> orderDtoMono = orderService.createOrder(testOrderDto);
        orderDtoMono.block();

        Flux<OrderDto> orderDtoFlux = orderService.getOrders();
        StepVerifier.create(orderDtoFlux)
                .assertNext(orderDto -> {
                    assertNotNull(orderDto);
                    assertNotNull(orderDto.getId());
                    assertEquals(1, orderDto.getOrderItems().size());
                    assertEquals(1, orderDto.getOrderItems().get(0).getAddOns().size());
                    assertEquals(OrderItemStatus.Pending, orderDto.getOrderItems().get(0).getStatus());
                })
                .verifyComplete();
    }

    @Test
    void test_UpdateOrderItemStatus(){
        Mono<OrderDto> orderDtoMono = orderService.createOrder(testOrderDto);
        OrderDto savedOrderDto = orderDtoMono.block();

        OrderItemDto orderItemDto = savedOrderDto.getOrderItems().get(0);
        OrderItemStatus newStatus = OrderItemStatus.Processing;

        Mono<OrderDto> updatedOrderDtoMono = orderService.updateOrderItemStatus(savedOrderDto.getId(), orderItemDto.getId(), newStatus);
        StepVerifier.create(updatedOrderDtoMono)
                .assertNext(updatedOrderDto -> {
                    assertNotNull(updatedOrderDto);
                    assertNotNull(updatedOrderDto.getId());
                    assertEquals(1, updatedOrderDto.getOrderItems().size());
                    assertEquals(1, updatedOrderDto.getOrderItems().get(0).getAddOns().size());
                    assertEquals(newStatus, updatedOrderDto.getOrderItems().get(0).getStatus());
                    assertEquals(OrderStatus.Processing, updatedOrderDto.getStatus());
                })
                .verifyComplete();
    }

}
