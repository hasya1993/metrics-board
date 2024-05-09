CREATE SEQUENCE roll.board_id_seq START 1000;

CREATE TABLE roll.board (
    id BIGINT PRIMARY KEY DEFAULT nextval('roll.board_id_seq'),
    project_id BIGINT NOT NULL,
    name VARCHAR(1024) NOT NULL,
    description TEXT,
    status VARCHAR(8) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    FOREIGN KEY (project_id) REFERENCES roll.project(id)
);