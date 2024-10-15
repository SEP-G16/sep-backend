package com.devx.order_service.message;

import com.devx.order_service.dto.OrderDto;
import com.devx.order_service.model.Order;
import com.devx.order_service.utils.AppUtils;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class MessageSender {

    private final RabbitTemplate template;

    private final DirectExchange direct;

    @Autowired
    public MessageSender(RabbitTemplate template, @Qualifier("wsDirect") DirectExchange direct) {
        this.template = template;
        this.direct = direct;
    }

    public void sendOrderStatusUpdateMessage(Order order) {
        OrderDto orderDto = AppUtils.OrderUtils.entityToDto(order);
        template.convertAndSend(direct.getName(), "sendOrderStatusUpdate", orderDto);
    }

    public void sendOrderAddedMessage() {
        template.convertAndSend(direct.getName(), "sendOrderAdded", "");
    }
}