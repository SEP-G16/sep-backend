package com.devx.menu_service.message;

import com.devx.menu_service.dto.AddOnDto;
import com.devx.menu_service.dto.MenuItemDto;
import com.devx.menu_service.dto.request.UpdateMenuItemStatusRequestBody;
import com.devx.menu_service.enums.MenuItemStatus;
import com.devx.menu_service.model.MenuItem;
import com.devx.menu_service.utils.AppUtils;
import lombok.Getter;
import lombok.Setter;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MessageSender {

    private final RabbitTemplate template;

    private final DirectExchange orderDirect;

    private final DirectExchange wsDirect;

    public MessageSender(RabbitTemplate template, @Qualifier("orderDirect") DirectExchange orderDirect, @Qualifier("wsDirect") DirectExchange wsDirect) {
        this.template = template;
        this.orderDirect = orderDirect;
        this.wsDirect = wsDirect;
    }

    private static class MenuItemDtoMessageBody extends MenuItemDto {
        private Long id;
        private String name;
        private List<AddOnDto> addOns;
    }

    @Getter
    @Setter
    private static class UpdateMenuItemStatusMessageBody{
        private Long id;
        private MenuItemStatus status;
    }

    public void sendMenuItemStatusChangeMessage(Long id, MenuItemStatus status) {
        UpdateMenuItemStatusMessageBody msg = new UpdateMenuItemStatusMessageBody();
        msg.setId(id);
        msg.setStatus(status);
        template.convertAndSend(wsDirect.getName(), "updateMenuItemStatus", msg);
    }


    public void sendAddMenuItemMessage(MenuItem menuItem) {
        MenuItemDtoMessageBody menuItemDto = new MenuItemDtoMessageBody();
        menuItemDto.setId(menuItem.getId());
        menuItemDto.setName(menuItem.getName());
        menuItemDto.setAddOns((long) menuItem.getAddOns().size() > 0 ? menuItem.getAddOns().stream().map(AppUtils.AddOnUtils::entityToDto).toList() : List.of());
        template.convertAndSend(orderDirect.getName(), "addMenuItem", menuItemDto);
    }
}
