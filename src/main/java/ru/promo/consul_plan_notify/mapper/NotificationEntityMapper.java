package ru.promo.consul_plan_notify.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.promo.consul_plan_notify.domain.Notification;
import ru.promo.consul_plan_notify.domain.entity.NotificationEntity;

@Mapper(componentModel = "spring")
public interface NotificationEntityMapper {

    @Mapping(source = "type", target = "type")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "consultationDate", target = "consultationDate")
    NotificationEntity toEntity(Notification notification);
}