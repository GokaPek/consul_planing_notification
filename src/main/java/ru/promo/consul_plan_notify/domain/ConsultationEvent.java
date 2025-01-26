package ru.promo.consul_plan_notify.domain;

import lombok.Data;
import ru.promo.consul_plan_notify.domain.entity.TypeStatus;

import java.time.LocalDate;

@Data
public class ConsultationEvent {
    private Long consultationId;
    private String clientEmail;
    private String specialistEmail;
    private LocalDate consultationDate;
    private TypeStatus status;
}
