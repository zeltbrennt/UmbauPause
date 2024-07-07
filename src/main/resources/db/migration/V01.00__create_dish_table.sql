CREATE SCHEMA IF NOT EXISTS shop;

CREATE TABLE IF NOT EXISTS shop.dish
(
    id          SERIAL PRIMARY KEY,
    description VARCHAR(250),
    available   BOOLEAN,
    scheduled   VARCHAR(50),
    price       NUMERIC
);