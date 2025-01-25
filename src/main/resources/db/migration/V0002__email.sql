-- Добавление столбцов client_email и specialist_email в таблицу notification
ALTER TABLE notification
    ADD COLUMN client_email VARCHAR(255) NOT NULL;

ALTER TABLE notification
    ADD COLUMN specialist_email VARCHAR(255) NOT NULL;