CREATE SCHEMA example;

CREATE SEQUENCE example.demo_seq START 1;

CREATE TABLE example.demo (
    id BIGINT DEFAULT nextval('example.demo_seq') PRIMARY KEY,
    name VARCHAR(64) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);