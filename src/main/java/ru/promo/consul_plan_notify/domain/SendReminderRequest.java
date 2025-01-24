package ru.promo.consul_plan_notify.domain;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SendReminderRequest {
    @NotNull(message = "Id консультанта не может быть пустым")
    @Positive(message = "Не корректный id консультации")
    private Long consultationId;
    @Email(message = "Не корректный email адрес")
    @NotNull(message = "Почта не может быть пустой")
    private String clientEmail;
    @NotNull(message = "Имя специалиста не может быть пустым")
    private String specialistName;
}
