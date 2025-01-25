package ru.promo.consul_plan_notify.service;

import org.springframework.kafka.annotation.KafkaListener;

public interface KafkaConsumerService {
    @KafkaListener(topics = "consultation-confirmed", groupId = "notification-service")
    void handleConsultationConfirmed(String event);
    @KafkaListener(topics = "consultation-cancelled", groupId = "notification-service")
    void handleConsultationCancelled(String event);
}