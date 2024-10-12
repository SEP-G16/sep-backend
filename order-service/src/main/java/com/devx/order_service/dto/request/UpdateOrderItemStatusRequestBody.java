package com.devx.order_service.dto.request;

import com.devx.order_service.enums.OrderItemStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateOrderItemStatusRequestBody {
    private Long orderId;
    private Long orderItemId;
    private OrderItemStatus status;

    @JsonIgnore
    public boolean hasNullFields(){
        return orderId == null || orderItemId == null || status == null;
    }
}
