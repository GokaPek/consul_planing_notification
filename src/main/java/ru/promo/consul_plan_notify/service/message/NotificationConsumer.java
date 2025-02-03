package ru.promo.consul_plan_notify.service.message;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.promo.consul_plan_notify.domain.ConsultationEvent;
import ru.promo.consul_plan_notify.domain.entity.TypeStatus;
import ru.promo.consul_plan_notify.service.NotificationService;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationConsumer {

    private final NotificationService notificationService;

    @KafkaListener(topics = "consultation-topic", groupId = "notification-service")
    public void handleConsultationEvent(ConsultationEvent consultationEvent) {
        try {
            notificationService.handleNotification(consultationEvent);
            log.info("Received consultation event: {}", consultationEvent);
        } catch (Exception e) {
            log.error("Failed to process consultation event: {}", consultationEvent, e);
        }
    }
}