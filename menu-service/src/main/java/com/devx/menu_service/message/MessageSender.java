package com.devx.menu_service.message;

import com.devx.menu_service.dto.AddOnDto;
import com.devx.menu_service.dto.MenuItemDto;
import com.devx.menu_service.model.MenuItem;
import com.devx.menu_service.utils.AppUtils;
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

    private static class MenuItemDtoMessageBody extends MenuItemDto {
        private Long id;
        private String name;
        private List<AddOnDto> addOns;
    }

    public void sendAddMenuItemMessage(MenuItem menuItem) {
        MenuItemDtoMessageBody menuItemDto = new MenuItemDtoMessageBody();
        menuItemDto.setId(menuItem.getId());
        menuItemDto.setName(menuItem.getName());
        menuItemDto.setAddOns((long) menuItem.getAddOns().size() > 0 ? menuItem.getAddOns().stream().map(AppUtils.AddOnUtils::entityToDto).toList() : List.of());
        template.convertAndSend(direct.getName(), "addMenuItem", menuItemDto);
    }
}
