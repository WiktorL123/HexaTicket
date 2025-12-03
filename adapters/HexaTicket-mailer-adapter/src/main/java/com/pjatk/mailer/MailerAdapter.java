package com.pjatk.mailer;

import com.pjatk.core.port.out.NotificationPort;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MailerAdapter implements NotificationPort {

    private final String SENDER = "wiktorlem1@gmail.com";

    private final JavaMailSender mailSender;



    @Override
    public void sendNotification(NotificationDetails details) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(SENDER);

        message.setTo(details.email());
        message.setSubject(details.subject());
        message.setText(details.body());

        try {
            mailSender.send(message);
            System.out.println("Wysłano mail z adaptera do" + details.email());
        }catch (Exception e){
            System.out.println("ERROR:" + e.getCause());
            throw new RuntimeException("cannot send email to" + details.email() + " " + e.getMessage());
        }

    }
}
