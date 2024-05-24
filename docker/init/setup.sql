CREATE SCHEMA IF NOT EXISTS app;
CREATE SCHEMA IF NOT EXISTS domain;

CREATE TABLE IF NOT EXISTS app.session
(
    id           SERIAL PRIMARY KEY,
    session_data TEXT NOT NULL,
    created_at   TIMESTAMP DEFAULT current_timestamp
);

CREATE TABLE IF NOT EXISTS domain.user
(
    id       SERIAL primary key,
    role     VARCHAR(50)  NOT NULL,
    username VARCHAR(100) NOT NULL,
    password CHAR(72)     NOT NULL,
    email    VARCHAR(100) NOT NULL
);