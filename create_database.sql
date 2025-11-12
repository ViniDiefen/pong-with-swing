CREATE SCHEMA IF NOT EXISTS pws;

CREATE TABLE IF NOT EXISTS pws.paddle (
    id UUID PRIMARY KEY,
    x INTEGER,
    y INTEGER,
    is_left_player BOOLEAN
);

CREATE TABLE IF NOT EXISTS pws.ball (
    id UUID PRIMARY KEY,
    x INTEGER,
    y INTEGER,
    velocity_x INTEGER,
    velocity_y INTEGER
);