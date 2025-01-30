package ru.promo.consul_plan_notify.service;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.promo.consul_plan_notify.domain.ConsultationEvent;
import ru.promo.consul_plan_notify.domain.Notification;
import ru.promo.consul_plan_notify.domain.SendReminderRequest;
import ru.promo.consul_plan_notify.domain.entity.NotificationEntity;
import ru.promo.consul_plan_notify.domain.entity.NotificationType;
import ru.promo.consul_plan_notify.domain.entity.TypeStatus;
import ru.promo.consul_plan_notify.exeption.NotFoundException;
import ru.promo.consul_plan_notify.mapper.NotificationEntityMapper;
import ru.promo.consul_plan_notify.mapper.NotificationMapper;
import ru.promo.consul_plan_notify.repository.NotificationRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final EmailService emailService;
    private final NotificationMapper notificationMapper;
    private final NotificationEntityMapper notificationEntityMapper;

    @Override
    public void handleNotification(ConsultationEvent event) {
        NotificationEntity notification = switch (event.getStatus()) {
            case CONFORMED -> createNotificationEntity(event, TypeStatus.CONFORMED);
            case CANCELLED -> getOrCreateNotificationEntity(event);
            default -> throw new IllegalStateException("Неподходящий статус консультации: %s".formatted(event.getStatus()));
        };

        notificationRepository.save(notification);
    }

    private NotificationEntity createNotificationEntity(ConsultationEvent event, TypeStatus status) {
        NotificationEntity notification = new NotificationEntity();
        notification.setConsultationId(event.getConsultationId());
        notification.setClientEmail(event.getClientEmail());
        notification.setSpecialistEmail(event.getSpecialistEmail());
        notification.setStatus(NotificationType.UNSENT);
        notification.setType(status);
        notification.setSentDateTime(LocalDateTime.now());
        notification.setConsultationDate(event.getConsultationDate());
        return notification;
    }

    private NotificationEntity getOrCreateNotificationEntity(ConsultationEvent event) {
        return notificationRepository.findByConsultationId(event.getConsultationId())
                .orElseGet(() -> createNotificationEntity(event, TypeStatus.CANCELLED));
    }

    @Override
    public void create(Notification dto) {
        notificationRepository.save(notificationEntityMapper.toEntity(dto));
    }

    @Override
    public void create(NotificationEntity entity) {
        notificationRepository.save(entity);
    }

    @Override
    public Notification getById(Long id) {
        return notificationMapper.toDTO(notificationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Notification not found with id: " + id)));
    }

    @Override
    public void update(Notification dto) {
        if (notificationRepository.existsById(dto.getId())) {
            notificationRepository.save(notificationEntityMapper.toEntity(dto));
        }
    }

    @Override
    public void delete(Long id) {
        notificationRepository.deleteById(id);
    }

    @Override
    public List<Notification> getAllByConsultationId(Long consultationId) {
        return notificationMapper.toDTOList(notificationRepository.findAllByConsultationId(consultationId));
    }

    @Override
    public void sendReminder(Long consultationId, String clientEmail, String specialistEmail) {
        NotificationEntity reminder = createNotificationEntity(consultationId, clientEmail, specialistEmail, TypeStatus.REMAINED);
        notificationRepository.save(reminder);

        try {
            String subject = "Напоминание о консультации";
            String text = "Уважаемый пользователь, напоминаем вам о предстоящей консультации у специалиста " + specialistEmail;
            emailService.sendEmail(clientEmail, subject, text);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendReminder(SendReminderRequest request) {
        NotificationEntity reminder = createNotificationEntity(request.getConsultationId(), request.getClientEmail(), request.getSpecialistName(), TypeStatus.REMAINED);
        notificationRepository.save(reminder);

        try {
            String subject = "Напоминание о консультации";
            String text = "Уважаемый пользователь, напоминаем вам о предстоящей консультации у специалиста " + request.getSpecialistName();
            emailService.sendEmail(request.getClientEmail(), subject, text);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<NotificationEntity> getUnsentNotificationsTomorrow(int page, int size) {
        return notificationRepository.findByStatusAndConsultationDate(PageRequest.of(page, size), NotificationType.UNSENT, LocalDate.now().plusDays(1));
    }

    @Override
    public void markNotificationAsSent(NotificationEntity notification) {
        notification.setStatus(NotificationType.SENT);
        notificationRepository.save(notification);
    }

    private NotificationEntity createNotificationEntity(Long consultationId, String clientEmail, String specialistEmail, TypeStatus status) {
        NotificationEntity notification = new NotificationEntity();
        notification.setConsultationId(consultationId);
        notification.setClientEmail(clientEmail);
        notification.setSpecialistEmail(specialistEmail);
        notification.setStatus(NotificationType.UNSENT);
        notification.setType(status);
        notification.setSentDateTime(LocalDateTime.now());
        return notification;
    }
}
