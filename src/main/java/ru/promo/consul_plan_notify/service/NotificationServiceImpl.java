package ru.promo.consul_plan_notify.service;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
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
    public void create(Notification dto) {
        notificationRepository.save(notificationEntityMapper.toEntity(dto));
    }

    @Override
    public void create(NotificationEntity entity) {
        notificationRepository.save(entity);
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
    public List<Notification> getAllByClientId(Long clientId) {
        return notificationMapper.toDTOList(notificationRepository.findAllByClientId(clientId));
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
    public List<NotificationEntity> getUnsentNotificationsTomorrow() {
        return notificationRepository.findByStatusAndConsultationDate(NotificationType.UNSENT, LocalDate.now().plusDays(1));
    }

    @Override
    public void markNotificationAsSent(Long notificationId) {
        NotificationEntity notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found with ID: " + notificationId));
        notification.setStatus(NotificationType.SENT);
        notificationRepository.save(notification);
    }

    @Override
    public Notification getByConsultationId(Long consultationId) {
        NotificationEntity notification = notificationRepository.findByConsultationId(consultationId);
        Notification notificationDto = notificationMapper.toDTO(notification);
        return notificationDto;
    }
}
