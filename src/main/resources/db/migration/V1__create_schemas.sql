CREATE SCHEMA IF NOT EXISTS shop;

CREATE TABLE IF NOT EXISTS shop.article
(
    id          SERIAL PRIMARY KEY,
    type        VARCHAR(100),
    name        VARCHAR(100),
    description VARCHAR(250),
    price       DECIMAL(5, 2)
);