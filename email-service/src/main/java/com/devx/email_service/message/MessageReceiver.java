package com.devx.email_service.message;

import com.devx.email_service.dto.BookingDto;
import com.devx.email_service.dto.EmailDetails;
import com.devx.email_service.dto.TempBookingDto;
import com.devx.email_service.service.EmailService;
import jakarta.mail.MessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MessageReceiver {

    public static Logger LOG = LoggerFactory.getLogger(MessageReceiver.class);


    private EmailService emailService;

    @Autowired
    public MessageReceiver(EmailService emailService) {
        this.emailService = emailService;
    }

    @RabbitListener(queues = "#{sendTicketResponseQueue.name}")
    public void sendTicketResponseMessageHandler(EmailDetails details) throws InterruptedException {
        // Handle Message
        LOG.info(details.toString());
        emailService.sendTicketResponseEmail(details);
    }

    @RabbitListener(queues = "#{sendBookingApprovedQueue.name}")
    public void sendBookingAcceptedMessageHandler(BookingDto bookingDto) throws InterruptedException, MessagingException {
        // Handle Message
        LOG.info(bookingDto.toString());
        emailService.sendBookingAcceptedEmail(bookingDto);
    }

    @RabbitListener(queues = "#{sendBookingRejectedQueue.name}")
    public void sendBookingRejectedMessageHandler(TempBookingDto tempBookingDto) throws InterruptedException, MessagingException {
        // Handle Message
        LOG.info(tempBookingDto.toString());
        emailService.sendBookingRejectedEmail(tempBookingDto);
    }
}

