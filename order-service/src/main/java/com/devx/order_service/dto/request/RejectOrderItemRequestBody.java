package com.devx.order_service.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RejectOrderItemRequestBody {
    private Long orderId;
    private Long orderItemId;

    public boolean hasNullFields(){
        return orderId == null || orderItemId == null;
    }
}
