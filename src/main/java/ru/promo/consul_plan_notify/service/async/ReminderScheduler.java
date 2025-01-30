package ru.promo.consul_plan_notify.service.async;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.promo.consul_plan_notify.domain.entity.NotificationEntity;
import ru.promo.consul_plan_notify.service.NotificationService;

import java.util.List;

@Slf4j
@Service
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

        int page = 0;
        int size = 50;
        List<NotificationEntity> notifications;

        do {
            notifications = notificationService.getUnsentNotificationsTomorrow(page, size);
            notifications.forEach(notification -> {
                try {
                    // Отправить уведомление
                    notificationService.sendReminder(
                            notification.getConsultationId(),
                            notification.getClientEmail(),
                            notification.getSpecialistEmail()
                    );

                    // Обновить статус уведомления на "SENT"
                    notificationService.markNotificationAsSent(notification);

                    log.info("Reminder sent for consultation ID: {}", notification.getConsultationId());
                } catch (Exception e) {
                    log.error("Failed to send reminder for consultation ID: {}", notification.getConsultationId(), e);
                }
            });
            page++;
        } while (!notifications.isEmpty());

        log.info("Daily reminders task completed");
    }
}
