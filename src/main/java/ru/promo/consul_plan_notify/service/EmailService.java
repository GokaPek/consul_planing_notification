package ru.promo.consul_plan_notify.service;

import jakarta.mail.MessagingException;

public interface EmailService {
    void sendEmail(String to, String subject, String text) throws MessagingException;
}
