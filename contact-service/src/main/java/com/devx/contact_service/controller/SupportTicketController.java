package com.devx.contact_service.controller;

import com.devx.contact_service.dto.SupportTicketDto;
import com.devx.contact_service.dto.response.AddSupportTicketResponseBody;
import com.devx.contact_service.dto.response.GetAllSupportTicketsSingleTicketDto;
import com.devx.contact_service.exception.BadRequestException;
import com.devx.contact_service.exception.NullFieldException;
import com.devx.contact_service.service.SupportTicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/contact-us/support-ticket")
public class SupportTicketController {
    // Class data members
    private final SupportTicketService supportTicketService;

    @Autowired
    public SupportTicketController(SupportTicketService supportTicketService) {
        this.supportTicketService = supportTicketService;
    }

    @PostMapping("/add")
    public ResponseEntity<Mono<AddSupportTicketResponseBody>> addSupportTicket(@RequestBody SupportTicketDto supportTicketDto) {
        try {
            if (supportTicketDto.hasNullFields()) {
                throw new NullFieldException("Support ticket fields cannot be null");
            }
            if (supportTicketDto.isFormInvalid()) {
                throw new BadRequestException("Invalid form data");
            }
            return ResponseEntity.created(null).body(supportTicketService.addSupportTicket(supportTicketDto));
        } catch (BadRequestException | NullFieldException e) {
            return ResponseEntity.badRequest().body(Mono.error(e));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Mono.error(e));
        }
    }

    @GetMapping("/all")
    public ResponseEntity<Flux<GetAllSupportTicketsSingleTicketDto>> getAllSupportTicket() {
        try {
            return ResponseEntity.ok().body(supportTicketService.getAllSupportTicket());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Flux.error(e));
        }
    }
}
