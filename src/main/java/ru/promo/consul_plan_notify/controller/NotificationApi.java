package ru.promo.consul_plan_notify.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.promo.consul_plan_notify.domain.Notification;
import ru.promo.consul_plan_notify.domain.SendReminderRequest;

import java.util.List;

@Tag(name = "Notification API", description = "API для управления уведомлениями")
@RequestMapping("/api/notifications")
public interface NotificationApi {

    @Operation(summary = "Создать уведомление")
    @PostMapping
    ResponseEntity<Notification> createNotification(@Parameter(description = "Параметры для создания уведомления") @Valid @RequestBody @NotNull Notification notification);

    @Operation(summary = "Получить уведомление по ID")
    @GetMapping("/{id}")
    ResponseEntity<Notification> getNotificationById(@Parameter(description = "ID уведомления") @PathVariable(name = "id") @NotNull @Positive Long id);

    @Operation(summary = "Обновить уведомление")
    @PutMapping
    ResponseEntity<Notification> updateNotification(@Parameter(description = "Параметры для изменения уведомления") @Valid @RequestBody @NotNull Notification notification);

    @Operation(summary = "Удалить уведомление")
    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteNotification(@Parameter(description = "ID уведомления") @PathVariable(name = "id") @NotNull @Positive Long id);

    @Operation(summary = "Получить все уведомления по ID консультации")
    @GetMapping("/consultation/{consultationId}")
    ResponseEntity<List<Notification>> getNotificationsByConsultationId(@Parameter(description = "ID консультации") @PathVariable(name = "consultationId") @NotNull @Positive Long consultationId);

    @Operation(summary = "Получить все уведомления по ID клиента")
    @GetMapping("/client/{clientId}")
    ResponseEntity<List<Notification>> getNotificationsByClientId(@Parameter(description = "ID клиента") @PathVariable(name = "clientId") @NotNull @Positive Long clientId);

    @Operation(summary = "Отправить напоминание о консультации")
    @PostMapping("/reminder/")
    ResponseEntity<Void> sendReminder(@Parameter(description = "Параметры для изменения уведомления") @Valid @RequestBody @NotNull SendReminderRequest request);
}
