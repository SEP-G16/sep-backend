package com.devx.booking_service.message;

import com.devx.booking_service.dto.BookingDto;
import com.devx.booking_service.dto.TempBookingDto;
import com.devx.booking_service.model.Booking;
import com.devx.booking_service.model.TempBooking;
import com.devx.booking_service.utils.AppUtils;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MessageSender {

    @Autowired
    private RabbitTemplate template;

    @Autowired
    private DirectExchange direct;

    public void sendBookingRejectedMessage(TempBooking tempBooking) {
        TempBookingDto dto = AppUtils.TempBookingUtils.convertEntityToDto(tempBooking);
        template.convertAndSend(direct.getName(), "sendBookingRejected", dto);
    }

    public void sendBookingApprovedMessage(Booking booking) {
        BookingDto dto = AppUtils.BookingUtils.convertEntityToDto(booking);
        template.convertAndSend(direct.getName(), "sendBookingApproved", dto);
    }
}
