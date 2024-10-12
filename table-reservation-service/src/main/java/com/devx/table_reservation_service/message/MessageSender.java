package com.devx.table_reservation_service.message;

import com.devx.table_reservation_service.dto.RestaurantTableDto;
import com.devx.table_reservation_service.model.RestaurantTable;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MessageSender {

    @Autowired
    private RabbitTemplate template;

    @Autowired
    private DirectExchange direct;

    private static class TableDtoMessageBody extends RestaurantTableDto {
        private Long id;
        private Integer tableNo;
    }

    public void sendAddRestaurantTableMessage(RestaurantTable table) {
        TableDtoMessageBody tableDto = new TableDtoMessageBody();
        tableDto.setId(table.getId());
        tableDto.setTableNo(table.getTableNo());
        template.convertAndSend(direct.getName(), "addTable", tableDto);
    }
}
