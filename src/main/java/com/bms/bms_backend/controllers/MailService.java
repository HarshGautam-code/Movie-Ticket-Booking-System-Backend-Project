package com.bms.bms_backend.controllers;


import com.bms.bms_backend.models.Bill;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Slf4j
@Service
public class MailService {


    public JavaMailSender getJavaMailSender(){

        JavaMailSenderImpl javamailSender = new JavaMailSenderImpl();
        javamailSender.setHost("smtp.gmail.com");
        javamailSender.setPort(587);
        javamailSender.setUsername("");
        javamailSender.setPassword("");

        Properties props = javamailSender.getJavaMailProperties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        return javamailSender;
    }


    public void sendBillDetailsToCustomer(Bill bill) {
        StringBuilder ticketDetails = new StringBuilder();

        for (int i = 0; i < bill.getSeatBookings().size(); i++) {
            var booking = bill.getSeatBookings().get(i);
            ticketDetails.append(String.format(
                    "Ticket %d:\n  Seat: %s | Ticket ID: %s\n  Movie: %s | Show: %s\n  Time: %s\n\n",
                    i + 1,
                    booking.getSeatId(),
                    booking.getTicketId(),
                    booking.getShow().getMovie().getMovieName(),
                    booking.getShow().getShowName(),
                    booking.getShow().getDisplayStartTime()
            ));
        }

        String finalMailText = String.format(
                "========================================\n" +
                        "    BOOKING CONFIRMATION RECEIPT\n" +
                        "========================================\n\n" +
                        "Dear %s,\n\n" +
                        "Thank you for your booking!\n\n" +
                        "BOOKING DETAILS\n" +
                        "Bill ID: %s\n" +
                        "Payment ID: %s\n" +
                        "Payment Source: %s\n\n" +
                        "THEATER INFORMATION\n" +
                        "Theater: %s\n" +
                        "Address: %s, %s, %s - %d\n\n" +
                        "TICKET DETAILS\n" +
                        "Number of Seats: %d\n\n" +
                        "%s" +
                        "TOTAL AMOUNT: ₹%.2f\n\n" +
                        "========================================\n" +
                        "Please arrive 15 minutes early.\n" +
                        "Show this email at the counter.\n" +
                        "Enjoy your movie!\n" +
                        "========================================\n",
                bill.getCustomer().getUserName(),
                bill.getId(),
                bill.getPaymentId(),
                bill.getPaymentSource(),
                bill.getTheater().getTheaterName(),
                bill.getTheater().getAddress(),
                bill.getTheater().getCity(),
                bill.getTheater().getState(),
                bill.getTheater().getPincode(),
                bill.getSeatBookings().size(),
                ticketDetails.toString(),
                bill.getTotalPrice()
        );

        // How we will send the mail ->
        // So, to send the mail our api requires its own email id and password

        JavaMailSender javaMailSender = this.getJavaMailSender();
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        try {
            mimeMessageHelper.setSubject("Congratulations !! Booking done");
            mimeMessageHelper.setText(finalMailText);
            mimeMessageHelper.setTo(bill.getCustomer().getEmail());
            javaMailSender.send(mimeMessage);

        }
        catch (Exception e){
            log.info(e.getMessage());
        }
    }


}
