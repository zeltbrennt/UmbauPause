CREATE SCHEMA IF NOT EXISTS shop;

CREATE TABLE IF NOT EXISTS shop.article
(
    id        SERIAL PRIMARY KEY,
    name      VARCHAR(250),
    available BOOLEAN,
    scheduled VARCHAR(50),
    price     NUMERIC
);