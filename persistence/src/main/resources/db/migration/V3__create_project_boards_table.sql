CREATE SEQUENCE roll.project_boards_id_seq START 1;

CREATE TABLE roll.project_boards(
    id BIGINT PRIMARY KEY DEFAULT nextval('roll.project_boards_id_seq'),
    project_id BIGINT NOT NULL,
    board_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (project_id) REFERENCES roll.project(id),
    FOREIGN KEY (board_id) REFERENCES roll.board(id),
    CONSTRAINT project_boards_unique UNIQUE (project_id, board_id)
);