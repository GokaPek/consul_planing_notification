package ru.promo.consul_plan_notify.domain;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import ru.promo.consul_plan_notify.domain.entity.NotificationType;
import ru.promo.consul_plan_notify.domain.entity.TypeStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class Notification {
    private Long id;
    @NotNull(message = "Идентификатор консультации не может быть пустым")
    @Positive(message = "Идентификатор консультации должен быть положительным числом")
    private Long consultationId;
    @NotNull(message = "Тип уведомления не может быть пустым")
    private TypeStatus type;
    @NotNull(message = "Дата и время отправки не могут быть пустыми")
    private LocalDateTime sentDateTime;
    @NotNull(message = "Статус уведомления не может быть пустым")
    private NotificationType status;
    @NotNull(message = "Почта клиента не может быть пустой")
    @Email(message = "Почта клиента должна быть корректна")
    private String clientEmail;
    @NotNull(message = "Почта специалиста не может быть пустой")
    @Email(message = "Почта клиента должна быть корректна")
    private String specialistEmail;
    @NotNull(message = "Дата и время консультации не могут быть пустыми")
    private LocalDate consultationDate;
}
