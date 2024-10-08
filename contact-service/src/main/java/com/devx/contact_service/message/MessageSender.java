package com.devx.contact_service.message;

import com.devx.contact_service.model.EmailDetails;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MessageSender {

    @Autowired
    private RabbitTemplate template;

    @Autowired
    private DirectExchange direct;

    public void sendTicketResponse(EmailDetails details) {
        template.convertAndSend(direct.getName(), "sendTicketResponse", details);
    }
}
