package ru.promo.consul_plan_notify.service;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final EmailService emailService;
    private final NotificationMapper notificationMapper;
    private final NotificationEntityMapper notificationEntityMapper;

    @Override
    public void create(Notification dto) {
        notificationRepository.save(notificationEntityMapper.toEntity(dto));
    }

    @Override
    public void create(NotificationEntity entity) {
        notificationRepository.save(entity);
    }

    @Override
    public void create(ConsultationEvent consultationEvent) {
        var notification = new NotificationEntity();
        notification.setConsultationId(consultationEvent.getConsultationId());
        notification.setClientEmail(consultationEvent.getClientEmail());
        notification.setSpecialistEmail(consultationEvent.getSpecialistEmail());
        notification.setStatus(NotificationType.UNSENT);
        notification.setType(TypeStatus.CONFORMED);
        notification.setSentDateTime(LocalDateTime.now());
        notification.setConsultationDate(consultationEvent.getConsultationDate());

        notificationRepository.save(notification);
    }

    @Override
    public void update(ConsultationEvent consultationEvent) {
        // Ищем уведомление по consultationId
        Optional<NotificationEntity> optionalNotification = notificationRepository.findByConsultationId(consultationEvent.getConsultationId());

        // Используем orElseGet для создания нового уведомления, если оно не найдено
        NotificationEntity notification = optionalNotification.orElseGet(() -> {
            NotificationEntity newNotification = new NotificationEntity();
            newNotification.setConsultationId(consultationEvent.getConsultationId());
            newNotification.setClientEmail(consultationEvent.getClientEmail());
            newNotification.setSpecialistEmail(consultationEvent.getSpecialistEmail());
            newNotification.setStatus(NotificationType.UNSENT);
            newNotification.setConsultationDate(consultationEvent.getConsultationDate());
            return newNotification;
        });

        // Обновляем тип уведомления
        notification.setType(TypeStatus.CANCELLED);

        // Сохраняем уведомление в репозитории
        notificationRepository.save(notification);
    }

    @Override
    public Notification getById(Long id) {
        return notificationMapper.toDTO(notificationRepository.findById(id).orElseThrow(() -> new NotFoundException("Notification not found with id: " + id)));
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
        // Логика отправки напоминания
        NotificationEntity reminder = new NotificationEntity();
        reminder.setConsultationId(consultationId);
        reminder.setType(TypeStatus.REMAINED);
        reminder.setSentDateTime(LocalDateTime.now());
        reminder.setStatus(NotificationType.SENT);
        notificationRepository.save(reminder);

        // Отправка уведомления по электронной почте
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
        // Логика отправки напоминания
        NotificationEntity reminder = new NotificationEntity();
        reminder.setConsultationId(request.getConsultationId());
        reminder.setClientEmail(request.getClientEmail()); // Сохраняем email клиента
        reminder.setSpecialistEmail(request.getSpecialistName()); // Сохраняем email специалиста
        reminder.setType(TypeStatus.REMAINED);
        reminder.setSentDateTime(LocalDateTime.now());
        reminder.setStatus(NotificationType.SENT);
        notificationRepository.save(reminder);

        // Отправка уведомления по электронной почте
        try {
            String subject = "Напоминание о консультации";
            String text = "Уважаемый пользователь, напоминаем вам о предстоящей консультации у специалиста " + request.getSpecialistName();
            emailService.sendEmail(request.getClientEmail(), subject, text);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Page<NotificationEntity> getUnsentNotificationsTomorrow(int page, int size) {
        return notificationRepository.findByStatusAndConsultationDate(PageRequest.of(page, size), NotificationType.UNSENT, LocalDate.now().plusDays(1));
    }

    @Override
    public void markNotificationAsSent(Long notificationId) {
        NotificationEntity notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found with ID: " + notificationId));
        notification.setStatus(NotificationType.SENT);
        notificationRepository.save(notification);
    }
}
