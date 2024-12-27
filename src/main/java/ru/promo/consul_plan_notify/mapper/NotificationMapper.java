package ru.promo.consul_plan_notify.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.promo.consul_plan_notify.domain.Notification;
import ru.promo.consul_plan_notify.domain.entity.NotificationEntity;

import java.util.List;

@Mapper(componentModel = "spring")
public interface NotificationMapper {
    Notification toDTO(NotificationEntity notificationEntity);

    List<Notification> toDTOList(List<NotificationEntity> entities);
}
