CREATE SCHEMA IF NOT EXISTS pws;

CREATE TABLE IF NOT EXISTS pws.paddle (
    id UUID PRIMARY KEY,
    x INTEGER,
    y INTEGER,
    is_left_player BOOLEAN
);

INSERT INTO pws.paddle (id, x, y, is_left_player) VALUES ('f6a8f7de-5e4a-4b7d-9dc0-62fceac4b3a7', 200, 200, TRUE);

CREATE TABLE IF NOT EXISTS pws.ball (
    id UUID PRIMARY KEY,
    x INTEGER,
    y INTEGER,
    velocity_x INTEGER,
    velocity_y INTEGER
);