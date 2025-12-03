package com.pjatk.core.port.out;

public interface NotificationPort {
    record NotificationDetails(
            String email,
            String subject,
            String body
    ){}
    void sendNotification(NotificationDetails details);
}
