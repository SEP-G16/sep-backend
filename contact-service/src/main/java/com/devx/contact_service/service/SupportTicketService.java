package com.devx.contact_service.service;

import com.devx.contact_service.dto.SupportTicketDto;
import com.devx.contact_service.dto.response.AddSupportTicketResponseBody;
import com.devx.contact_service.dto.response.GetAllSupportTicketsSingleTicketDto;
import com.devx.contact_service.model.SupportTicket;
import com.devx.contact_service.service.integration.SupportTicketServiceIntegration;
import com.devx.contact_service.utils.AppUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;

@Service
public class SupportTicketService {
    private final SupportTicketServiceIntegration supportTicketServiceIntegration;


    @Autowired
    public SupportTicketService(SupportTicketServiceIntegration supportTicketServiceIntegration) {
        this.supportTicketServiceIntegration = supportTicketServiceIntegration;
    }

    public Mono<AddSupportTicketResponseBody> addSupportTicket(SupportTicketDto supportTicketDto) {
        supportTicketDto.setResponses(new ArrayList<>());
        SupportTicket supportTicket = AppUtils.SupportTicketUtils.convertDtoToEntity(supportTicketDto);
        return supportTicketServiceIntegration.addSupportTicket(supportTicket);
    }

    public Flux<GetAllSupportTicketsSingleTicketDto> getAllSupportTicket(){
        return supportTicketServiceIntegration.getAllSupportTicket();
    }
}
