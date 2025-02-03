package ru.promo.consul_plan_notify.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.promo.consul_plan_notify.domain.entity.NotificationEntity;
import ru.promo.consul_plan_notify.domain.entity.NotificationType;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {
    List<NotificationEntity> findAllByConsultationId(Long consultationId);

    List<NotificationEntity> findByStatusAndConsultationDate(Pageable page, NotificationType status, LocalDate date);

    Optional<NotificationEntity> findByConsultationId(Long consultationId);
}
