package ru.promo.consul_plan_notify.service;

import ru.promo.consul_plan_notify.domain.Notification;
import ru.promo.consul_plan_notify.domain.SendReminderRequest;
import ru.promo.consul_plan_notify.domain.entity.NotificationEntity;

import java.util.List;

public interface NotificationService {
    void create(Notification dto);

    void create(NotificationEntity entity);

    Notification getById(Long id);

    void update(Notification dto);

    void delete(Long id);

    List<Notification> getAllByConsultationId(Long consultationId);

    List<Notification> getAllByClientId(Long clientId);

    void sendReminder(SendReminderRequest request);
}
