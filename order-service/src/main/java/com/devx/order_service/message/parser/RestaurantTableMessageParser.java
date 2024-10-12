package com.devx.order_service.message.parser;

import com.devx.order_service.dto.RestaurantTableDto;
import com.devx.order_service.exception.NullFieldException;
import com.devx.order_service.repository.RestaurantTableRepository;
import com.devx.order_service.utils.AppUtils;
import org.springframework.stereotype.Component;

@Component
public class RestaurantTableMessageParser {
    private final RestaurantTableRepository tableRepository;

    public RestaurantTableMessageParser(RestaurantTableRepository tableRepository) {
        this.tableRepository = tableRepository;
    }

    public void addTable(RestaurantTableDto tableDto) {
        if (tableDto.hasNUllFields()) {
            throw new NullFieldException("RestaurantTableDto has null fields");
        }
        tableRepository.save(AppUtils.TableUtils.dtoToEntity(tableDto));
    }
}
