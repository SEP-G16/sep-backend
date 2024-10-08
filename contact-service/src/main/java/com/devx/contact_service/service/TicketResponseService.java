package com.devx.contact_service.service;

import com.devx.contact_service.dto.TicketResponseDto;
import com.devx.contact_service.dto.response.AddTicketResponseResponseBody;
import com.devx.contact_service.model.TicketResponse;
import com.devx.contact_service.service.integration.TicketResponseServiceIntegration;
import com.devx.contact_service.utils.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class TicketResponseService {
    private final TicketResponseServiceIntegration ticketResponseServiceIntegration;


    @Autowired
    public TicketResponseService(TicketResponseServiceIntegration ticketResponseServiceIntegration) {
        this.ticketResponseServiceIntegration = ticketResponseServiceIntegration;
    }

    public Mono<AddTicketResponseResponseBody> addTicketResponse(TicketResponseDto ticketResponseDto) {
        ticketResponseDto.getSupportTicket().setResponses(List.of());
        TicketResponse ticketResponse = AppUtils.TicketResponseUtils.convertDtoToEntity(ticketResponseDto);
        return ticketResponseServiceIntegration.addTicketResponse(ticketResponse);
    }
}
