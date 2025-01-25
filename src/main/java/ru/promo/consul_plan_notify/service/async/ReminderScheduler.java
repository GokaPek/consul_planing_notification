package ru.promo.consul_plan_notify.service.async;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.promo.consul_plan_notify.domain.entity.NotificationEntity;
import ru.promo.consul_plan_notify.domain.entity.NotificationType;
import ru.promo.consul_plan_notify.service.NotificationService;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@ConditionalOnProperty(
        prefix = "reminder.scheduler",
        name = "enabled",
        havingValue = "true"
)
public class ReminderScheduler {

    private final NotificationService notificationService;

    @Scheduled(cron = "${reminder.scheduler.cron}")
    public void sendDailyReminders() {
        log.info("Starting daily reminders task");

        // Получить уведомления, которые нужно отправить (статус UNSENT)
        List<NotificationEntity> notifications = notificationService.getUnsentNotifications();

        for (NotificationEntity notification : notifications) {
            try {
                // Отправить уведомление
                notificationService.sendReminder(
                        notification.getConsultationId(),
                        notification.getClientEmail(),
                        notification.getSpecialistEmail()
                );

                // Обновить статус уведомления на "SENT"
                notificationService.markNotificationAsSent(notification.getId());

                log.info("Reminder sent for consultation ID: {}", notification.getConsultationId());
            } catch (Exception e) {
                log.error("Failed to send reminder for consultation ID: {}", notification.getConsultationId(), e);
            }
        }

        log.info("Daily reminders task completed");
    }
}