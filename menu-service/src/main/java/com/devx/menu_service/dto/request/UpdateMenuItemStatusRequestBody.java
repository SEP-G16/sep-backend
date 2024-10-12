package com.devx.menu_service.dto.request;

import com.devx.menu_service.enums.MenuItemStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateMenuItemStatusRequestBody {
    private Long id;
    private MenuItemStatus status;
}
