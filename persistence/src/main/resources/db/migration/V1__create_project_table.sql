CREATE SCHEMA roll;

CREATE SEQUENCE roll.project_id_seq START 1000;

CREATE TABLE roll.project (
    id BIGINT PRIMARY KEY DEFAULT nextval('roll.project_id_seq'),
    owner_id UUID NOT NULL,
    name VARCHAR(1024) NOT NULL,
    description TEXT,
    status VARCHAR(10) NOT NULL CHECK (status IN ('active', 'suspended', 'archived')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);