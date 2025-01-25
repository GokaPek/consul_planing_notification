package ru.promo.consul_plan_notify.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.promo.consul_plan_notify.domain.ConsultationEvent;
import ru.promo.consul_plan_notify.domain.Notification;
import ru.promo.consul_plan_notify.domain.entity.NotificationEntity;
import ru.promo.consul_plan_notify.domain.entity.NotificationType;
import ru.promo.consul_plan_notify.domain.entity.TypeStatus;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class KafkaConsumerServiceImpl implements KafkaConsumerService{

    private final NotificationService notificationService;
    private final ObjectMapper objectMapper;

    @Override
    public void handleConsultationConfirmed(String event) {
        try {
            ConsultationEvent consultationEvent = objectMapper.readValue(event, ConsultationEvent.class);

            NotificationEntity notification = new NotificationEntity();
            notification.setConsultationId(consultationEvent.getConsultationId());
            notification.setClientEmail(consultationEvent.getClientEmail());
            notification.setSpecialistEmail(consultationEvent.getSpecialistEmail());
            notification.setStatus(NotificationType.UNSENT);
            notification.setType(TypeStatus.CONFORMED);
            notification.setSentDateTime(LocalDateTime.now());
            notification.setConsultationDate(consultationEvent.getConsultationDate());

            notificationService.create(notification);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handleConsultationCancelled(String event) {
        ConsultationEvent consultationEvent = null;
        try {
            consultationEvent = objectMapper.readValue(event, ConsultationEvent.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to process the cancellation of the consultation", e);
        }

        Notification notification = notificationService.getByConsultationId(consultationEvent.getConsultationId());

            if (notification != null) {
                notification.setType(TypeStatus.CANCELLED);
                notificationService.update(notification);
            }
    }
}