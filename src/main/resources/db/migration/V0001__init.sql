-- Создание таблицы notification
CREATE TABLE notification
(
    id              BIGSERIAL   NOT NULL
        CONSTRAINT notification_pk PRIMARY KEY,
    consultation_id BIGINT,
    type            VARCHAR(50) NOT NULL,
    sent_date_time  TIMESTAMP   NOT NULL,
    status          VARCHAR(50) NOT NULL
);