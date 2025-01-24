package ru.promo.consul_plan_notify.domain;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Schedule {
    private Long id;
    private Long specialistId;
    private Long clientId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
