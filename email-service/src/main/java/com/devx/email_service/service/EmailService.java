package com.devx.email_service.service;

import com.devx.email_service.dto.BookingDto;
import com.devx.email_service.dto.EmailDetails;
import com.devx.email_service.dto.RoomDto;
import com.devx.email_service.dto.TempBookingDto;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {


    private JavaMailSender javaMailSender;

    @Autowired
    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Value("${spring.mail.username}")
    private String sender;

    public void sendTicketResponseEmail(EmailDetails details) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(sender);
        mailMessage.setTo(details.getRecipient());
        mailMessage.setText(details.getMsgBody());
        mailMessage.setSubject(details.getSubject());
        javaMailSender.send(mailMessage);
    }

//    public String sendMailWithAttachment(EmailDetails details) throws MessagingException {
//        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
//        MimeMessageHelper mimeMessageHelper;
//
//
//            mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
//            mimeMessageHelper.setFrom(sender);
//            mimeMessageHelper.setTo(details.getRecipient());
//            mimeMessageHelper.setText(details.getMsgBody());
//            mimeMessageHelper.setSubject(details.getSubject());
//
//            FileSystemResource file = new FileSystemResource(new File(details.getAttachment()));
//
//            mimeMessageHelper.addAttachment(file.getFilename(), file);
//
//            javaMailSender.send(mimeMessage);
//            return "Mail sent Successfully";
//    }

    public void sendBookingAcceptedEmail(BookingDto bookingDto) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        helper.setSubject("Your Booking has been Approved at Ceylon Resort");

        String html =
                "<!doctype html>\n" +
                        "<html lang=\"en\" xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:th=\"http://www.thymeleaf.org\">\n" +
                        "<head>\n" +
                        "    <meta charset=\"UTF-8\">\n" +
                        "    <meta name=\"viewport\"\n" +
                        "          content=\"width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0\">\n" +
                        "    <meta http-equiv=\"X-UA-Compatible\" content=\"ie=edge\">\n" +
                        "    <title>Booking Confirmation</title>\n" +
                        "    <style>\n" +
                        "        body { font-family: Arial, sans-serif; }\n" +
                        "        .container { max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #ddd; border-radius: 10px; }\n" +
                        "        h2 { color: #2c3e50; }\n" +
                        "        .room-grid { display: grid; grid-template-columns: repeat(auto-fit, minmax(100px, 1fr)); gap: 10px; }\n" +
                        "        .room { padding: 10px; border: 1px solid #ccc; text-align: center; border-radius: 5px; }\n" +
                        "        .button { background-color: #3498db; color: white; padding: 10px 20px; text-align: center; text-decoration: none; display: inline-block; border-radius: 5px; margin-top: 20px; }\n" +
                        "    </style>\n" +
                        "</head>\n" +
                        "<body>\n" +
                        "    <div class='container'>\n" +
                        "        <h2>Welcome to CeylonResort, <b>" + bookingDto.getCustomerName() + "</b></h2>\n" +
                        "        <p>We are pleased to inform you that your booking made on <b>" + bookingDto.getCreatedAt() + "</b> has been approved. We look forward to welcoming you and hope you have a relaxing and enjoyable stay with us.</p>\n" +
                        "        <h3>Your Assigned Rooms:</h3>\n" +
                        "        <div class='room-grid'>\n";

// Append the room details dynamically
        for (RoomDto room : bookingDto.getRoomList()) {
            html +=
                    "            <div class='room'><b>" + room.getRoomNo() + "</b><br>" + room.getRoomType() + "</div>\n";
        }

        html +=
                "        </div>\n" +
                        "        <a href='https://www.ceylonresort.com' target='_blank' class='button'>Visit Ceylon Resort</a>\n" +
                        "        <p>Thank you for choosing CeylonResort. We look forward to your arrival!</p>\n" +
                        "    </div>\n" +
                        "</body>\n" +
                        "</html>";

        helper.setText(html, true);
        helper.setTo(bookingDto.getEmail());
        helper.setFrom(sender);
        javaMailSender.send(mimeMessage);
    }

    public void sendBookingRejectedEmail(TempBookingDto tempBookingDto) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        helper.setSubject("We're Sorry to Inform You: Booking Unavailable at Ceylon Resort – We'd Love to Welcome You in the Future!");

        String html =
                "<!doctype html>\n" +
                        "<html lang=\"en\" xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:th=\"http://www.thymeleaf.org\">\n" +
                        "<head>\n" +
                        "    <meta charset=\"UTF-8\">\n" +
                        "    <meta name=\"viewport\"\n" +
                        "          content=\"width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0\">\n" +
                        "    <meta http-equiv=\"X-UA-Compatible\" content=\"ie=edge\">\n" +
                        "    <title>Booking Unavailable</title>\n" +
                        "    <style>\n" +
                        "        body { font-family: Arial, sans-serif; }\n" +
                        "        .container { max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #ddd; border-radius: 10px; }\n" +
                        "        h2 { color: #e74c3c; }\n" +
                        "        p { color: #555; }\n" +
                        "        .button { background-color: #3498db; color: white; padding: 10px 20px; text-align: center; text-decoration: none; display: inline-block; border-radius: 5px; margin-top: 20px; }\n" +
                        "    </style>\n" +
                        "</head>\n" +
                        "<body>\n" +
                        "    <div class='container'>\n" +
                        "        <h2>Dear <b>" + tempBookingDto.getCustomerName() + "</b>,</h2>\n" +
                        "        <p>We regret to inform you that unfortunately, we were unable to confirm your booking made on <b>" + tempBookingDto.getCreatedAt() + "</b> at Ceylon Resort. Our rooms are fully booked during the selected dates, and we sincerely apologize for the inconvenience caused.</p>\n" +
                        "        <p>We understand this may be disappointing, but we would love the opportunity to host you in the future. If your schedule allows, please feel free to explore other dates or contact us directly to assist with future reservations.</p>\n" +
                        "        <p>At Ceylon Resort, we’re dedicated to ensuring you have a welcoming and memorable stay whenever you decide to visit.</p>\n" +
                        "        <a href='https://www.ceylonresort.com' target='_blank' class='button'>Browse Available Dates</a>\n" +
                        "        <p>Thank you for considering Ceylon Resort, and we truly hope to welcome you soon!</p>\n" +
                        "        <p>Best regards,<br>The Ceylon Resort Team</p>\n" +
                        "    </div>\n" +
                        "</body>\n" +
                        "</html>";
        helper.setText(html, true);
        helper.setTo(tempBookingDto.getEmail());
        helper.setFrom(sender);
        javaMailSender.send(mimeMessage);
    }
}
