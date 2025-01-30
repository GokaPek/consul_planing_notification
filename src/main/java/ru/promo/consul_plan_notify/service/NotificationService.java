package ru.promo.consul_plan_notify.service;

import org.springframework.data.domain.Page;
import ru.promo.consul_plan_notify.domain.ConsultationEvent;
import ru.promo.consul_plan_notify.domain.Notification;
import ru.promo.consul_plan_notify.domain.SendReminderRequest;
import ru.promo.consul_plan_notify.domain.entity.NotificationEntity;

import java.util.List;

public interface NotificationService {
    void handleNotification(ConsultationEvent event);

    void create(Notification dto);

    void create(NotificationEntity entity);

    Notification getById(Long id);

    void update(Notification dto);

    void delete(Long id);

    List<Notification> getAllByConsultationId(Long consultationId);

    void sendReminder(NotificationEntity reminder);

    List<NotificationEntity> getUnsentNotificationsTomorrow(int page, int size);

    void markNotificationAsSent(NotificationEntity notification);
}
