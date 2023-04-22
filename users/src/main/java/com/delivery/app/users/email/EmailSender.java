package com.delivery.app.users.email;

import jakarta.mail.MessagingException;

public interface EmailSender {
    void sendEmailVerification(String receiver,
                               String email) throws MessagingException;
}
