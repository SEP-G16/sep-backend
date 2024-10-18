package com.devx.order_service.dto.message;

import com.devx.order_service.enums.MenuItemStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateMenuItemStatusMessageBody {
    Long id;
    MenuItemStatus status;
}