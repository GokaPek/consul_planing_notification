package ru.promo.consul_plan_notify.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.promo.consul_plan_notify.domain.Consultation;
import ru.promo.consul_plan_notify.domain.ConsultationDetails;
import ru.promo.consul_plan_notify.domain.Schedule;

import java.time.LocalDate;
import java.util.List;

@FeignClient(name = "consultation-service", url = "${feign.client.config.consultation-service.url}")
public interface ConsultationClient {

    @GetMapping("/api/schedules")
    List<Schedule> getSchedulesByDate(@RequestParam("date") LocalDate date);

    @GetMapping("/api/consultations/client/{clientId}")
    List<Consultation> getConsultationsByClientId(@PathVariable("clientId") Long clientId);

    @PutMapping("/api/consultations/{consultationId}/mark-reminder-sent")
    void markReminderSent(@PathVariable("consultationId") Long consultationId);

    @GetMapping("/api/consultations/{consultationId}/details")
    ConsultationDetails getConsultationDetails(@PathVariable("consultationId") Long consultationId);
}
