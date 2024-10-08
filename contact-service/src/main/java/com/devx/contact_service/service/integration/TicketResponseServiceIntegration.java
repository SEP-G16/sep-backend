package com.devx.contact_service.service.integration;

import com.devx.contact_service.dto.TicketResponseDto;
import com.devx.contact_service.dto.response.AddTicketResponseResponseBody;
import com.devx.contact_service.exception.SupportTicketNotFoundException;
import com.devx.contact_service.message.MessageSender;
import com.devx.contact_service.model.EmailDetails;
import com.devx.contact_service.model.SupportTicket;
import com.devx.contact_service.model.TicketResponse;
import com.devx.contact_service.repository.SupportTicketRepository;
import com.devx.contact_service.repository.TicketResponseRepository;
import com.devx.contact_service.utils.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

import java.util.List;

@Component
public class TicketResponseServiceIntegration {
    private final Scheduler jdbcScheduler;
    private final TicketResponseRepository ticketResponseRepository;
    private final SupportTicketRepository supportTicketRepository;
    private final MessageSender messageSender;

    @Autowired
    public TicketResponseServiceIntegration(@Qualifier("jdbcScheduler") Scheduler jdbcScheduler, TicketResponseRepository ticketResponseRepository, SupportTicketRepository supportTicketRepository, MessageSender messageSender) {
        this.jdbcScheduler = jdbcScheduler;
        this.ticketResponseRepository = ticketResponseRepository;
        this.supportTicketRepository = supportTicketRepository;
        this.messageSender = messageSender;
    }

    private SupportTicket findSupportTicket(SupportTicket supportTicket) {
        return supportTicketRepository.findById(supportTicket.getId()).orElseThrow(SupportTicketNotFoundException::new);
    }

    private void sendTicketResponse(TicketResponse ticketResponse) {
        EmailDetails emailDetails = new EmailDetails();
        emailDetails.setRecipient(ticketResponse.getSupportTicket().getEmail());
        emailDetails.setMsgBody(ticketResponse.getResponse());
        emailDetails.setSubject("no-reply: Customer Support - Ceylon Resort");
        messageSender.sendTicketResponse(emailDetails);
    }

    private TicketResponse addTicketResponseInternal(TicketResponse ticketResponse) {
        SupportTicket supportTicket = findSupportTicket(ticketResponse.getSupportTicket());
        supportTicket.setResponses(List.of());
        ticketResponse.setSupportTicket(supportTicket);
        sendTicketResponse(ticketResponse);
        return ticketResponseRepository.save(ticketResponse);
    }

    public Mono<AddTicketResponseResponseBody> addTicketResponse(TicketResponse ticketResponse) {
        return Mono.fromCallable(() -> addTicketResponseInternal(ticketResponse)).subscribeOn(jdbcScheduler).map(AppUtils.TicketResponseUtils::convertEntityToDto).map(AppUtils.ResponseDtoMapper::convertTicketResponseDtoToAddTicketResponseBody);
    }
}
