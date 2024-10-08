package com.devx.contact_service.integration.repo;

import com.devx.contact_service.integration.BaseIntegrationTestConfiguration;
import com.devx.contact_service.model.SupportTicket;
import com.devx.contact_service.model.TicketResponse;
import com.devx.contact_service.repository.SupportTicketRepository;
import com.devx.contact_service.repository.TicketResponseRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class SupportTicketAndTicketResponseRepoTests extends BaseIntegrationTestConfiguration {

    private final SupportTicketRepository supportTicketRepository;

    private final TicketResponseRepository ticketResponseRepository;

    @Autowired
    public SupportTicketAndTicketResponseRepoTests(SupportTicketRepository supportTicketRepository, TicketResponseRepository ticketResponseRepository) {
        this.supportTicketRepository = supportTicketRepository;
        this.ticketResponseRepository = ticketResponseRepository;
    }

    private SupportTicket supportTicket;

    @BeforeEach
    void setUp() {
        ticketResponseRepository.deleteAll();
        supportTicketRepository.deleteAll();

        supportTicket = new SupportTicket();
        supportTicket.setName("John Doe");
        supportTicket.setEmail("jdoe@example.com");
        supportTicket.setDescription("Test Subject");
    }

    @Test
    void testAddSupportTicket() {
        SupportTicket saved = supportTicketRepository.save(supportTicket);
        Assertions.assertNotNull(saved);
        Assertions.assertEquals(supportTicket.getName(), saved.getName());
        Assertions.assertEquals(supportTicket.getEmail(), saved.getEmail());
        Assertions.assertEquals(supportTicket.getDescription(), saved.getDescription());
    }

    @Test
    void testTicketResponseUpdate() {
        SupportTicket saved = supportTicketRepository.save(supportTicket);

        TicketResponse ticketResponse = new TicketResponse();
        ticketResponse.setResponse("Test Response");
        ticketResponse.setSupportTicket(saved);

        saved.setResponses(List.of(ticketResponse));
        SupportTicket updated = supportTicketRepository.save(saved);
        Assertions.assertNotNull(updated);
        Assertions.assertEquals(1, updated.getResponses().size());

        TicketResponse response = updated.getResponses().get(0);

        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getId());
        Assertions.assertEquals(ticketResponse.getResponse(), response.getResponse());

        TicketResponse savedResponse = ticketResponseRepository.findById(response.getId()).orElse(null);

        assert savedResponse != null;
    }

    @Test
    void testMultipleTicketResponseUpdate() {
        SupportTicket saved = supportTicketRepository.save(supportTicket);

        TicketResponse ticketResponse1 = new TicketResponse();
        ticketResponse1.setResponse("Test Response");
        ticketResponse1.setSupportTicket(saved);

        saved.setResponses(List.of(ticketResponse1));
        SupportTicket firstUpdate = supportTicketRepository.save(saved);
        Assertions.assertNotNull(firstUpdate);
        Assertions.assertEquals(1, firstUpdate.getResponses().size());

        TicketResponse ticketResponse2 = new TicketResponse();
        ticketResponse2.setResponse("Test Response 2");
        ticketResponse2.setSupportTicket(firstUpdate);

        List<TicketResponse> existingResponses = firstUpdate.getResponses();
        existingResponses.add(ticketResponse2);
        firstUpdate.setResponses(existingResponses);
        SupportTicket secondUpdate = supportTicketRepository.save(firstUpdate);
        Assertions.assertNotNull(secondUpdate);
        Assertions.assertEquals(2, secondUpdate.getResponses().size());
    }

    @Test
    void testAddTicketResponseForSupportTicket() {
        SupportTicket savedSupportTicket = supportTicketRepository.save(supportTicket);

        TicketResponse ticketResponse1 = new TicketResponse();
        ticketResponse1.setResponse("Test Response");
        ticketResponse1.setSupportTicket(savedSupportTicket);

        TicketResponse savedResponse = ticketResponseRepository.save(ticketResponse1);
        Assertions.assertNotNull(savedResponse);

        SupportTicket updatedSupportTicket = supportTicketRepository.findById(savedSupportTicket.getId()).orElse(null);
        assert updatedSupportTicket != null;

        Assertions.assertEquals(1, updatedSupportTicket.getResponses().size());
    }

    @Test
    void testAddMultipleTicketResponsesForSupportTicket() {
        SupportTicket savedSupportTicket = supportTicketRepository.save(supportTicket);

        TicketResponse ticketResponse1 = new TicketResponse();
        ticketResponse1.setResponse("Test Response");
        ticketResponse1.setSupportTicket(savedSupportTicket);

        TicketResponse savedResponse1 = ticketResponseRepository.save(ticketResponse1);
        Assertions.assertNotNull(savedResponse1);

        SupportTicket firstUpdatedSupportTicket = supportTicketRepository.findById(savedSupportTicket.getId()).orElse(null);
        assert firstUpdatedSupportTicket != null;

        TicketResponse ticketResponse2 = new TicketResponse();
        ticketResponse2.setResponse("Test Response 2");
        ticketResponse2.setSupportTicket(savedSupportTicket);

        TicketResponse savedResponse2 = ticketResponseRepository.save(ticketResponse2);
        Assertions.assertNotNull(savedResponse2);

        SupportTicket secondUpdatedSupportTicket = supportTicketRepository.findById(savedSupportTicket.getId()).orElse(null);
        assert secondUpdatedSupportTicket != null;

        Assertions.assertEquals(2, secondUpdatedSupportTicket.getResponses().size());
    }
}
