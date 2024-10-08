package com.devx.contact_service.service.integration;

import com.devx.contact_service.dto.SupportTicketDto;
import com.devx.contact_service.dto.response.AddSupportTicketResponseBody;
import com.devx.contact_service.dto.response.GetAllSupportTicketsSingleTicketDto;
import com.devx.contact_service.model.SupportTicket;
import com.devx.contact_service.repository.SupportTicketRepository;
import com.devx.contact_service.utils.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

import java.util.List;
import java.util.Optional;

@Component
public class SupportTicketServiceIntegration {
    private final Scheduler jdbcScheduler;
    private final SupportTicketRepository supportTicketRepository;

    @Autowired
    public SupportTicketServiceIntegration(@Qualifier("jdbcScheduler") Scheduler jdbcScheduler, SupportTicketRepository supportTicketRepository) {
        this.jdbcScheduler = jdbcScheduler;
        this.supportTicketRepository = supportTicketRepository;
    }

    private SupportTicket addSupportTicketInternal(SupportTicket ticket) {
        return supportTicketRepository.save(ticket);
    }

    public Mono<AddSupportTicketResponseBody> addSupportTicket(SupportTicket ticket) {
        return Mono.fromCallable(() -> addSupportTicketInternal(ticket)).subscribeOn(jdbcScheduler).map(AppUtils.SupportTicketUtils::convertEntityToDto).map(AppUtils.ResponseDtoMapper::convertDtoToAddTicketResponseBody);
    }

    private List<SupportTicket> getAllSupportTicketInternal()
    {
        List<SupportTicket> tickets = supportTicketRepository.findAll();
        List<SupportTicket> modified = tickets.stream().peek((ticket) -> Optional.ofNullable(ticket.getResponses()).ifPresentOrElse((responses) -> {
            responses.forEach((response) -> {
                response.setSupportTicket(null);
            });
        }, () -> {
            ticket.setResponses(List.of());
        })).toList();
        return modified;
    }

    public Flux<GetAllSupportTicketsSingleTicketDto> getAllSupportTicket() {
        return Mono.fromCallable(() -> getAllSupportTicketInternal().stream().map(AppUtils.SupportTicketUtils::convertEntityToDto).toList().stream().map(AppUtils.ResponseDtoMapper::convertDtoToResponse).toList()).subscribeOn(jdbcScheduler).flatMapMany(Flux::fromIterable);
    }
}
