package ru.promo.consul_plan_notify.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ConsultationDetails {
    private Long consultationId;
    private String clientEmail;
    private String specialistEmail;
}
