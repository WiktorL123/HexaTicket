package com.pjatk.mailer;

import com.pjatk.core.port.out.NotificationPort;
import org.springframework.stereotype.Component;

@Component
public class MailerAdapter implements NotificationPort {
    @Override
    public void sendEmail(String email) {
        System.out.println("email sent to: " + email);
    }
}
