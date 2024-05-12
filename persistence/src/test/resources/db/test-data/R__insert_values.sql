INSERT INTO roll.project (id, owner_id, name, description, status)
VALUES (setval('roll.project_id_seq', 1000), gen_random_uuid(), 'Project', 'description', 'active');

INSERT INTO roll.board (id, project_id, name, description, status)
VALUES (setval('roll.board_id_seq', 1000), 1000, 'Board', 'description', 'active');

INSERT INTO roll.project_boards (id, project_id, board_id)
VALUES (setval('roll.project_boards_id_seq', 1), 1000, 1000);