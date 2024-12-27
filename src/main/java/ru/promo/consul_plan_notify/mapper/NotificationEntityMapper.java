package ru.promo.consul_plan_notify.mapper;

import org.mapstruct.Mapper;
import ru.promo.consul_plan_notify.domain.Notification;
import ru.promo.consul_plan_notify.domain.entity.NotificationEntity;

@Mapper(componentModel = "spring")
public interface NotificationEntityMapper {
    NotificationEntity toEntity(Notification notification);
}

