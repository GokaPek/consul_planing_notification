package ru.promo.consul_plan_notify.service.async;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.promo.consul_plan_notify.client.ConsultationClient;
import ru.promo.consul_plan_notify.domain.Consultation;
import ru.promo.consul_plan_notify.domain.ConsultationDetails;
import ru.promo.consul_plan_notify.domain.Schedule;
import ru.promo.consul_plan_notify.domain.entity.TypeStatus;
import ru.promo.consul_plan_notify.service.NotificationService;

import java.time.LocalDate;
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
    private final ConsultationClient consultationClient;

    @Scheduled(cron = "${reminder.scheduler.cron}")
    public void sendDailyReminders() {
        LocalDate now = LocalDate.now();
        LocalDate tomorrow = now.plusDays(1);

        // Получить расписания на завтра из основного проекта
        List<Schedule> schedules = consultationClient.getSchedulesByDate(tomorrow);

        for (Schedule schedule : schedules) {
            // Получить консультации для клиента из основного проекта
            List<Consultation> consultations = consultationClient.getConsultationsByClientId(schedule.getClientId());

            for (Consultation consultation : consultations) {
                // Отправить напоминание, если консультация подтверждена
                if (consultation.getStatus() == TypeStatus.CONFORMED) {
                    // Получить детали консультации (clientEmail и specialistEmail)
                    ConsultationDetails details = consultationClient.getConsultationDetails(consultation.getId());

                    // Отправить напоминание
                    notificationService.sendReminder(
                            consultation.getId(),
                            details.getClientEmail(),
                            details.getSpecialistEmail()
                    );

                    // Обновить статус отправки напоминания в основном проекте
                    consultationClient.markReminderSent(consultation.getId());
                }
            }
        }
        log.info("Daily reminders task completed");
    }
}