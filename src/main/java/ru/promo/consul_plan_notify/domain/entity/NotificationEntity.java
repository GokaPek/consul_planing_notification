package ru.promo.consul_plan_notify.domain.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "notification")
public class NotificationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @Column(name = "consultation_id", nullable = false)
    private Long consultationId;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 50)
    private TypeStatus type;

    @Column(name = "sent_date_time", nullable = false)
    private LocalDateTime sentDateTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 50)
    private NotificationType status;

    @Column(name = "client_email", nullable = false)
    private String clientEmail;

    @Column(name = "specialist_email", nullable = false)
    private String specialistEmail;

    @Column(name = "consultation_date", nullable = false)
    private LocalDate consultationDate;
}