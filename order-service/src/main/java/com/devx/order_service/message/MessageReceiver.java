package com.devx.order_service.message;

import com.devx.order_service.dto.MenuItemDto;
import com.devx.order_service.dto.RestaurantTableDto;
import com.devx.order_service.dto.message.UpdateMenuItemStatusMessageBody;
import com.devx.order_service.message.parser.MenuItemMessageParser;
import com.devx.order_service.message.parser.OrderMessageParser;
import com.devx.order_service.message.parser.RestaurantTableMessageParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Component;

@Component
public class MessageReceiver {

    public static Logger LOG = LoggerFactory.getLogger(MessageReceiver.class);

    @Autowired
    private MenuItemMessageParser menuItemMessageParser;

    @Autowired
    private RestaurantTableMessageParser restaurantTableMessageParser;

    @Autowired
    private OrderMessageParser orderMessageParser;

    @RabbitListener(queues = "#{addMenuItemQueue.name}")
    public void addMenuItemMessageHandler(MenuItemDto menuItemDto) throws MessagingException {
        try {
            menuItemMessageParser.addMenuItem(menuItemDto);
        } catch (Exception e) {
            LOG.error("Error occurred while adding menu item: ", e);
        }
    }

    @RabbitListener(queues = "#{addTableQueue.name}")
    public void addTableMessageHandler(RestaurantTableDto restaurantTableDto) throws MessagingException {
        try{
            restaurantTableMessageParser.addTable(restaurantTableDto);
        } catch (Exception e) {
            LOG.error("Error occurred while adding table: ", e);
        }
    }

    @RabbitListener(queues = "#{updateMenuItemStatusQueue.name}")
    public void updateMenuItemStatusMessageHandler(UpdateMenuItemStatusMessageBody msg) throws MessagingException {
        try{
            orderMessageParser.updateOrdersByMenuItemStatus(msg.getId(), msg.getStatus());
        } catch (Exception e) {
            LOG.error("Error occurred while adding table: ", e);
        }
    }
}

