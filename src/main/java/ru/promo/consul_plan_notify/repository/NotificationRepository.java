package ru.promo.consul_plan_notify.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.promo.consul_plan_notify.domain.Notification;
import ru.promo.consul_plan_notify.domain.entity.NotificationEntity;
import ru.promo.consul_plan_notify.domain.entity.NotificationType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {
    List<NotificationEntity> findAllByConsultationId(Long consultationId);

    @Query(value = """
            SELECT n.*
            FROM notification n
            JOIN consultation c ON n.consultation_id = c.id
            WHERE c.client_id = :clientId
            """, nativeQuery = true)
    List<NotificationEntity> findAllByClientId(@Param("clientId") Long clientId);

    List<NotificationEntity> findByStatusAndConsultationDate(NotificationType status, LocalDate date);

    NotificationEntity findByConsultationId(Long consultationId);
}
