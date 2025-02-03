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
    private static final int PAGE_SIZE = 50;

    @Scheduled(cron = "${reminder.scheduler.cron}")
    public void sendDailyReminders() {
        log.info("Starting daily reminders task");

        List<NotificationEntity> notifications = notificationService.getUnsentNotificationsTomorrow(0, PAGE_SIZE);

        if (notifications.isEmpty()) {
            log.info("No notifications to send.");
            return;
        }

        notifications.forEach(notification -> {
            try {
                // Отправить уведомление
                notificationService.sendReminder(notification);

                // Обновить статус уведомления на "SENT"
                notificationService.markNotificationAsSent(notification);

                log.info("Reminder sent for consultation ID: {}", notification.getConsultationId());
            } catch (Exception e) {
                log.error("Failed to send reminder for consultation ID: {}", notification.getConsultationId(), e);
            }
        });

        log.info("Daily reminders task completed");
    }
}
