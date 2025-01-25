package ru.promo.consul_plan_notify.domain;

import lombok.Data;

@Data
public class ConsultationEvent {
    private Long consultationId;
    private String clientEmail;
    private String specialistEmail;
}
