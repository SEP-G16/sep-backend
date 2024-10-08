package com.devx.contact_service.controller;

import com.devx.contact_service.dto.TicketResponseDto;
import com.devx.contact_service.dto.response.AddTicketResponseResponseBody;
import com.devx.contact_service.exception.BadRequestException;
import com.devx.contact_service.exception.NullFieldException;
import com.devx.contact_service.service.TicketResponseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/contact-us/ticket-response")
public class TicketResponseController {

    private final TicketResponseService ticketResponseService;

    @Autowired
    public TicketResponseController(TicketResponseService ticketResponseService) {
        this.ticketResponseService = ticketResponseService;
    }

    @PostMapping("/add")
    public ResponseEntity<Mono<AddTicketResponseResponseBody>> addTicketResponse(@RequestBody TicketResponseDto ticketResponseDto) {
        try {
            if(ticketResponseDto.hasNullFields()) {
                throw new NullFieldException("Ticket response fields cannot be null");
            }
            if(ticketResponseDto.isFormInvalid()) {
                throw new BadRequestException("Invalid form data");
            }
            return ResponseEntity.created(null).body(ticketResponseService.addTicketResponse(ticketResponseDto));
        }
        catch (BadRequestException | NullFieldException e) {
            return ResponseEntity.badRequest().body(Mono.error(e));
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body(Mono.error(e));
        }
    }
}
